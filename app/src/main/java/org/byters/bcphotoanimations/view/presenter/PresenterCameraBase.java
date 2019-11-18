package org.byters.bcphotoanimations.view.presenter;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;

import org.byters.bcphotoanimations.R;
import org.byters.bcphotoanimations.controller.data.memorycache.ICacheInterfaceState;
import org.byters.bcphotoanimations.controller.data.memorycache.ICacheProjectSelected;
import org.byters.bcphotoanimations.view.presenter.callback.IPresenterCameraCallback;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

abstract class PresenterCameraBase implements IPresenterCamera {

    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final int MAX_FRAMES_SHOW = 2;

    WeakReference<IPresenterCameraCallback> refCallback;

    @Inject
    ICacheInterfaceState cacheInterfaceState;

    @Inject
    ICacheProjectSelected cacheProjectSelected;

    @Override
    public void onActivityCreated(FragmentActivity activity) {

    }

    @Override
    public void onCreateView(View view) {
        setLastFrameState();
        setButtonFlashState();
        setGridState();
        setSettingsVisibility();
    }

    private void setLastFrameState() {
        if (refCallback == null || refCallback.get() == null) return;
        int framesNum = cacheInterfaceState.getShowLastFrameNum();
        refCallback.get().setLastFrameShowDrawable(
                framesNum == 0
                        ? R.drawable.ic_image_white_24dp
                        : framesNum == 1
                        ? R.drawable.ic_filter_white_24dp
                        : framesNum == 2
                        ? R.drawable.ic_filter2_white_24
                        : R.drawable.ic_image_white_24dp);
        setShowLastFrame();
    }

    @Override
    public void onClickLastFrameShow() {
        cacheInterfaceState.setLastFrameShow((cacheInterfaceState.getShowLastFrameNum() + 1) % (MAX_FRAMES_SHOW + 1));
        setLastFrameState();
    }

    @Override
    public void onClickGrid() {
        cacheInterfaceState.changeStateGrid();
        setGridState();
    }

    private void setGridState() {
        if (refCallback == null || refCallback.get() == null) return;
        refCallback.get().setButtonGridImage(cacheInterfaceState.isGridEnabled()
                ? R.drawable.ic_grid_on_white_24dp : R.drawable.ic_grid_off_white_24dp);

        refCallback.get().setButtonGridVisible(cacheInterfaceState.isGridEnabled());
    }

    @Override
    public void onClickSettings() {
        cacheInterfaceState.changeSettingsVisible();
        setSettingsVisibility();
    }

    private void setSettingsVisibility() {
        if (refCallback == null || refCallback.get() == null) return;
        refCallback.get().setSettingsVisibility(cacheInterfaceState.isSettingsVisible());
    }

    @Override
    public void onClickFlash() {
        cacheInterfaceState.changeStateFlash();
        setButtonFlashState();
    }

    private void setButtonFlashState() {
        if (refCallback == null || refCallback.get() == null) return;
        refCallback.get().setButtonFlashImage(cacheInterfaceState.isFlashEnabled()
                ? R.drawable.ic_flash_on_white_24dp : R.drawable.ic_flash_off_white_24dp);
    }


    @Override
    public void setCallback(IPresenterCameraCallback callback) {
        this.refCallback = new WeakReference<>(callback);
    }

    @Override
    public boolean onRequestPermissionsResult(View view,
                                              int requestCode,
                                              @NonNull String[] permissions,
                                              @NonNull int[] grantResults) {
        if (view == null) return false;

        if (requestCode != REQUEST_CAMERA_PERMISSION) return false;

        if (grantResults.length != 2
                || grantResults[0] != PackageManager.PERMISSION_GRANTED
                || grantResults[1] != PackageManager.PERMISSION_GRANTED)
            finishView();

        onPermissionGranted(view);
        return true;
    }

    void setShowLastFrame() {
        if (refCallback == null || refCallback.get() == null) return;
        String lastFramePath = cacheProjectSelected.getLastFramePreview();
        String lastFramePath2 = cacheProjectSelected.getLastFramePreview2();
        refCallback.get().showLastFrame(cacheInterfaceState.getShowLastFrameNum() > 0 ? lastFramePath : null);
        refCallback.get().showLastFrame2(cacheInterfaceState.getShowLastFrameNum() > 1 ? lastFramePath2 : null);
    }

    void showFlash() {
        if (refCallback == null || refCallback.get() == null) return;
        refCallback.get().showFlash();
    }

    abstract void onPermissionGranted(View view);

    void requestCameraPermission() {
        if (refCallback == null || refCallback.get() == null) return;
        refCallback.get().requestPermissionCamera(
                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_CAMERA_PERMISSION);
    }

    void finishView() {
        if (refCallback == null || refCallback.get() == null) return;
        refCallback.get().finishView();
    }


    boolean isPermissionExist(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

}
