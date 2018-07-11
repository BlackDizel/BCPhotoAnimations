package org.byters.dataplaybilling.controller.data.device;

import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchaseHistoryResponseListener;

import org.byters.dataplaybilling.LibDataPlayBilling;

import java.util.List;

import javax.inject.Inject;

public class RepositoryPurchasesCurrent {

    @Inject
    ICacheBilling cacheBilling;

    @Inject
    IRepositoryClientBilling repositoryClientBilling;

    private RepositoryClientListener listenerClient;
    private PurchaseHistoryResponseListener listenerHistoryPurchases;

    public RepositoryPurchasesCurrent() {
        LibDataPlayBilling.getComponent().inject(this);
        listenerHistoryPurchases = new CallbackRequest();
        listenerClient = new RepositoryClientListener();
    }

    public void request() {
        if (!cacheBilling.isClientReady()) {
            repositoryClientBilling.setListener(listenerClient);
            repositoryClientBilling.request();
            return;
        }

        cacheBilling.getClientBilling().queryPurchaseHistoryAsync(cacheBilling.getSkuType(), listenerHistoryPurchases);
    }

    private class CallbackRequest implements PurchaseHistoryResponseListener {
        @Override
        public void onPurchaseHistoryResponse(int responseCode, List<Purchase> purchasesList) {
            cacheBilling.setPurchases(purchasesList);
        }
    }

    private class RepositoryClientListener implements IRepositoryClientBillingCallback {
        @Override
        public void onConnected() {
            request();
        }
    }
}
