package org.byters.dataplaybilling;

import android.app.Activity;
import android.content.Context;

import com.android.billingclient.api.BillingClient;

import org.byters.billingapi.ILibDataPlayBilling;
import org.byters.dataplaybilling.controller.data.device.ICacheBilling;
import org.byters.dataplaybilling.controller.data.device.RepositoryBuy;
import org.byters.dataplaybilling.controller.data.device.RepositoryPurchasesCurrent;
import org.byters.dataplaybilling.controller.data.device.RepositorySkuList;
import org.byters.billingapi.controller.data.device.callback.ICacheBillingCallback;
import org.byters.dataplaybilling.view.ILibNavigator;

import java.util.List;

import javax.inject.Inject;

//todo implement INAPP
//todo implement SKU LIST instead of single SKU

public class LibDataPlayBilling implements ILibDataPlayBilling {

    private static LibBillingComponent component;

    @Inject
    ILibNavigator navigator;

    @Inject
    RepositorySkuList repositorySkuList;

    @Inject
    ICacheBilling cacheBilling;

    @Inject
    RepositoryBuy repositoryBuy;

    @Inject
    RepositoryPurchasesCurrent repositoryPurchasesCurrent;

    /**
     * @param context Application context
     */
    public LibDataPlayBilling(Context context) {

        component = DaggerLibBillingComponent
                .builder()
                .libBillingModule(new LibBillingModule(context))
                .build();

        component.inject(this);
    }

    public static LibBillingComponent getComponent() {
        return component;
    }

    public String getTypeSubs() {
        return BillingClient.SkuType.SUBS;
    }

    public void setActivity(Activity activity) {
        navigator.setActivity(activity);
    }

    public void requestList(List<String> listSkuNames, String type) {
        cacheBilling.setListSkuNames(listSkuNames);
        cacheBilling.setSkuType(type);
        repositorySkuList.request();
    }

    public void addListener(ICacheBillingCallback callback) {
        cacheBilling.addListener(callback);
    }

    public void requestBuy() {
        repositoryBuy.request();
    }

    public boolean isPurchased(String skuName) {
        return cacheBilling.isPurchasedExport(skuName);
    }

    public void requestPurchases() {
        repositoryPurchasesCurrent.request();
    }
}
