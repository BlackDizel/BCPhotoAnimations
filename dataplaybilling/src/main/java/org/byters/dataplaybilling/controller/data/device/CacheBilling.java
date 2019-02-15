package org.byters.dataplaybilling.controller.data.device;

import android.content.Context;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;

import org.byters.dataplaybilling.LibDataPlayBilling;
import org.byters.billingapi.controller.data.device.callback.ICacheBillingCallback;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.WeakHashMap;

import javax.inject.Inject;

public class CacheBilling implements ICacheBilling {

    @Inject
    WeakReference<Context> refContext;

    private List<SkuDetails> skuList;
    private List<Purchase> purchases;

    private BillingClient clientBilling;
    private boolean initInProgress;

    private String skuSelected;

    private WeakHashMap<String, ICacheBillingCallback> callbacks;
    private List<String> listSkuNames;
    private String skuType;

    public CacheBilling() {
        LibDataPlayBilling.getComponent().inject(this);
    }

    @Override
    public void setSkuList(List<SkuDetails> skuDetailsList) {
        this.skuList = skuDetailsList;
        if (skuList != null && skuList.size() == 1)
            skuSelected = skuList.get(0).getSku();

        notifyListenersSkuList();
    }

    @Override
    public BillingClient getClientBilling() {
        return clientBilling;
    }

    @Override
    public void initClientBilling(PurchasesUpdatedListener listener) {
        if (refContext == null || refContext.get() == null) return;
        this.clientBilling = BillingClient
                .newBuilder(refContext.get())
                .setListener(listener)
                .build();
    }

    @Override
    public String getSkuType() {
        return skuType;
    }

    @Override
    public boolean isPurchasedExport(String skuName) {
        //todo implement


        return false;
    }

    @Override
    public List<String> getListSkuNames() {
        return listSkuNames;
    }

    @Override
    public void setListSkuNames(List<String> listSkuNames) {
        this.listSkuNames = listSkuNames;
    }

    @Override
    public void setSkuType(String type) {
        this.skuType = type;
    }

    @Override
    public boolean isClientReady() {
        return clientBilling != null && clientBilling.isReady();
    }

    @Override
    public void setPurchases(List<Purchase> purchases) {
        this.purchases = purchases;
        notifyListenersPurchases();
    }

    @Override
    public boolean isInitInProgress() {
        return initInProgress;
    }

    @Override
    public void setInitInProgress(boolean param) {
        this.initInProgress = param;
    }

    @Override
    public String getSkuSelected() {
        return skuSelected;
    }

    @Override
    public boolean isSkuSingle() {
        return skuList != null && skuList.size() == 1;
    }

    @Override
    public void addListener(ICacheBillingCallback callback) {

        if (callbacks == null)
            callbacks = new WeakHashMap<>();

        callbacks.put(callback.getClass().getName(), callback);
    }

    private void notifyListenersSkuList() {
        if (callbacks == null || callbacks.keySet() == null) return;
        for (String key : callbacks.keySet()) {
            if (key == null) continue;
            ICacheBillingCallback callback = callbacks.get(key);
            if (callback == null) continue;
            callback.onUpdateSkuList(isSkuSingle());
        }
    }

    private void notifyListenersPurchases() {
        if (callbacks == null || callbacks.keySet() == null) return;
        for (String key : callbacks.keySet()) {
            if (key == null) continue;
            ICacheBillingCallback callback = callbacks.get(key);
            if (callback == null) continue;
            callback.onUpdatePurchases();
        }
    }
}
