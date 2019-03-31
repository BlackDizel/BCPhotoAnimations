package org.byters.bcphotoanimations.view.presenter;

import android.text.TextUtils;

import org.byters.bcphotoanimations.ApplicationStopMotion;
import org.byters.bcphotoanimations.controller.data.memorycache.ICacheFramesSelected;
import org.byters.bcphotoanimations.controller.data.memorycache.ICacheProjectSelected;
import org.byters.bcphotoanimations.controller.data.memorycache.ICacheProjects;
import org.byters.bcphotoanimations.controller.data.memorycache.callback.ICacheFramesSelectedCallback;
import org.byters.bcphotoanimations.view.INavigator;
import org.byters.bcphotoanimations.view.presenter.callback.IPresenterAdapterFramesCallback;
import org.byters.bcphotoanimations.view.util.IHelperDialog;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

public class PresenterAdapterFrames implements IPresenterAdapterFrames {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_ITEM_ADD = 1;
    private final CacheFramesSelectedCallback cacheFramesSelectedCallback;

    @Inject
    INavigator navigator;

    @Inject
    ICacheProjectSelected cacheProjectSelected;

    @Inject
    ICacheProjects cacheProjects;

    @Inject
    ICacheFramesSelected cacheFramesSelected;

    @Inject
    IHelperDialog helperDialog;

    private WeakReference<IPresenterAdapterFramesCallback> refCallback;

    public PresenterAdapterFrames() {
        ApplicationStopMotion.getComponent().inject(this);
        cacheFramesSelected.addCallback(cacheFramesSelectedCallback = new CacheFramesSelectedCallback());
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

    @Override
    public void onClickItem(int position) {
        if (!cacheFramesSelected.isModeSelect()) {
            helperDialog.showDialogFrame(cacheProjects.getItemImagePreview(cacheProjectSelected.getProjectSelectedId(), position));
            return;
        }
        onLongClickItem(position);
    }

    @Override
    public void onLongClickItem(int position) {
        String frameId = cacheProjectSelected.getFrameId(position);
        if (TextUtils.isEmpty(frameId)) return;
        cacheFramesSelected.changeSelected(frameId);
    }

    @Override
    public void setCallback(IPresenterAdapterFramesCallback callback) {
        this.refCallback = new WeakReference<>(callback);
    }

    @Override
    public boolean isSelected(int position) {
        String frameId = cacheProjectSelected.getFrameId(position);
        if (TextUtils.isEmpty(frameId)) return false;
        return cacheFramesSelected.isSelected(frameId);

    }

    private class CacheFramesSelectedCallback implements ICacheFramesSelectedCallback {

        @Override
        public void onUpdate() {
            if (refCallback == null || refCallback.get() == null) return;
            refCallback.get().onUpdate();
        }
    }
}
