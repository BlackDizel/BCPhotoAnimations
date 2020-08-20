package org.byters.bcphotoanimations.view.ui.utils;

import org.byters.bcphotoanimations.model.CameraSize;

public class CameraSizeComparator implements java.util.Comparator<CameraSize> {
    @Override
    public int compare(CameraSize size1, CameraSize size2) {
        long s1 = size1.pictureSize.width * size1.pictureSize.height;
        long s2 = size2.pictureSize.width * size2.pictureSize.height;
        return Long.valueOf(s1).compareTo(s2);
    }
}
