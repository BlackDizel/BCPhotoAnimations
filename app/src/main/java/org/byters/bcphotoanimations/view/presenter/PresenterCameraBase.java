package org.byters.bcphotoanimations.view.presenter;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;

import org.byters.bcphotoanimations.view.presenter.callback.IPresenterCameraCallback;

import java.lang.ref.WeakReference;

abstract class PresenterCameraBase implements IPresenterCamera {

    private static final int REQUEST_CAMERA_PERMISSION = 1;

    WeakReference<IPresenterCameraCallback> refCallback;

    @Override
    public void onActivityCreated(FragmentActivity activity) {

    }

    @Override
    public void onCreateView(View view) {

    }

    @Override
    public void onClickSettings() {
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

        if (requestCode != REQUEST_CAMERA_PERMISSION) return false;

        if (grantResults.length != 2
                || grantResults[0] != PackageManager.PERMISSION_GRANTED
                || grantResults[1] != PackageManager.PERMISSION_GRANTED)
            finishView();

        onPermissionGranted(view);
        return true;
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
