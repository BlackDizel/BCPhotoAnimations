package org.byters.bcphotoanimations.view.presenter.callback;


import android.graphics.Point;

public interface IPresenterCameraCallback {
    void requestPermissionCamera(String[] permissions, int requestCameraPermission);

    void finishView();

    int getScreenRotation();

    int getScreenOrientation();

    void getDisplaySize(Point displaySize);

    void showFlash();
}
