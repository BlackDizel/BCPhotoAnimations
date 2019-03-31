package org.byters.bcphotoanimations.view.presenter;

import android.text.TextUtils;

import org.byters.bcphotoanimations.ApplicationStopMotion;
import org.byters.bcphotoanimations.controller.data.memorycache.ICacheProjectSelected;
import org.byters.bcphotoanimations.view.INavigator;
import org.byters.bcphotoanimations.view.presenter.callback.IPresenterProjectCreateListener;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

public class PresenterProjectCreate implements IPresenterProjectCreate {

    @Inject
    INavigator navigator;

    @Inject
    ICacheProjectSelected cacheProjectSelected;

    private WeakReference<IPresenterProjectCreateListener> refCallback;


    public PresenterProjectCreate() {
        ApplicationStopMotion.getComponent().inject(this);
    }

    @Override
    public void onClickContinue(String title) {

        if (TextUtils.isEmpty(title)) {
            if (refCallback == null || refCallback.get() == null) return;
            refCallback.get().showErrorTitleEmpty();
            return;
        }

        cacheProjectSelected.setProjectTitleEdit(title);
        cacheProjectSelected.saveProject();

        navigator.closeProjectEdit();
        navigator.navigateProject();

        if (refCallback != null && refCallback.get() != null)
            refCallback.get().hideKeyboard();
    }

    @Override
    public void setListener(IPresenterProjectCreateListener listener) {
        refCallback = new WeakReference<>(listener);
    }
}
