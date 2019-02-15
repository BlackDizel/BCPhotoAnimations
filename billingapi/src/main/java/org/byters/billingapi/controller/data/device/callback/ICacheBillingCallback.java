package org.byters.billingapi.controller.data.device.callback;

public interface ICacheBillingCallback {
    void onUpdatePurchases();

    void onUpdateSkuList(boolean isSingle);
}
