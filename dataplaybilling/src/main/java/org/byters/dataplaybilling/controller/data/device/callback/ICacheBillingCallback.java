package org.byters.dataplaybilling.controller.data.device.callback;

public interface ICacheBillingCallback {
    void onUpdatePurchases();

    void onUpdateSkuList(boolean isSingle);
}
