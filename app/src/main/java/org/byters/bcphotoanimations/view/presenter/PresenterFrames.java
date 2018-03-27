package org.byters.bcphotoanimations.view.presenter;


import org.byters.bcphotoanimations.ApplicationStopMotion;
import org.byters.bcphotoanimations.controller.data.memorycache.ICacheFramesSelected;
import org.byters.bcphotoanimations.controller.data.memorycache.ICacheProjectSelected;
import org.byters.bcphotoanimations.controller.data.memorycache.callback.ICacheFramesSelectedCallback;
import org.byters.bcphotoanimations.view.presenter.callback.IPresenterFramesCallback;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

public class PresenterFrames implements IPresenterFrames {

    @Inject
    ICacheFramesSelected cacheFramesSelected;

    @Inject
    ICacheProjectSelected cacheProjectSelected;

    private CacheFramesSelectedCallback cacheFramesSelectedCallback;

    private WeakReference<IPresenterFramesCallback> refCallback;


    public PresenterFrames() {
        ApplicationStopMotion.getComponent().inject(this);
        cacheFramesSelected.addCallback(cacheFramesSelectedCallback = new CacheFramesSelectedCallback());
    }

    @Override
    public void setCallback(IPresenterFramesCallback callback) {
        this.refCallback = new WeakReference<>(callback);
    }

    @Override
    public void onResume() {
        cacheFramesSelectedCallback.onUpdate();
    }

    @Override
    public void onClickPlay() {
        //todo preview
    }

    @Override
    public void onClickSelectCancel() {
        cacheFramesSelected.selectCancel();
    }

    @Override
    public void onClickSelectRange() {
//todo select range
    }

    @Override
    public void onClickRemove() {
        cacheProjectSelected.removeSelected();
    }

    @Override
    public void onClickCopy() {
//todo copy to
    }

    @Override
    public void onClickMove() {
//todo move to
    }

    @Override
    public void onClickRevert() {
        cacheProjectSelected.revertSelected();
    }

    private class CacheFramesSelectedCallback implements ICacheFramesSelectedCallback {
        @Override
        public void onUpdate() {
            if (refCallback == null || refCallback.get() == null) return;
            refCallback.get().setSelectedMode(cacheFramesSelected.isModeSelect());
        }
    }
}
