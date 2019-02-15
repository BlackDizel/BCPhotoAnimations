package org.byters.bcphotoanimations.model;

import java.io.Serializable;

public class ModelInterfaceState implements Serializable {
    private boolean showLastFrame;
    private boolean isFlashEnabled;

    public boolean isShowLastFrame() {
        return showLastFrame;
    }

    public void setShowLastFrame(boolean isShow) {
        showLastFrame = isShow;
    }

    public boolean isFlashEnabled() {
        return isFlashEnabled;
    }

    public void setFlashEnabled(boolean isEnabled) {
        isFlashEnabled = isEnabled;
    }
}
