package org.byters.bcphotoanimations.view.presenter.callback;


public interface IPresenterCameraCallback {
    void requestPermissionCamera(String[] permissions, int requestCameraPermission);

    void finishView();

    int getScreenRotation();

    int getScreenOrientation();

    void showFlash();

    void showLastFrame(String path);
}
