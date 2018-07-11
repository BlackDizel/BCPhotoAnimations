package org.byters.dataplaybilling.controller.data.device;

import android.text.TextUtils;

import com.android.billingclient.api.BillingFlowParams;

import org.byters.dataplaybilling.LibDataPlayBilling;
import org.byters.dataplaybilling.view.ILibNavigator;

import javax.inject.Inject;

public class RepositoryBuy {

    @Inject
    ICacheBilling cacheBilling;

    @Inject
    IRepositoryClientBilling repositoryClientBilling;

    @Inject
    ILibNavigator navigator;

    private RepositoryClientListener listenerClient;

    public RepositoryBuy() {
        LibDataPlayBilling.getComponent().inject(this);

        listenerClient = new RepositoryClientListener();
    }

    public void request() {
        if (!cacheBilling.isClientReady()) {
            repositoryClientBilling.setListener(listenerClient);
            repositoryClientBilling.request();
            return;
        }

        if (TextUtils.isEmpty(cacheBilling.getSkuSelected()))
            return;

        BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                .setType(cacheBilling.getSkuType())
                .setSku(cacheBilling.getSkuSelected())
                .build();


        navigator.buy(cacheBilling.getClientBilling(), billingFlowParams);
    }

    private class RepositoryClientListener implements IRepositoryClientBillingCallback {
        @Override
        public void onConnected() {
            request();
        }
    }
}
