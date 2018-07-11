package org.byters.dataplaybilling.view;

import android.app.Activity;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingFlowParams;

public interface ILibNavigator {
    void setActivity(Activity activity);

    void buy(BillingClient clientBilling, BillingFlowParams billingFlowParams);
}
