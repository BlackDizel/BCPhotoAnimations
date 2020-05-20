package org.byters.bcphotoanimations.view.util;

import android.support.media.ExifInterface;

public class ExifUtil {
    public static int toOrientation(int cameraRotation) {
        return cameraRotation == 90
                ? ExifInterface.ORIENTATION_ROTATE_90
                : cameraRotation == 180
                ? ExifInterface.ORIENTATION_ROTATE_180
                : cameraRotation == 270
                ? ExifInterface.ORIENTATION_ROTATE_270
                : ExifInterface.ORIENTATION_NORMAL;
    }
}
