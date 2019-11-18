package org.byters.bcphotoanimations.controller.data.memorycache;

public interface ICacheInterfaceState {
    void setLastFrameShow(int num);

    void changeStateFlash();

    void changeStateGrid();

    void changeSettingsVisible();

    int getShowLastFrameNum();

    boolean isFlashEnabled();

    boolean isGridEnabled();

    boolean isSettingsVisible();
}
