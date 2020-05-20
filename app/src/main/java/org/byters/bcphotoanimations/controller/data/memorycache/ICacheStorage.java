package org.byters.bcphotoanimations.controller.data.memorycache;


import android.util.Pair;

import org.byters.bcphotoanimations.model.PreferenceHelperEnum;

import java.util.HashMap;

public interface ICacheStorage {
    void writeObjectToFile(byte[] data, String path);

    String getAppFolder();

    String getImageExt();

    String getProjectFolderFile(String projectSelectedId, String filename, String extension);

    void writeObjectToFile(Object data, String appFolder, PreferenceHelperEnum item);

    <T> T readObjectFromFile(String folder, PreferenceHelperEnum item, Class<T> class_);

    String getProjectFolder(String projectSelectedId);

    void removeFolder(String projectFolder);

    void removeFile(String path);

    void writeProject(ICacheProjectSelected cacheProjectSelected);

    String getProjectOutputFolder(String title);

    void copyFrame(ICacheProjects cacheProjects, String projectId, int position, int framesNum);

    void removeFolder(ICacheProjects cacheProjects, String projectId);

    void writeExif(String path, HashMap<String,String> params);

    String getVideoExt();
}
