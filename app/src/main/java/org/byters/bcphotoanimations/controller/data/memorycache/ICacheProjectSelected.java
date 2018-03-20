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
}
