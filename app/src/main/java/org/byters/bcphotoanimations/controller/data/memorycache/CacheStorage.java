package org.byters.bcphotoanimations.controller.data.memorycache;


import android.content.Context;
import android.os.Environment;

import org.byters.bcphotoanimations.ApplicationStopMotion;
import org.byters.bcphotoanimations.BuildConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

import javax.inject.Inject;

public class CacheStorage implements ICacheStorage {

    private final String EXT_JPG = ".jpg";

    @Inject
    WeakReference<Context> refContext;

    public CacheStorage() {
        ApplicationStopMotion.getComponent().inject(this);
    }

    @Override
    public void writeObjectToFile(byte[] data, String path) {
        File pictureFile = new File(path);

        if (!pictureFile.getParentFile().exists() && !pictureFile.getParentFile().mkdirs())
            return;

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(data);
            fos.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
    }

    @Override
    public String getAppFolder() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + BuildConfig.APPLICATION_ID;
    }

    @Override
    public String getImageExt() {
        return EXT_JPG;
    }
}
