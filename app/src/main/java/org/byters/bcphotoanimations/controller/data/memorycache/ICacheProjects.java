package org.byters.bcphotoanimations.controller.data.memorycache;

import org.byters.bcphotoanimations.controller.data.memorycache.callback.ICacheProjectsCallback;

public interface ICacheProjects {
    void addCallback(ICacheProjectsCallback callback);

    int getDataSize();

    int getItemType(int position);

    String getProjectId(int position);

    String editProject(String projectSelectedId, String title);

    String getItemTitle(int position);

    boolean isProjectNew(int position);
}
