package org.byters.bcphotoanimations.view.ui.view.callback;

import android.graphics.Point;

public interface ICameraPreviewCallback {

    int requestRotation();

    void getDisplaySize(Point displaySize);
}
