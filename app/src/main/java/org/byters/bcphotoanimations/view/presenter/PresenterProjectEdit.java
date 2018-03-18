package org.byters.bcphotoanimations.view.presenter;

import android.text.TextUtils;

import org.byters.bcphotoanimations.ApplicationStopMotion;
import org.byters.bcphotoanimations.controller.data.memorycache.ICacheProjectSelected;
import org.byters.bcphotoanimations.view.INavigator;
import org.byters.bcphotoanimations.view.presenter.callback.IPresenterProjectEditCallback;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

public class PresenterProjectEdit implements IPresenterProjectEdit {

    @Inject
    ICacheProjectSelected cacheProjectSelected;
    @Inject
    INavigator navigator;

    private WeakReference<IPresenterProjectEditCallback> refCallback;

    public PresenterProjectEdit() {
        ApplicationStopMotion.getComponent().inject(this);
    }

    @Override
    public void onClickClose() {
        navigator.closeProjectEdit();
    }

    @Override
    public void onClickSave() {

        String title = cacheProjectSelected.getProjectTitleEdit();
        if (TextUtils.isEmpty(title)) {
            if (refCallback == null || refCallback.get() == null) return;
            refCallback.get().showErrorTitleEmpty();
            return;
        }

        boolean isProjectNew = cacheProjectSelected.getProjectSelectedId() == null;
        cacheProjectSelected.saveProject();
        navigator.closeProjectEdit();

        if (isProjectNew)
            navigator.navigateProject();
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
    }

    @Override
    public void setCallback(IPresenterProjectEditCallback callback) {
        this.refCallback = new WeakReference<>(callback);
    }
}
