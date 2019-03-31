package org.byters.bcphotoanimations.view.util;

import android.widget.Toast;

import org.byters.bcphotoanimations.view.ui.activity.ActivityBase;

import java.lang.ref.WeakReference;

public class ViewPopup implements IHelperPopup {

    private WeakReference<ActivityBase> refContext;

    @Override
    public void showMessage(int messageRes) {
        if (refContext == null || refContext.get() == null) return;
        Toast.makeText(refContext.get(), messageRes, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void set(ActivityBase activityBase) {
        this.refContext = new WeakReference<>(activityBase);
    }
}
