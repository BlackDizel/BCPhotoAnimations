package org.byters.bcphotoanimations.view.ui.utils;

import android.hardware.Camera;
import android.view.Surface;

import java.util.List;

public class CameraUtils {
    public static int getDegrees(int rotation) {
        switch (rotation) {
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 270;
            default:
                return 0;
        }
    }

    public static boolean ratioIsEqual(Camera.Size size, Camera.Size previewSize) {
        return Math.abs(size.height / (float) size.width - previewSize.height / (float) previewSize.width) < 0.000001;
    }

    public static boolean isAreaMore(Camera.Size one, Camera.Size other) {
        return one == null || other == null || one.height * one.width > other.height * other.width;
    }

    public static Camera.Size getPreviewSize(Camera.Size pictureSize, List<Camera.Size> previewSizes) {

        Camera.Size result = null;

        for (Camera.Size item : previewSizes) {

            if (ratioIsEqual(item, pictureSize)
                    && (result == null || isAreaMore(item, result))
            )
                result = item;
        }

        return result;
    }
}
