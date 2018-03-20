package org.byters.bcphotoanimations.controller.data.memorycache;


public interface ICacheStorage {
    void writeObjectToFile(byte[] data, String path);

    String getAppFolder();

    String getImageExt();
}
