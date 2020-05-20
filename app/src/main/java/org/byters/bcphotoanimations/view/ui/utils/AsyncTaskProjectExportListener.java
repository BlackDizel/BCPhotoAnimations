package org.byters.bcphotoanimations.view.ui.utils;

public interface AsyncTaskProjectExportListener {
    void onUpdate(String projectId, Integer[] values);

    void onComplete(String projectId, String file);

    void onCompleteFolder(String projectId, String folder);
}
