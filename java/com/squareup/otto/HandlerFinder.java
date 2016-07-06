package com.squareup.otto;

import java.util.Map;
import java.util.Set;

interface HandlerFinder {
    public static final HandlerFinder ANNOTATED = new HandlerFinder() {
        public Map<Class<?>, EventProducer> findAllProducers(Object listener) {
            return AnnotatedHandlerFinder.findAllProducers(listener);
        }

        public Map<Class<?>, Set<EventHandler>> findAllSubscribers(Object listener) {
            return AnnotatedHandlerFinder.findAllSubscribers(listener);
        }
    };

    Map<Class<?>, EventProducer> findAllProducers(Object obj);

    Map<Class<?>, Set<EventHandler>> findAllSubscribers(Object obj);
}
