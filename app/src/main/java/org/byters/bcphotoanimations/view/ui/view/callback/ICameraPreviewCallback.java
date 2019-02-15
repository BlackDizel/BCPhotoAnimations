package org.byters.bcphotoanimations.view.ui.view.callback;

import java.util.List;

public interface ICameraPreviewCallback {

    int requestRotation();

    void onSizeChanged(int width, int height);

    void onFlashModesGet(List<String> supportedFlashModes);

}
