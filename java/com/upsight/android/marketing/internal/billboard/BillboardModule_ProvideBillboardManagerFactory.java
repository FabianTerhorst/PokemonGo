package com.upsight.android.marketing.internal.billboard;

import com.upsight.android.UpsightContext;
import com.upsight.android.marketing.UpsightBillboardManager;
import com.upsight.android.marketing.internal.content.MarketingContentStore;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class BillboardModule_ProvideBillboardManagerFactory implements Factory<UpsightBillboardManager> {
    static final /* synthetic */ boolean $assertionsDisabled = (!BillboardModule_ProvideBillboardManagerFactory.class.desiredAssertionStatus());
    private final Provider<MarketingContentStore> contentStoreProvider;
    private final BillboardModule module;
    private final Provider<UpsightContext> upsightProvider;

    public BillboardModule_ProvideBillboardManagerFactory(BillboardModule module, Provider<UpsightContext> upsightProvider, Provider<MarketingContentStore> contentStoreProvider) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            if ($assertionsDisabled || upsightProvider != null) {
                this.upsightProvider = upsightProvider;
                if ($assertionsDisabled || contentStoreProvider != null) {
                    this.contentStoreProvider = contentStoreProvider;
                    return;
                }
                throw new AssertionError();
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public UpsightBillboardManager get() {
        UpsightBillboardManager provided = this.module.provideBillboardManager((UpsightContext) this.upsightProvider.get(), (MarketingContentStore) this.contentStoreProvider.get());
        if (provided != null) {
            return provided;
        }
        throw new NullPointerException("Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<UpsightBillboardManager> create(BillboardModule module, Provider<UpsightContext> upsightProvider, Provider<MarketingContentStore> contentStoreProvider) {
        return new BillboardModule_ProvideBillboardManagerFactory(module, upsightProvider, contentStoreProvider);
    }
}
