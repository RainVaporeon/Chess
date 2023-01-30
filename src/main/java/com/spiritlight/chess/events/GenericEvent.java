package com.spiritlight.chess.events;

public abstract class GenericEvent<T> extends Event {
    private final Class<T> type;
    private final T object;

    public Class<T> getType() {
        return type;
    }

    public T get() {
        return object;
    }

    public GenericEvent(T object, Class<T> clazz) {
        this.type = clazz;
        this.object = object;
    }
}
