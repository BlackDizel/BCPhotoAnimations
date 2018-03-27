package org.byters.bcphotoanimations.controller.data.memorycache;


import org.byters.bcphotoanimations.model.PreferenceHelperEnum;

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
}
