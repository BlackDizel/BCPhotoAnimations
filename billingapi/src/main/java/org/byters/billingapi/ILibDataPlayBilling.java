package org.byters.billingapi;

import org.byters.billingapi.controller.data.device.callback.ICacheBillingCallback;

import java.util.List;

public interface ILibDataPlayBilling {
    boolean isPurchased(String skuName);

    void requestBuy();

    void requestPurchases();

    void addListener(ICacheBillingCallback callback);

    String getTypeSubs();

    void requestList(List<String> strings, String typeSubs);
}
