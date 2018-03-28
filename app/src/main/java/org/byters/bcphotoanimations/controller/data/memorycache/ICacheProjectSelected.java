package org.byters.bcphotoanimations.controller.data.memorycache;

public interface ICacheProjectSelected {
    void setSelectedProject(int position);

    void resetCache();

    void setProjectTitleEdit(String title);

    String getProjectTitleEdit();

    void resetTitleEdit();

    void saveProject();

    String getProjectSelectedId();

    void addFrame(byte[] data);

    boolean isEdit();

    void removeProject();

    String getFrameId(int position);

    void removeSelected();

    void revertSelected();

    int getFramesNum();

    void copySelectedFramesTo(int position);

    void moveSelectedFramesTo(int position);

    void selectRange(int val1, int val2);
}
