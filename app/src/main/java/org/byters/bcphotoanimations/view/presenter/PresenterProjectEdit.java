package org.byters.bcphotoanimations.view.presenter;

import android.text.TextUtils;

import org.byters.bcphotoanimations.ApplicationStopMotion;
import org.byters.bcphotoanimations.controller.data.memorycache.ICacheProjectSelected;
import org.byters.bcphotoanimations.controller.data.memorycache.ICacheProjects;
import org.byters.bcphotoanimations.controller.data.memorycache.ICacheStorage;
import org.byters.bcphotoanimations.view.INavigator;
import org.byters.bcphotoanimations.view.presenter.callback.IPresenterProjectEditCallback;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

public class PresenterProjectEdit implements IPresenterProjectEdit {

    @Inject
    ICacheProjectSelected cacheProjectSelected;
    @Inject
    INavigator navigator;
    @Inject
    ICacheStorage cacheStorage;
    @Inject
    ICacheProjects cacheProjects;

    private WeakReference<IPresenterProjectEditCallback> refCallback;

    public PresenterProjectEdit() {
        ApplicationStopMotion.getComponent().inject(this);
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
        cacheProjectSelected.removeProject();
        navigator.closeProjectEdit();
        if (refCallback != null && refCallback.get() != null)
            refCallback.get().hideKeyboard();
    }

    @Override
    public void onClickExport() {
        if (refCallback != null && refCallback.get() != null) {
            refCallback.get().showDialogProgressExport(cacheProjectSelected.getProjectTitle());
            refCallback.get().exportProject(cacheProjectSelected.getProjectSelectedId());
        }
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

        if (refCallback != null && refCallback.get() != null) {
            refCallback.get().setTitle(cacheProjectSelected.getProjectTitleEdit());
            refCallback.get().setProjectEditVisibility(cacheProjectSelected.isEdit());
        }
    }

    @Override
    public void setCallback(IPresenterProjectEditCallback callback) {
        this.refCallback = new WeakReference<>(callback);
    }
}
