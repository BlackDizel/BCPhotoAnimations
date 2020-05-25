package org.byters.bcphotoanimations.view.presenter;

import android.os.Build;
import android.text.TextUtils;

import org.byters.bcphotoanimations.ApplicationStopMotion;
import org.byters.bcphotoanimations.BuildConfig;
import org.byters.bcphotoanimations.R;
import org.byters.bcphotoanimations.controller.data.memorycache.ICacheExportAttempts;
import org.byters.bcphotoanimations.controller.data.memorycache.ICacheProjectSelected;
import org.byters.bcphotoanimations.controller.data.memorycache.ICacheProjects;
import org.byters.bcphotoanimations.controller.data.memorycache.ICacheStorage;
import org.byters.bcphotoanimations.view.INavigator;
import org.byters.bcphotoanimations.view.presenter.callback.IPresenterProjectEditCallback;
import org.byters.bcphotoanimations.view.ui.dialog.listener.IDialogProjectRemoveListener;
import org.byters.bcphotoanimations.view.util.IHelperDialog;
import org.byters.bcphotoanimations.view.util.IHelperPopup;
import org.byters.billingapi.ILibDataPlayBilling;
import org.byters.billingapi.controller.data.device.callback.ICacheBillingCallback;

import java.lang.ref.WeakReference;
import java.util.Arrays;

import javax.inject.Inject;

public class PresenterProjectEdit implements IPresenterProjectEdit {

    private static final String SKU_NAME = "subscription_project_export_unlimited_monthly";
    private final IDialogProjectRemoveListener listenerDialogProjectRemove;

    @Inject
    ICacheProjectSelected cacheProjectSelected;

    @Inject
    INavigator navigator;

    @Inject
    ICacheStorage cacheStorage;

    @Inject
    ICacheProjects cacheProjects;

    @Inject
    ICacheExportAttempts cacheExportAttempts;

    @Inject
    ILibDataPlayBilling libDataPlayBilling;

    @Inject
    IHelperDialog helperDialog;

    @Inject
    IHelperPopup helperPopup;

    private CacheBillingCallback listenerCacheBilling;
    private WeakReference<IPresenterProjectEditCallback> refCallback;

    public PresenterProjectEdit() {
        ApplicationStopMotion.getComponent().inject(this);

        if (BuildConfig.DEBUG)
            cacheExportAttempts.setAttemptsUnlimited();

        libDataPlayBilling.addListener(listenerCacheBilling = new CacheBillingCallback());
        listenerDialogProjectRemove = new ListenerDialogProjectRemove();
    }

    @Override
    public void onClickClose() {
        navigator.closeProjectEdit();

        if (refCallback != null && refCallback.get() != null)
            refCallback.get().hideKeyboard();
    }

    @Override
    public void onClickSave() {

        String title = cacheProjectSelected.getProjectTitleEdit();
        if (TextUtils.isEmpty(title)) {
            if (refCallback == null || refCallback.get() == null) return;
            refCallback.get().showErrorTitleEmpty();
            return;
        }

        boolean isProjectNew = !cacheProjectSelected.isEdit();
        cacheProjectSelected.saveProject();
        navigator.closeProjectEdit();

        if (isProjectNew)
            navigator.navigateProject();

        if (refCallback != null && refCallback.get() != null)
            refCallback.get().hideKeyboard();
    }

    @Override
    public void onClickRemove() {
        helperDialog.showDialogProjectRemove(cacheProjectSelected.getProjectTitle(), listenerDialogProjectRemove);
    }

    @Override
    public void onClickExport() {
        if (!cacheExportAttempts.isEnoughAttempts()) {
            libDataPlayBilling.requestList(Arrays.asList(SKU_NAME), libDataPlayBilling.getTypeSubs());
            return;
        }

        if (refCallback != null && refCallback.get() != null) {
            refCallback.get().showDialogProgressExport(cacheProjectSelected.getProjectTitle());

            navigator.startExportServiceImages(cacheProjectSelected.getProjectSelectedId());
        }
    }

    @Override
    public void onClickExportJCodec() {
        helperDialog.showDialogExportJCodec();
    }

    @Override
    public void onClickExportMediaCodec() {
        if (!isMediaCodecAvailable()) {
            helperPopup.showMessage(R.string.error_export_video_os_version);
            return;
        }
        helperDialog.showDialogExportMediaCodec();
    }

    private boolean isMediaCodecAvailable() {
        return android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
    }

    @Override
    public void onCreateView() {
        if (refCallback == null || refCallback.get() == null) return;
        refCallback.get().setExportMediaCodecVisibility(isMediaCodecAvailable());
    }

    @Override
    public void onClickRoot() {
        navigator.closeProjectEdit();
    }

    @Override
    public void onTitleEdit(String title) {
        cacheProjectSelected.setProjectTitleEdit(title);
    }

    @Override
    public void onStart() {
        cacheProjectSelected.resetTitleEdit();

        checkExport();

        if (refCallback != null && refCallback.get() != null) {
            refCallback.get().setTitle(cacheProjectSelected.getProjectTitleEdit());
        }
    }

    private void checkExport() {
        if (!cacheProjectSelected.isEdit()) return;

        if (libDataPlayBilling.isPurchased(SKU_NAME)) {
            setViewExportUnlimited();
            return;
        }

        libDataPlayBilling.requestPurchases();
    }

    @Override
    public void setCallback(IPresenterProjectEditCallback callback) {
        this.refCallback = new WeakReference<>(callback);
    }

    private void setExportAttempts(int attempts) {
        if (refCallback == null || refCallback.get() == null) return;
        refCallback.get().setExportAttempts(attempts);
    }

    private void setViewExportUnlimited() {
        if (refCallback == null || refCallback.get() == null) return;
        refCallback.get().setExportAttemptsUnlimited();
    }

    private void setExportAttemptsNotEnough() {
        if (refCallback == null || refCallback.get() == null) return;
        refCallback.get().setExportAttemptsNotEnough();
    }

    private class CacheBillingCallback implements ICacheBillingCallback {

        @Override
        public void onUpdatePurchases() {
            if (libDataPlayBilling.isPurchased(SKU_NAME)) {
                cacheExportAttempts.setAttemptsUnlimited();
                setViewExportUnlimited();
                return;
            }

            if (cacheExportAttempts.isEnoughAttempts()) {
                setExportAttempts(cacheExportAttempts.getAttempts());
                return;
            }

            setExportAttemptsNotEnough();

        }

        @Override
        public void onUpdateSkuList(boolean isSingle) {
            if (isSingle)
                libDataPlayBilling.requestBuy();
        }
    }

    private class ListenerDialogProjectRemove implements IDialogProjectRemoveListener {
        @Override
        public void onRemove() {
            cacheProjectSelected.removeProject();
            navigator.closeProjectEdit();
            if (refCallback != null && refCallback.get() != null)
                refCallback.get().hideKeyboard();
        }
    }
}
