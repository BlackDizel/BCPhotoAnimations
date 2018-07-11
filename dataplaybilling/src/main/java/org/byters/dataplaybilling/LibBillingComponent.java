package org.byters.dataplaybilling;

import org.byters.dataplaybilling.controller.data.device.CacheBilling;
import org.byters.dataplaybilling.controller.data.device.RepositoryBuy;
import org.byters.dataplaybilling.controller.data.device.RepositoryClientBilling;
import org.byters.dataplaybilling.controller.data.device.RepositoryPurchasesCurrent;
import org.byters.dataplaybilling.controller.data.device.RepositorySkuList;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {LibBillingModule.class})
@Singleton
public interface LibBillingComponent {

    void inject(RepositoryClientBilling billingManager);

    void inject(RepositoryBuy repositoryPurchase);

    void inject(RepositorySkuList repositorySkuList);

    void inject(RepositoryPurchasesCurrent repositoryCurrentPurchases);

    void inject(LibDataPlayBilling libDataPlayBilling);

    void inject(CacheBilling cacheBilling);
}
