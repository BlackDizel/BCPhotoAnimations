package org.byters.bcphotoanimations.controller.data.memorycache;

import org.byters.bcphotoanimations.controller.data.memorycache.callback.ICacheProjectsCallback;
import org.byters.bcphotoanimations.model.FrameObject;

public interface ICacheProjects {
    void addCallback(ICacheProjectsCallback callback);

    int getDataSize();

    int getItemType(int position);

    String getProjectId(int position);

    String editProject(String projectSelectedId, String title);

    String getItemTitle(int position);

    boolean isProjectNew(int position);

    int getItemFramesNum(String projectId);

    String getItemImagePreview(String projectId, int position);

    void addFrame(String projectSelectedId, FrameObject frameObject);

    void storeCache();

    String getItemTitleById(String projectSelectedId);

    void removeProject(String projectSelectedId);

    String getFrameId(String projectSelectedId, int position);
}
