package org.byters.bcphotoanimations.view.presenter;

import android.text.TextUtils;

import org.byters.bcphotoanimations.controller.data.memorycache.ICacheProjectSelected;
import org.byters.bcphotoanimations.view.INavigator;
import org.byters.bcphotoanimations.view.presenter.callback.IPresenterProjectEditCallback;

import java.lang.ref.WeakReference;

public class PresenterProjectEdit implements IPresenterProjectEdit {

    private WeakReference<ICacheProjectSelected> refCacheProjectSelected;
    private WeakReference<INavigator> refNavigator;
    private WeakReference<IPresenterProjectEditCallback> refCallback;

    public PresenterProjectEdit(INavigator navigator, ICacheProjectSelected cacheProjectSelected) {
        this.refNavigator = new WeakReference<>(navigator);
        this.refCacheProjectSelected = new WeakReference<>(cacheProjectSelected);

    }

    @Override
    public void onClickClose() {
        refNavigator.get().closeProjectEdit();
    }

    @Override
    public void onClickSave() {

        String title = refCacheProjectSelected.get().getProjectTitleEdit();
        if (TextUtils.isEmpty(title)) {
            if (refCallback == null || refCallback.get() == null) return;
            refCallback.get().showErrorTitleEmpty();
            return;
        }

        boolean isProjectNew = refCacheProjectSelected.get().getProjectSelectedId() == null;
        refCacheProjectSelected.get().saveProject();
        refNavigator.get().closeProjectEdit();

        if (isProjectNew)
            refNavigator.get().navigateProject();
    }

    @Override
    public void onClickRoot() {
        refNavigator.get().closeProjectEdit();
    }

    @Override
    public void onTitleEdit(String title) {
        refCacheProjectSelected.get().setProjectTitleEdit(title);
    }

    @Override
    public void onStart() {
        refCacheProjectSelected.get().resetTitleEdit();
    }

    @Override
    public void setCallback(IPresenterProjectEditCallback callback) {
        this.refCallback = new WeakReference<>(callback);
    }
}
