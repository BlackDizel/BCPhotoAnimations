package org.byters.dataplaybilling.controller.data.device;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;

import org.byters.dataplaybilling.controller.data.device.callback.ICacheBillingCallback;

import java.util.List;

public interface ICacheBilling {

    void setSkuList(List<SkuDetails> skuDetailsList);

    BillingClient getClientBilling();

    void initClientBilling(PurchasesUpdatedListener listener);

    String getSkuType();

    boolean isClientReady();

    void setPurchases(List<Purchase> purchases);

    boolean isInitInProgress();

    void setInitInProgress(boolean param);

    String getSkuSelected();

    boolean isSkuSingle();

    boolean isPurchasedExport(String skuName);

    void addListener(ICacheBillingCallback callback);

    List<String> getListSkuNames();

    void setListSkuNames(List<String> listSkuNames);

    void setSkuType(String type);
}
