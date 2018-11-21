package org.byters.bcphotoanimations.model;

import java.io.Serializable;

public class ModelInterfaceState implements Serializable {
    private boolean showLastFrame;

    public boolean isShowLastFrame() {
        return showLastFrame;
    }

    public void setShowLastFrame(boolean isShow) {
        showLastFrame = isShow;
    }
}
