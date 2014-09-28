todo documentation
------------------

1. what is this
2. quick start
3. using the pom
4. explaining in depth:
handling cascading events (adding/removing a listener while running in an invocation will have effect only after the invocation is finished on all listeners)
why interfaces and not annotations? Answer: because you'll want to mock anyway and have support for refactoring
talk about return types
talk about collecting all return types
talk about cancelling events
5. unit testing