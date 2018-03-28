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
        if (refCallback == null || refCallback.get() == null) return;
        int framesNum = cacheProjectSelected.getFramesNum();
        if (framesNum == 0) return;
        refCallback.get().showAlertSelectRange(framesNum);
    }

    @Override
    public void onClickRemove() {
        cacheProjectSelected.removeSelected();
    }

    @Override
    public void onClickCopy() {
        if (refCallback == null || refCallback.get() == null) return;
        int framesNum = cacheProjectSelected.getFramesNum();
        if (framesNum == 0) return;
        refCallback.get().showAlertCopyToPosition(framesNum);
    }

    @Override
    public void onClickMove() {
        if (refCallback == null || refCallback.get() == null) return;
        int framesNum = cacheProjectSelected.getFramesNum();
        if (framesNum == 0) return;
        refCallback.get().showAlertMoveToPosition(framesNum);
    }

    @Override
    public void onClickRevert() {
        cacheProjectSelected.revertSelected();
    }

    @Override
    public void onSelectRange(int from, int to) {
        cacheProjectSelected.selectRange(from-1,to-1);
    }

    @Override
    public void onSelectCopyPosition(int position) {
        cacheProjectSelected.copySelectedFramesTo(position);
    }

    @Override
    public void onSelectMovePosition(int position) {
        cacheProjectSelected.moveSelectedFramesTo(position);
    }

    private class CacheFramesSelectedCallback implements ICacheFramesSelectedCallback {
        @Override
        public void onUpdate() {
            if (refCallback == null || refCallback.get() == null) return;
            refCallback.get().setSelectedMode(cacheFramesSelected.isModeSelect());
        }
    }
}
