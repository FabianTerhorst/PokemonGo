package com.upsight.android;

public abstract class UpsightExtension<T extends BaseComponent, U> {
    private T mExtensionComponent;

    public interface BaseComponent<T extends UpsightExtension> {
        void inject(T t);
    }

    protected abstract T onResolve(UpsightContext upsightContext);

    protected void onCreate(UpsightContext upsight) {
    }

    protected void onPostCreate(UpsightContext upsight) {
    }

    final void setComponent(T component) {
        this.mExtensionComponent = component;
    }

    public final T getComponent() {
        return this.mExtensionComponent;
    }

    public U getApi() throws IllegalStateException {
        throw new IllegalStateException("This Upsight extension supports no public API.");
    }
}
