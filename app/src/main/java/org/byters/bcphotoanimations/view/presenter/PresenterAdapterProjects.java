package org.byters.bcphotoanimations.view.presenter;

import org.byters.bcphotoanimations.controller.data.memorycache.ICacheProjectSelected;
import org.byters.bcphotoanimations.controller.data.memorycache.ICacheProjects;
import org.byters.bcphotoanimations.controller.data.memorycache.callback.ICacheProjectsCallback;
import org.byters.bcphotoanimations.model.ProjectObject;
import org.byters.bcphotoanimations.model.ProjectObjectNew;
import org.byters.bcphotoanimations.view.INavigator;
import org.byters.bcphotoanimations.view.presenter.callback.IPresenterAdapterProjectsCallback;

import java.lang.ref.WeakReference;

public class PresenterAdapterProjects implements IPresenterAdapterProjects {

    private WeakReference<INavigator> refNavigator;
    private WeakReference<ICacheProjectSelected> refCacheProjectSelected;
    private WeakReference<ICacheProjects> refCacheProjects;
    private WeakReference<IPresenterAdapterProjectsCallback> refCallback;

    private CacheCallback cacheCallback;

    public PresenterAdapterProjects(ICacheProjects cacheProjects, ICacheProjectSelected cacheProjectSelected, INavigator navigator) {
        this.refCacheProjects = new WeakReference<>(cacheProjects);
        this.refCacheProjectSelected = new WeakReference<>(cacheProjectSelected);
        this.refNavigator = new WeakReference<>(navigator);

        refCacheProjects.get().addCallback(cacheCallback = new CacheCallback());
    }

    @Override
    public int getItemsSize() {
        return refCacheProjects.get().getDataSize();
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
        return refCacheProjects.get().getItemType(position);
    }

    @Override
    public void onClickLong(int position) {
        refCacheProjectSelected.get().setSelectedProject(position);
        refNavigator.get().navigateProjectEdit();
    }

    @Override
    public void onClickItem(int position) {
        if (refCacheProjects.get().isProjectNew(position)) {
            refCacheProjectSelected.get().resetCache();
            refNavigator.get().navigateProjectCreate();
        } else {
            refCacheProjectSelected.get().setSelectedProject(position);
            refNavigator.get().navigateProject();
        }
    }

    @Override
    public String getItemTitle(int position) {
        return refCacheProjects.get().getItemTitle(position);
    }

    private class CacheCallback implements ICacheProjectsCallback {
        @Override
        public void onUpdate() {
            if (refCallback == null || refCallback.get() == null) return;
            refCallback.get().onUpdate();
        }
    }
}
