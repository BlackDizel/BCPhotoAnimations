package org.byters.dataplaybilling;

import org.byters.billingapi.controller.data.device.callback.ICacheBillingCallback;

import java.util.List;

public class LibDataPlayBilling implements org.byters.billingapi.ILibDataPlayBilling {

    @Override
    public boolean isPurchased(String skuName) {
        return false;
    }

    @Override
    public void requestBuy() {

    }

    @Override
    public void requestPurchases() {

    }

    @Override
    public void addListener(ICacheBillingCallback callback) {

    }

    @Override
    public String getTypeSubs() {
        return null;
    }

    @Override
    public void requestList(List<String> strings, String typeSubs) {

    }
}
