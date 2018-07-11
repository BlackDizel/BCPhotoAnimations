package org.byters.dataplaybilling.controller.data.device;

import android.support.annotation.Nullable;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;

import org.byters.dataplaybilling.LibDataPlayBilling;

import java.lang.ref.WeakReference;
import java.util.List;

import javax.inject.Inject;

public class RepositoryClientBilling implements IRepositoryClientBilling {

    @Inject
    ICacheBilling cacheBilling;

    private BillingClientStateListener connectionListener;
    private PurchasesUpdatedListener listenerPurchases;
    private WeakReference<IRepositoryClientBillingCallback> refCallback;

    public RepositoryClientBilling() {

        LibDataPlayBilling.getComponent().inject(this);
        connectionListener = new ConnectionListener();
        listenerPurchases = new PurchasesListener();
    }

    @Override
    public void request() {

        if (cacheBilling.isClientReady()) return;

        if (cacheBilling.isInitInProgress()) return;

        cacheBilling.initClientBilling(listenerPurchases);

        cacheBilling.setInitInProgress(true);
        cacheBilling.getClientBilling().startConnection(connectionListener);
    }

    @Override
    public void setListener(IRepositoryClientBillingCallback listener) {
        this.refCallback = new WeakReference<>(listener);
    }


    private void notifyError(int response) {
        //todo implement
    }

    private void notifySuccess() {
        if (refCallback == null || refCallback.get() == null) return;
        refCallback.get().onConnected();
    }

    private class ConnectionListener implements BillingClientStateListener {

        @Override
        public void onBillingSetupFinished(@BillingClient.BillingResponse int billingResponse) {
            cacheBilling.setInitInProgress(false);
            if (billingResponse != BillingClient.BillingResponse.OK) {
                notifyError(billingResponse);
                return;
            }

            notifySuccess();
        }

        @Override
        public void onBillingServiceDisconnected() {
            cacheBilling.setInitInProgress(false);
        }
    }

    private class PurchasesListener implements PurchasesUpdatedListener {
        @Override
        public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {
            if (responseCode != BillingClient.BillingResponse.OK)
                return;

            cacheBilling.setPurchases(purchases);
        }
    }
}