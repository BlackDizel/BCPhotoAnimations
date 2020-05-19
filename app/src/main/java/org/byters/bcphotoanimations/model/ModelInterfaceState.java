package org.byters.bcphotoanimations.model;

import java.io.Serializable;

public class ModelInterfaceState implements Serializable {
    private int showLastFrame;
    private boolean isFlashEnabled;
    private boolean isGridEnabled;
    private boolean isSettingsVisible;
    private int cameraType;

    public int isShowLastFrame() {
        return showLastFrame;
    }

    public void setShowLastFrame(int num) {
        showLastFrame = num;
    }

    public boolean isFlashEnabled() {
        return isFlashEnabled;
    }

    public void setFlashEnabled(boolean isEnabled) {
        isFlashEnabled = isEnabled;
    }

    public boolean isGridEnabled() {
        return isGridEnabled;
    }

    public void setGridEnabled(boolean isEnabled) {
        isGridEnabled = isEnabled;
    }

    public boolean isSettingsVisible() {
        return isSettingsVisible;
    }

    public void setSettingsVisible(boolean isVisible) {
        this.isSettingsVisible = isVisible;
    }

    public int getCameraType() {
        return cameraType;
    }

    public void setCameraType(int cameraType) {
        this.cameraType = cameraType;
    }
}
