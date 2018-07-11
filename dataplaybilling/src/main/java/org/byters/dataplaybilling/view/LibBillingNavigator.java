package org.byters.dataplaybilling.view;

import android.app.Activity;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingFlowParams;

import java.lang.ref.WeakReference;

public class LibBillingNavigator implements ILibNavigator {

    private WeakReference<Activity> refActivity;

    @Override
    public void setActivity(Activity activity) {
        this.refActivity = new WeakReference<>(activity);
    }

    @Override
    public void buy(BillingClient clientBilling, BillingFlowParams billingFlowParams) {
        if (clientBilling == null
                || !clientBilling.isReady()
                || refActivity == null
                || refActivity.get() == null) return;
        clientBilling.launchBillingFlow(refActivity.get(), billingFlowParams);
    }

}
