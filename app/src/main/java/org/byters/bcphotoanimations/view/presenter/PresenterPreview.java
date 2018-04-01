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
        cachePreview.resetCache();
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
        onClickPlay();
        updateView();
    }

    private void updateView() {
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
        updateView();
    }

    private void updateImage() {
        if (refCallback == null || refCallback.get() == null) return;

        int position = cachePreview.getNextFrameIndex(cacheProjectSelected.getProjectSelectedFramesNum());

        refCallback.get().setImage(cacheProjectSelected.getFrameUrl(position));
    }

    private class TaskUpdate implements Runnable {
        @Override
        public void run() {

            if (!cachePreview.isPlaying()) return;
            updateImage();
            handler.postDelayed(this, cachePreview.getFrameDuration());
        }
    }
}
