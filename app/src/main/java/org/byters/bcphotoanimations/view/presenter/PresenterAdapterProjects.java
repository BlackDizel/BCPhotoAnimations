package org.byters.bcphotoanimations.view.presenter;

import org.byters.bcphotoanimations.ApplicationStopMotion;
import org.byters.bcphotoanimations.controller.data.memorycache.ICacheProjectSelected;
import org.byters.bcphotoanimations.controller.data.memorycache.ICacheProjects;
import org.byters.bcphotoanimations.controller.data.memorycache.callback.ICacheProjectsCallback;
import org.byters.bcphotoanimations.model.ProjectObject;
import org.byters.bcphotoanimations.model.ProjectObjectNew;
import org.byters.bcphotoanimations.view.INavigator;
import org.byters.bcphotoanimations.view.presenter.callback.IPresenterAdapterProjectsCallback;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

public class PresenterAdapterProjects implements IPresenterAdapterProjects {

    @Inject
    INavigator navigator;
    @Inject
    ICacheProjectSelected cacheProjectSelected;
    @Inject
    ICacheProjects cacheProjects;

    private WeakReference<IPresenterAdapterProjectsCallback> refCallback;

    private CacheCallback cacheCallback;

    public PresenterAdapterProjects() {
        ApplicationStopMotion.getComponent().inject(this);
        cacheProjects.addCallback(cacheCallback = new CacheCallback());
    }

    @Override
    public int getItemsSize() {
        return cacheProjects.getDataSize();
    }

    @Override
    public boolean isTypeProject(int viewType) {
        return viewType == ProjectObject.getType();
    }

    @Override
    public boolean isTypeProjectNew(int viewType) {
        return viewType == ProjectObjectNew.getType();
    }

    @Override
    public void setCallback(IPresenterAdapterProjectsCallback callback) {
        this.refCallback = new WeakReference<>(callback);
    }

    @Override
    public int getItemViewType(int position) {
        return cacheProjects.getItemType(position);
    }

    @Override
    public void onClickLong(int position) {
        cacheProjectSelected.setSelectedProject(position);
        navigator.navigateProjectEdit();
    }

    @Override
    public void onClickItem(int position) {
        if (cacheProjects.isProjectNew(position)) {
            cacheProjectSelected.resetCache();
            navigator.navigateProjectCreate();
        } else {
            cacheProjectSelected.setSelectedProject(position);
            navigator.navigateProject();
        }
    }

    @Override
    public String getItemTitle(int position) {
        return cacheProjects.getItemTitle(position);
    }

    @Override
    public void onClickSettings(int position) {
        onClickLong(position);
    }

    private class CacheCallback implements ICacheProjectsCallback {
        @Override
        public void onUpdate() {
            if (refCallback == null || refCallback.get() == null) return;
            refCallback.get().onUpdate();
        }
    }
}
