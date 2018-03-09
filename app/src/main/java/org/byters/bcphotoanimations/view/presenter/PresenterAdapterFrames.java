package org.byters.bcphotoanimations.view.presenter;

import org.byters.bcphotoanimations.controller.data.memorycache.ICacheProjectSelected;
import org.byters.bcphotoanimations.controller.data.memorycache.ICacheProjects;
import org.byters.bcphotoanimations.view.INavigator;

import java.lang.ref.WeakReference;

public class PresenterAdapterFrames implements IPresenterAdapterFrames {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_ITEM_ADD = 1;

    private WeakReference<INavigator> refNavigator;
    private WeakReference<ICacheProjectSelected> cacheProjectSelected;
    private WeakReference<ICacheProjects> refCacheProjects;

    public PresenterAdapterFrames(ICacheProjects cacheProjects, ICacheProjectSelected cacheProjectSelected, INavigator navigator) {
        this.refCacheProjects = new WeakReference<>(cacheProjects);
        this.cacheProjectSelected = new WeakReference<>(cacheProjectSelected);
        this.refNavigator = new WeakReference<>(navigator);
    }

    @Override
    public boolean isTypeFrame(int viewType) {
        return viewType == TYPE_ITEM;
    }

    @Override
    public boolean isTypeFrameAdd(int viewType) {
        return viewType == TYPE_ITEM_ADD;
    }

    @Override
    public void onClickFrameAdd() {
        refNavigator.get().navigateCamera();
    }

    @Override
    public int getItemsNum() {
        return refCacheProjects.get().getItemFramesNum(cacheProjectSelected.get().getProjectSelectedId()) + 1;
    }

    @Override
    public String getItemImageUri(int position) {
        return refCacheProjects.get().getItemImagePreview(cacheProjectSelected.get().getProjectSelectedId(), position);
    }

    @Override
    public int getItemViewType(int position) {
        return (position + 1) == getItemsNum() ? TYPE_ITEM_ADD : TYPE_ITEM;
    }
}
