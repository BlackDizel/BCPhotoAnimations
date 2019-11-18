package org.byters.bcphotoanimations.view.presenter.callback;


public interface IPresenterCameraCallback {
    void requestPermissionCamera(String[] permissions, int requestCameraPermission);

    void finishView();

    int getScreenRotation();

    void showFlash();

    void showLastFrame(String path);

    void setLastFrameShowDrawable(int drawableRes);

    void setLastFrameSize(int width, int height);

    void setFlashVisible(boolean isFlashSupported);

    void setButtonFlashImage(int drawableRes);

    void showPictureSize(String message);

    void setButtonGridImage(int drawableRes);

    void setButtonGridVisible(boolean gridEnabled);

    void setSettingsVisibility(boolean settingsVisible);

    void showLastFrame2(String path);
}
