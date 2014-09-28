# Flatbus for GWT

[Flatbus on github](https://github.com/ggeorgovassilis/flatbus)

Authors: George Georgovassilis (g.georgovassilis@gmail.com)

# Table of contents

1.  [Overview](#overview)
2.  [Quick start](#quickstart)
3.  [Flatbus in detail](#howitworks)
4.  [Programming with Flatbus](#using)
5.  [Testing](#testing)
6.  [FAQ &amp; troubleshooting](#faq)
7.  [Changelog](#changelog)

# <a name="overview">1. Overview</a>

Flatbus is an "event" bus for GWT which proxies listeners instead of passing message objects between them, essentially doing away with messages
as both a concept or implementation. The bus impersonates listeners by implementing the same interfaces the listeners do, removing the need for 
messages. A Flatbus instance implements the interfaces of listeners that are registered with the bus. When a method of such a proxied interface 
is invoked on the bus the bus passes control to the same method of all listeners which implement the same interface also allowing for return values.

Flatbus is available under the [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0.html) 

# <a name="quickstart">2. Quick start</a>

Let's say you have a listener interface which declares a service for resolving postal codes to place names and some implementation:

```java
interface PostalCodeLookupListener extends BusListener{

	String lookupPlaceFor(String postalCode);
}

class PostalCodeLookupListenerImpl implements PostalCodeLookupListener{

	public String lookupPlaceFor(String postalCode){
		if (postalCode.equals("1234"))
			return "Numberland";
		if (postalCode.equals("0000"))
			return "Nowhere";
		return "Unkown";
	}

}
```

All you need to do is declare a bus interface which extends `PostalCodeLookupListener` and `EventBus`:

```java
interface MyBus extends EventBus, PostalCodeLookupListener{
}

...

MyBus bus = GWT.create(MyBus.class);
```

Now you can register any `PostalCodeLookupListener` instances on the bus:

```java
bus.register(new PostalCodeLookupListenerImpl());
```

Talking to those instances through the bus is just as easy:

```java
String place = bus.lookupPlaceFor("12345");
```

And in order for all of this to work you need to include the `BusGenerator` in your GWT module xml:

```xml
<inherits name='com.bazarooma.flatbus'/>
```

# <a name="howitworks">3. Flatbus in detail</a>

## A messaging bus without messages

GWT applications organize control flow between their components with 
[MVP](https://developers.google.com/web-toolkit/articles/mvp-architecture) which leads to a clear
system architecture, isolated components with precisely delineated responsibilities and interfaces. An important side effect
of the separation of view and control logic is that business logic is unit-testable by means of [mocking](http://code.google.com/p/mockito/)
which in many ways is superior and faster to using the [GWTTestcase](https://developers.google.com/web-toolkit/doc/1.6/DevGuideTesting).

Decoupled components obviously share no direct dependencies, but they still need to be able to talk to each other in one or another way.
This is where the GWT [event bus](http://google-web-toolkit.googlecode.com/svn/javadoc/2.1/com/google/gwt/event/shared/EventBus.html)
comes into play: components register themselves with the event bus and send messages to each others either as clients or as servers.
In the remainder of this document we'll be referring to them as listeners.

GWT uses messages for communication between interested parties which has its advantages: the message class is the only dependency between
components and as it usually will be a simple data container changes to it are unlikely to break anything. Messages can be extended or
altered (i.e. new members) and can be used to communicate asynchronously as they encapsulate the state and possible invocation results on
listeners.

From a programming point of view a message bus comes with one or two nuisances: for every function involved one must declare and implement an interface 
(or a method on an already existing interface) and create a message (or modify an existing one). Furthermore, refactoring listeners
does not lead to refactored messages and does not uncover invocation mismatches because there are no direct dependencies between listeners
anymore.

Sometimes one would prefer to not have to worry about where the needed components are instantiated but just have a simple way to talk to
them, and that is what Flatbus does best: it implements in a single object _all_ listener interfaces and proxies one or many components. 
This ensures that refactoring will not leave out components (which is as discussed both a benefit and drawback) and removes the hassle of
writing message classes.

## Setting up a project

The flatbus-lib itself has no other dependencies than gwt-dev, thus including the `flatbus-lib.jar` and
`flatbus-lib-sources.jar` for the GWT compiler in the project dependencies is enough. Also, flatbus
must be activated in your project's gwt module xml:

```xml
<inherits name='com.bazarooma.flatbus'/>
```

Maven users have to set up a private repository with the Flatbus jar and then can add the dependency:

```xml
<dependency>
	<groupId>com.bazarooma.flatbus</groupId>
	<artifactId>flatbus-project</artifactId>
	<version>1.0-SNAPSHOT</version>
</dependency>
```

## Details of the BusGenerator

The library provides the `BusGenerator`, a GWT source code generator which is activated when
`GWT.create(MyBus.class)` is called with a class that implements `EventBus` and
other `BusListener` interfaces. The generator will generate a bus implementation which can
manage all listeners which have interfaces extended by `MyBus`. The generated class has all methods declared
on `MyBus` and thus all methods declared by client interfaces. When such a method is invoked on the bus,
it will iterate through all registered listeners that implement that method of the specified interface and invoke them one after another.

# <a name="using">4. Programming with Flatbus</a>

In a nut shell, Flatbus does three simple things:

*   It removes the need for a component to know where to find another component it depends on
*   It allows direct method invocation of a listener's interface on the bus because the bus proxies the listener
*   It handles "broadcasting" invocations to multiple listeners

## Listeners

In a traditional messaging bus listeners are components which receive messages send by clients through the bus to them. For a Flatbus listener
implements an interface derived from `BusClient` which is also implemented by the bus. A component that wishes to
communicate with one or more of the listeners registered with the bus needs a reference only to the bus. It then can cast the bus to the
listener's interface and directly invoke a listener method on that bus.

The first step is declaring a listener interface which must extend `BusClient` and declare all methods it would
like to expose as public functionality to other components over the bus. There are no restrictions as to the method names, arguments
and return types (void, primitives, object, collections, arrays...). For example:

```java
interface PostalCodeLookupListener extends BusListener{
	String lookupPlaceFor(String postalCode);
}
```

You'll also need an implementation. It is possible to have multiple implementations of the listener interface, also an implementation 
can implement multiple listener interfaces.

```java
class PostalCodeLookupListenerImpl implements PostalCodeLookupListener{
	public String lookupPlaceFor(String postalCode){
		...
		return ...;
	}
}
```

After you have declared all listener interfaces you need to declare the bus interface which must extend `EventBus` and all
listener interfaces it should proxy. Make sure to not declare any other methods on the bus interface since the generated bus won't know how
to implement them.

```java
interface MyBus extends EventBus, PostalCodeLookupListener, ...{ 
}
```

A bus is then instantiated as follows, where it is possible to instantiate more than one buses:

```java
MyBus bus = GWT.create(MyBus.class);
```

After registering a listener instance on the bus it will be able to receive method invocations. Unregistering it will cease method
invocation from the bus.

```java
PostalCodeLookupListener listener = new PostalCodeLookupListenerImpl();

bus.register(listener);
```
`
bus.unregister(listener);
`

## Invoking listeners

A Flatbus implements all listener interfaces, thus invoking a listener method goes through invoking that method on the bus instance: 

```java
String place = bus.lookupPlaceFor("1234");
Window.alert(place);
```

When more than one `PostalCodeLookupListener`s instances were registered with the bus and the invoked method returns
a value, then only the value of the first method invocation is returned although all listeners are still being invoked. All return values
can be accessed by chaining the `all` or `allWithValues` methods where the first returns all invocation
values as returned by the listener methods while the later filters out null return values.

```java
List<String> places = bus.all(bus.lookupPlaceFor("1234"));
```

A listener can abort the invocation similar to canceling an event with the traditional GWT event bus, but it requires a reference to the
bus to do that.

```java
public class PostalCodeLookupListenerImpl implements PostalCodeLookupListener{

	private MyEventBus bus;

	public PostalCodeLookupListenerImpl(MyEventBus bus){
		this.bus = bus;
	}

	@Override
	public String lookupPlaceFor(String postalCode){
		if (postalCode.startsWith("99")){
			bus.cancelCurrentEvent();		
			return "Point of no return";
		}
		return null;
	}
}
```

# <a name="testing">5. Testing</a>

Flatbus can be used without restrictions with Selenium type integration tests, `GWTTestCase` unit tests or
pure unit tests which mock GWT dependencies. Examples for the latter two cases can be found in the `flatbus-test` project.

## Unit testing with GWTTestCase

```java
public class Tests extends GWTTestCase{

	protected PostalCodeLookupService postalCodesInCountryA;
	protected MyEventBus eventBus;

	@Override
	public String getModuleName() {
		return "my.project.Tests";
	}

	@Override
	public void gwtSetUp(){
		eventBus = GWT.create(MyEventBus.class);
		postalCodesInCountryA = new PostalCodeLookupServiceImpl(...);
		eventBus.register(postalCodesInCountryA);
	}

	public void testPostalCodes() {
		String result = eventBus.lookupPlaceFor("75018");
		assertEquals("Paris", result);
	}
}
```

## Unit testing with mocking

When mocking GWT dependencies one wants to avoid instantiating the complete GWT browser environment of a `GWTTestCase`.
Since `GWT.create` is not available in that stripped-down unit test a different implementation can be used instead:
`ServerEventBus`.

```java
public class Tests{

	protected PostalCodeLookupService postalCodesInCountryA;
	protected MyEventBus eventBus;

	@Before
	@Override
	public void setup(){
		ServerEventBusFactory factory = new ServerEventBusFactory();
		bus = factory.createAndProxy(MyEventBus.class);
		postalCodesInCountryA = new PostalCodeLookupServiceImpl(...);
		eventBus.register(postalCodesInCountryA);
	}

	public void testPostalCodes() {
		String result = eventBus.lookupPlaceFor("75018");
		assertEquals("Paris", result);
	}
}
```

# <a name="faq">6. FAQ &amp; troubleshooting</a>

## Why interfaces and not annotations?

The IDE refactoring tools currently available handle annotations badly and specifically won't raise errors when a method mismatch
arises as the result of a code modification. This would result in an error condition at run time which for many development purposes is
too late. Furthermore, we wanted to be able to test mocked implementations of both the bus and listeners which is difficult with
annotations.

## What happens when a listener throws an exception?

All listeners following the failed listener will still be invoked. The exception thrown will be collected together with any other
exceptions in an `UmbrellaException` and thrown after all listeners have been invoked.

## What happens when no listener is registered for a method invocation?

<p>If the method is declared to not return a value (`void`) then nothing will happen. If the method
declares a return value then an exception is thrown because the Flatbus wouldn't know what value to return.

## Method invocations on the bus are synchronous/blocking, right?

Yes. When a method on the bus is called then all listeners are processed before the control returns to the caller.

## Recursive/cascading invocations: What happens when a listener which is currently being invoked by the bus calls the bus again?

Actually nothing spectacular happens, this is quite intuitive. Assuming the following listeners with their methods:

```java
A.ma(...){
	...
	bus.mb(...);
};

B.mb(...){
	...
}
```

and the event bus which implements `A.ma` and `B.mb`, when `bus.ma`
is invoked which in turn invokes `bus.mb` then calls are processed in a stack. This means that `bus.mb`
is processed first and afterwards the control is returned to `A.ma`.

## How are listeners processed which are registered or unregistered on the bus during an invocation?

When a listener registers or unregisters during an invocation an other listeners L then this change does not affect the current invocation:
Registering L during the invocation will not get it invoked and unregistering L will now save it from being invoked either.
The best way to understand the invocation process is to describe what the implementation does: when a listener method is invoked on the
bus it determines at that point all eligible listeners and creates an invocation schedule. Any listeners that are added or removed
during the invocation will not affect that schedule, but they will affect new schedules (even recursive ones caused by the current invocation).

## Are there restrictions on what listener methods can be?

There hardly are any relevant restrictions on listener methods with the notable exception that since the generated bus implementation
implements _all_ listener interfaces it is not clear what should happen when two interfaces declare the same method. While this
would be a single invocation method on the bus, it is unclear which type of listeners should process that invocation. Ie:

```java
interface Listener1 extends BusListener{
	void someMethod();
} 

interface Listener2 extends BusListener{
	void someMethod();
} 

interface MyBus extends Listener1, Listener2, EventBus{
}

...

MyBus bus = GWT.create(MyBus.class);

bus.someMethod(); // <-- is that Listener1.someMethod or Listener2.someMethod ?
```

## Can there be more than one implementation of a listener interface?

As many as you'd like.

## Can multiple instances of a listener register with the event bus?

Yes, and they will be all invoked when their declared method is invoked on the bus. The normal method invocation on the bus will 
return only the first invocation result, in conjunction with the `all` or 
`allWithValues` methods it will return a list with all invocation values.

# <a name="changelog">7. Changelog</a>

### 1.1
Cleaned up POMs and documentation

### 1.0
Initial release
