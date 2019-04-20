package org.byters.bcphotoanimations.view.ui.view.callback;

import android.hardware.Camera;

import java.util.List;

public interface ICameraPreviewCallback {

    int requestRotation();

    void onSizeChanged(int width, int height);

    void onFlashModesGet(List<String> supportedFlashModes);

    void onGetPictureSize(Camera.Size previewSize, Camera.Size photoSize);
}
