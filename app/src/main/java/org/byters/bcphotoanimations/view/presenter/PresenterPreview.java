package org.byters.bcphotoanimations.view.presenter;

import android.os.Handler;

import org.byters.bcphotoanimations.ApplicationStopMotion;
import org.byters.bcphotoanimations.controller.data.memorycache.ICachePreview;
import org.byters.bcphotoanimations.controller.data.memorycache.ICacheProjectSelected;
import org.byters.bcphotoanimations.view.presenter.callback.IPresenterPreviewCallback;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

public class PresenterPreview implements IPresenterPreview {

    @Inject
    ICacheProjectSelected cacheProjectSelected;

    @Inject
    ICachePreview cachePreview;

    private Handler handler;
    private Runnable taskUpdate;
    private WeakReference<IPresenterPreviewCallback> refCallback;
    private boolean isTracking;

    public PresenterPreview() {
        ApplicationStopMotion.getComponent().inject(this);

        taskUpdate = new TaskUpdate();

        handler = new Handler();
    }

    @Override
    public void selCallback(IPresenterPreviewCallback callback) {
        this.refCallback = new WeakReference<>(callback);
    }

    @Override
    public void onCreateView() {
        cachePreview.resetCache(cacheProjectSelected.getProjectSelectedFramesNum());
    }

    @Override
    public void onClickFrame() {
        cachePreview.stop();
        setViewButtonPlayVisibility(true);
    }

    private void setViewButtonPlayVisibility(boolean isVisible) {
        if (refCallback == null || refCallback.get() == null) return;
        refCallback.get().setButtonPlayVisibility(isVisible);
    }

    @Override
    public void onClickPlay() {
        cachePreview.play();
        taskUpdate.run();
        setViewButtonPlayVisibility(false);
    }

    @Override
    public void onStart() {
        onTrackingStop();
        onClickPlay();
        updateViewFPS();
        setViewFrameCurrent();
        setViewFramesRange();
    }

    private void setViewFramesRange() {
        if (refCallback == null || refCallback.get() == null) return;
        refCallback.get().setFramesRange(cachePreview.getFrameRangeFrom(), cachePreview.getFrameRangeTo());
    }

    private void setViewFrameCurrent() {
        if (refCallback == null || refCallback.get() == null) return;
        refCallback.get().setFrameCurrent(
                cachePreview.getFrameRangeTo() - cachePreview.getFrameRangeFrom(),
                cachePreview.getFrameCurrentIndex() - cachePreview.getFrameRangeFrom(),
                !isTracking);
    }

    private void updateViewFPS() {
        if (refCallback == null || refCallback.get() == null) return;
        refCallback.get().setFPS((int) (1000 / cachePreview.getFrameDuration()));
    }

    @Override
    public void onStop() {
        onClickFrame();
    }

    @Override
    public void onClickFPS() {
        cachePreview.changeFPS();
        updateViewFPS();
    }

    @Override
    public void onClickFrameRange() {
        //todo show alert
    }

    @Override
    public void onTrackingStart() {
        isTracking = true;
        onClickFrame();
    }

    @Override
    public void onTrackingStop() {
        isTracking = false;
    }

    @Override
    public void onFrameSelected(int position) {
        cachePreview.setFrameFromView(position);
        setViewFrame();
        setViewFrameCurrent();
    }

    private void setViewFrame() {
        if (refCallback == null || refCallback.get() == null) return;

        refCallback.get().setImage(cacheProjectSelected.getFrameUrl(cachePreview.getFrameCurrentIndex()));
    }

    private class TaskUpdate implements Runnable {
        @Override
        public void run() {

            if (!cachePreview.isPlaying()) return;

            cachePreview.getNextFrameIndex();
            setViewFrame();
            setViewFrameCurrent();
            handler.postDelayed(this, cachePreview.getFrameDuration());
        }
    }
}
