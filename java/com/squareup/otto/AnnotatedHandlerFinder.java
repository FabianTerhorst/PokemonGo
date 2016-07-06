package com.squareup.otto;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

final class AnnotatedHandlerFinder {
    private static final ConcurrentMap<Class<?>, Map<Class<?>, Method>> PRODUCERS_CACHE = new ConcurrentHashMap();
    private static final ConcurrentMap<Class<?>, Map<Class<?>, Set<Method>>> SUBSCRIBERS_CACHE = new ConcurrentHashMap();

    private static void loadAnnotatedProducerMethods(Class<?> listenerClass, Map<Class<?>, Method> producerMethods) {
        loadAnnotatedMethods(listenerClass, producerMethods, new HashMap());
    }

    private static void loadAnnotatedSubscriberMethods(Class<?> listenerClass, Map<Class<?>, Set<Method>> subscriberMethods) {
        loadAnnotatedMethods(listenerClass, new HashMap(), subscriberMethods);
    }

    private static void loadAnnotatedMethods(Class<?> listenerClass, Map<Class<?>, Method> producerMethods, Map<Class<?>, Set<Method>> subscriberMethods) {
        for (Method method : listenerClass.getDeclaredMethods()) {
            if (!method.isBridge()) {
                Class<?>[] parameterTypes;
                Class<?> eventType;
                if (method.isAnnotationPresent(Subscribe.class)) {
                    parameterTypes = method.getParameterTypes();
                    if (parameterTypes.length != 1) {
                        throw new IllegalArgumentException("Method " + method + " has @Subscribe annotation but requires " + parameterTypes.length + " arguments.  Methods must require a single argument.");
                    }
                    eventType = parameterTypes[0];
                    if (eventType.isInterface()) {
                        throw new IllegalArgumentException("Method " + method + " has @Subscribe annotation on " + eventType + " which is an interface.  Subscription must be on a concrete class type.");
                    } else if ((method.getModifiers() & 1) == 0) {
                        throw new IllegalArgumentException("Method " + method + " has @Subscribe annotation on " + eventType + " but is not 'public'.");
                    } else {
                        Set<Method> methods = (Set) subscriberMethods.get(eventType);
                        if (methods == null) {
                            methods = new HashSet();
                            subscriberMethods.put(eventType, methods);
                        }
                        methods.add(method);
                    }
                } else if (method.isAnnotationPresent(Produce.class)) {
                    parameterTypes = method.getParameterTypes();
                    if (parameterTypes.length != 0) {
                        throw new IllegalArgumentException("Method " + method + "has @Produce annotation but requires " + parameterTypes.length + " arguments.  Methods must require zero arguments.");
                    } else if (method.getReturnType() == Void.class) {
                        throw new IllegalArgumentException("Method " + method + " has a return type of void.  Must declare a non-void type.");
                    } else {
                        eventType = method.getReturnType();
                        if (eventType.isInterface()) {
                            throw new IllegalArgumentException("Method " + method + " has @Produce annotation on " + eventType + " which is an interface.  Producers must return a concrete class type.");
                        } else if (eventType.equals(Void.TYPE)) {
                            throw new IllegalArgumentException("Method " + method + " has @Produce annotation but has no return type.");
                        } else if ((method.getModifiers() & 1) == 0) {
                            throw new IllegalArgumentException("Method " + method + " has @Produce annotation on " + eventType + " but is not 'public'.");
                        } else if (producerMethods.containsKey(eventType)) {
                            throw new IllegalArgumentException("Producer for type " + eventType + " has already been registered.");
                        } else {
                            producerMethods.put(eventType, method);
                        }
                    }
                } else {
                    continue;
                }
            }
        }
        PRODUCERS_CACHE.put(listenerClass, producerMethods);
        SUBSCRIBERS_CACHE.put(listenerClass, subscriberMethods);
    }

    static Map<Class<?>, EventProducer> findAllProducers(Object listener) {
        Class<?> listenerClass = listener.getClass();
        Map<Class<?>, EventProducer> handlersInMethod = new HashMap();
        Map<Class<?>, Method> methods = (Map) PRODUCERS_CACHE.get(listenerClass);
        if (methods == null) {
            methods = new HashMap();
            loadAnnotatedProducerMethods(listenerClass, methods);
        }
        if (!methods.isEmpty()) {
            for (Entry<Class<?>, Method> e : methods.entrySet()) {
                handlersInMethod.put(e.getKey(), new EventProducer(listener, (Method) e.getValue()));
            }
        }
        return handlersInMethod;
    }

    static Map<Class<?>, Set<EventHandler>> findAllSubscribers(Object listener) {
        Class<?> listenerClass = listener.getClass();
        Map<Class<?>, Set<EventHandler>> handlersInMethod = new HashMap();
        Map<Class<?>, Set<Method>> methods = (Map) SUBSCRIBERS_CACHE.get(listenerClass);
        if (methods == null) {
            methods = new HashMap();
            loadAnnotatedSubscriberMethods(listenerClass, methods);
        }
        if (!methods.isEmpty()) {
            for (Entry<Class<?>, Set<Method>> e : methods.entrySet()) {
                Set<EventHandler> handlers = new HashSet();
                for (Method m : (Set) e.getValue()) {
                    handlers.add(new EventHandler(listener, m));
                }
                handlersInMethod.put(e.getKey(), handlers);
            }
        }
        return handlersInMethod;
    }

    private AnnotatedHandlerFinder() {
    }
}
