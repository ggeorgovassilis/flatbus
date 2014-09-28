package com.bazarooma.flatbus.generator;

import com.bazarooma.flatbus.shared.AbstractEventBus;
import com.bazarooma.flatbus.shared.BusListener;
import com.bazarooma.flatbus.shared.EventBus;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.TreeLogger.Type;
import com.google.gwt.core.ext.UnableToCompleteException;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JParameter;
import com.google.gwt.core.ext.typeinfo.JPrimitiveType;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * The {@link BusGenerator} is executed when {@link GWT#create(Class)} is used
 * on an interface which extends {@link EventBus} and implements one or more
 * interfaces extending {@link BusListener}. It generates an event bus which
 * acts as a proxy for these listeners and forwards method invocations to the
 * listeners.
 * 
 * @author george georgovassilis
 * 
 */
public class BusGenerator extends Generator {

	/**
	 * Return interfaces which extend {@link BusListener} and are implemented by classType
	 * 
	 * @param classType
	 * @param logger
	 * @param context
	 * @return
	 * @throws Exception
	 */
	protected List<JClassType> getListenerInterfaces(JClassType classType, TreeLogger logger, GeneratorContext context)
			throws Exception {
		List<JClassType> interfacesToImplement = new ArrayList<JClassType>();
		JClassType[] implementedInterfaces = classType.getImplementedInterfaces();
		for (JClassType listenerInterface : implementedInterfaces) {
			JClassType busListenerClass = context.getTypeOracle().getType(BusListener.class.getCanonicalName());
			if (!listenerInterface.isAssignableTo(busListenerClass))
				continue;
			interfacesToImplement.add(listenerInterface);
		}
		return interfacesToImplement;
	}

	/**
	 * Returns a string representation of "type" converting primitives to their boxed counterpart
	 * @param type
	 * @return
	 */
	protected String getBoxedTypeAsString(JType type){
		String name = type.getQualifiedSourceName();
		JPrimitiveType primitive = type.isPrimitive();
		if (primitive!=null)
			name = primitive.getQualifiedBoxedSourceName();
		return name;
	}
	
	/**
	 * Implement a method of the registered interface. The implementation generated iterates over listeners and
	 * invokes the specified "method" on them
	 * 
	 * @param listenerInterface
	 * @param method
	 * @param src
	 */
	protected void implementListenerMethod(JClassType listenerInterface, JMethod method, SourceWriter src) {
		
		JType returnType = method.getReturnType();
		boolean hasReturnType = !"void".equals(returnType.getSimpleSourceName());
		String cName = listenerInterface.getParameterizedQualifiedSourceName();
		String boxedReturnType = getBoxedTypeAsString(returnType);
		src.println("@Override");
		src.print("public "+returnType.getQualifiedSourceName()+" "+method.getName()+"(");
		String prefix = "";
		for (JParameter param:method.getParameters()){
			src.print(prefix+" final "+param.getType().getQualifiedSourceName()+" "+param.getName());
			prefix=",";
		}
		src.println("){");
		String template = "<"+cName+","+boxedReturnType+">";
		src.println("Executable"+template+" __executable = new Executable"+template+"(){");
		src.println("@Override");
		src.println("public "+boxedReturnType+" execute("+cName+" listener){");

		if (hasReturnType){
			src.print("return ");
		}
		src.print("listener." + method.getName() + "(");
		String delimiter = "";
		for (JParameter param : method.getParameters()) {
			src.print(delimiter + param.getName());
			delimiter = ", ";
		}
		src.println(");");
		if (!hasReturnType){
			src.print("return null;");
		}
		src.println("}");
		src.println("};");
		src.println("super.invokeListeners(\""+cName+"\", __executable, "+hasReturnType+");");
		if (hasReturnType){
			src.println("return __executable.results.get(0);");
		}
		src.println("}");
	}

	/**
	 * Implements {@link AbstractEventBus#getInterfaceNameForListener} with a cascade of instanceof checks
	 * of known listener interfaces against the provided listener
	 * @param listenerInterfaces
	 * @param src
	 */
	//TODO: a possibly faster implementation would be maintaining a map of listeners to their known interfaces
	protected void implementGetInterfaceNamesForListenerMethod(List<JClassType> listenerInterfaces, SourceWriter src) {
		src.println("@Override");
		src.println("protected List<String> getInterfaceNamesForListener(" + BusListener.class.getCanonicalName() + " listener){");
		src.println("List<String> list = new ArrayList<String>();");
		for (JClassType jc : listenerInterfaces) {
			String className = jc.getParameterizedQualifiedSourceName();
			src.println("if (listener instanceof " + className + ") list.add(\"" + className + "\");");
		}
		src.println("return list;");
		src.println("}");
	}

	/**
	 * Builds the service bus which implements all service interfaces
	 */
	@Override
	public String generate(TreeLogger logger, GeneratorContext context, String typeName) throws UnableToCompleteException {
		JClassType classType;
		try {
			classType = context.getTypeOracle().getType(typeName);
			SourceWriter src = getSourceWriter(classType, context, logger);
			if (src == null) {
				return typeName + "Generated";
			}
			String nameOfThisInterface = classType.getName();
			logger.log(Type.DEBUG, "Creating service bus " + nameOfThisInterface);
			// Find all interfaces that extend BusListener
			List<JClassType> interfacesToImplement = getListenerInterfaces(classType, logger, context);
			if (interfacesToImplement.isEmpty())
				throw new IllegalArgumentException(
						"Event bus does not implement any listener interfaces. Listener interfaces must extend "
								+ BusListener.class.getCanonicalName());
			for (JClassType listenerInterface : interfacesToImplement) {
				logger.log(Type.DEBUG, "Implementing listener interface " + listenerInterface);
				for (JMethod method : listenerInterface.getMethods()) {
					implementListenerMethod(listenerInterface, method, src);
				}
			}
			implementGetInterfaceNamesForListenerMethod(interfacesToImplement, src);
			src.commit(logger);
			return typeName + "Generated";
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public SourceWriter getSourceWriter(JClassType classType,
			GeneratorContext context, TreeLogger logger) {
		String packageName = classType.getPackage().getName();
		String simpleName = classType.getSimpleSourceName() + "Generated";
		ClassSourceFileComposerFactory composer = new ClassSourceFileComposerFactory(
				packageName, simpleName);
		composer.setSuperclass(AbstractEventBus.class.getCanonicalName());
		composer.addImplementedInterface(classType.getName());

		composer.addImport(List.class.getCanonicalName());
		composer.addImport(ArrayList.class.getCanonicalName());
		composer.addImport(IllegalArgumentException.class.getCanonicalName());
		composer.addImport(BusListener.class.getCanonicalName());

		PrintWriter printWriter = context.tryCreate(logger, packageName,
				simpleName);
		if (printWriter == null) {
			return null;
		} else {
			SourceWriter sw = composer.createSourceWriter(context, printWriter);
			return sw;
		}
	}
}
