package com.upsight.android.analytics.internal.provider;

import com.upsight.android.analytics.provider.UpsightLocationTracker;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ProviderModule_ProvidesUpsightLocationTrackerFactory implements Factory<UpsightLocationTracker> {
    static final /* synthetic */ boolean $assertionsDisabled = (!ProviderModule_ProvidesUpsightLocationTrackerFactory.class.desiredAssertionStatus());
    private final Provider<LocationTracker> locationTrackerProvider;
    private final ProviderModule module;

    public ProviderModule_ProvidesUpsightLocationTrackerFactory(ProviderModule module, Provider<LocationTracker> locationTrackerProvider) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            if ($assertionsDisabled || locationTrackerProvider != null) {
                this.locationTrackerProvider = locationTrackerProvider;
                return;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public UpsightLocationTracker get() {
        UpsightLocationTracker provided = this.module.providesUpsightLocationTracker((LocationTracker) this.locationTrackerProvider.get());
        if (provided != null) {
            return provided;
        }
        throw new NullPointerException("Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<UpsightLocationTracker> create(ProviderModule module, Provider<LocationTracker> locationTrackerProvider) {
        return new ProviderModule_ProvidesUpsightLocationTrackerFactory(module, locationTrackerProvider);
    }
}
