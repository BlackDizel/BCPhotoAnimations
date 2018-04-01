package org.byters.bcphotoanimations.controller.data.memorycache;

import org.byters.bcphotoanimations.controller.data.memorycache.callback.ICacheProjectsCallback;
import org.byters.bcphotoanimations.model.FrameObject;

import java.util.ArrayList;
import java.util.ListIterator;

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

    ListIterator<FrameObject> getFramesIteratorList(String id);

    ListIterator<FrameObject> getFramesIteratorListReverse(String id);

    void addFrames(String projectId, ArrayList<FrameObject> frames, int position);

    void notifyListeners();

    String getFrameLastPreview(String projectSelectedId);

    String getFrameUrl(String projectSelectedId, int position);
}
