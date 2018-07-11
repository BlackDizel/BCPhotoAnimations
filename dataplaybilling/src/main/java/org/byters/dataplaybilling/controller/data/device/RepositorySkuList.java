package org.byters.dataplaybilling.controller.data.device;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;

import org.byters.dataplaybilling.LibDataPlayBilling;

import java.util.List;

import javax.inject.Inject;

public class RepositorySkuList {

    @Inject
    ICacheBilling cacheBilling;

    @Inject
    IRepositoryClientBilling repositoryClientBilling;

    private RepositoryClientListener listenerClient;
    private SkuDetailsResponseListener skuResponseListener;

    public RepositorySkuList() {
        LibDataPlayBilling.getComponent().inject(this);

        listenerClient = new RepositoryClientListener();
        skuResponseListener = new SkuResponseListener();
    }

    public void request() {
        if (!cacheBilling.isClientReady()) {
            repositoryClientBilling.setListener(listenerClient);
            repositoryClientBilling.request();
            return;
        }

        SkuDetailsParams skuDetailsParams = SkuDetailsParams
                .newBuilder()
                .setSkusList(cacheBilling.getListSkuNames())
                .setType(cacheBilling.getSkuType())
                .build();

        cacheBilling.getClientBilling().querySkuDetailsAsync(skuDetailsParams, skuResponseListener);
    }

    private void notifyError(int responseCode) {
        //todo implement
    }

    private class SkuResponseListener implements SkuDetailsResponseListener {
        @Override
        public void onSkuDetailsResponse(int responseCode,
                                         List<SkuDetails> skuDetailsList) {

            if (responseCode != BillingClient.BillingResponse.OK) {
                notifyError(responseCode);
                return;
            }

            cacheBilling.setSkuList(skuDetailsList);
        }
    }

    private class RepositoryClientListener implements IRepositoryClientBillingCallback {
        @Override
        public void onConnected() {
            request();
        }
    }
}
