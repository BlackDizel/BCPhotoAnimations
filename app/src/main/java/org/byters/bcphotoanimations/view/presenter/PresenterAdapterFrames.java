package org.byters.bcphotoanimations.view.presenter;

import org.byters.bcphotoanimations.ApplicationStopMotion;
import org.byters.bcphotoanimations.controller.data.memorycache.ICacheProjectSelected;
import org.byters.bcphotoanimations.controller.data.memorycache.ICacheProjects;
import org.byters.bcphotoanimations.view.INavigator;

import javax.inject.Inject;

public class PresenterAdapterFrames implements IPresenterAdapterFrames {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_ITEM_ADD = 1;

    @Inject
    INavigator navigator;
    @Inject
    ICacheProjectSelected cacheProjectSelected;
    @Inject
    ICacheProjects cacheProjects;

    public PresenterAdapterFrames() {
        ApplicationStopMotion.getComponent().inject(this);
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
        navigator.navigateCamera();
    }

    @Override
    public int getItemsNum() {
        return cacheProjects.getItemFramesNum(cacheProjectSelected.getProjectSelectedId()) + 1;
    }

    @Override
    public String getItemImageUri(int position) {
        return cacheProjects.getItemImagePreview(cacheProjectSelected.getProjectSelectedId(), position);
    }

    @Override
    public int getItemViewType(int position) {
        return (position + 1) == getItemsNum() ? TYPE_ITEM_ADD : TYPE_ITEM;
    }
}
