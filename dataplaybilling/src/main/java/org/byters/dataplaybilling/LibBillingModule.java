package org.byters.dataplaybilling;

import android.content.Context;

import org.byters.dataplaybilling.controller.data.device.CacheBilling;
import org.byters.dataplaybilling.controller.data.device.ICacheBilling;
import org.byters.dataplaybilling.controller.data.device.IRepositoryClientBilling;
import org.byters.dataplaybilling.controller.data.device.RepositoryBuy;
import org.byters.dataplaybilling.controller.data.device.RepositoryClientBilling;
import org.byters.dataplaybilling.controller.data.device.RepositoryPurchasesCurrent;
import org.byters.dataplaybilling.controller.data.device.RepositorySkuList;
import org.byters.dataplaybilling.view.ILibNavigator;
import org.byters.dataplaybilling.view.LibBillingNavigator;

import java.lang.ref.WeakReference;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class LibBillingModule {

    private WeakReference<Context> refContextApp;

    public LibBillingModule(Context context) {
        this.refContextApp = new WeakReference<>(context);
    }

    @Provides
    WeakReference<Context> getRefContext() {
        return refContextApp;
    }

    @Provides
    @Singleton
    ICacheBilling getCacheBilling() {
        return new CacheBilling();
    }

    @Provides
    @Singleton
    IRepositoryClientBilling getRepositoryClientBilling() {
        return new RepositoryClientBilling();
    }

    @Provides
    @Singleton
    RepositoryPurchasesCurrent getRepositoryPurchasesCurrent() {
        return new RepositoryPurchasesCurrent();
    }

    @Provides
    @Singleton
    RepositoryBuy getRepositoryBuy() {
        return new RepositoryBuy();
    }

    @Provides
    @Singleton
    RepositorySkuList getRepositorySkuList() {
        return new RepositorySkuList();
    }

    @Provides
    @Singleton
    ILibNavigator getNavigator() {
        return new LibBillingNavigator();
    }

}
