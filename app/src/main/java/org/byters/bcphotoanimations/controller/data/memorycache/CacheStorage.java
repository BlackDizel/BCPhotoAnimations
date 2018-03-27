package org.byters.bcphotoanimations.controller.data.memorycache;


import android.os.Environment;

import org.byters.bcphotoanimations.BuildConfig;
import org.byters.bcphotoanimations.model.PreferenceHelperEnum;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class CacheStorage implements ICacheStorage {

    private final String EXT_JPG = ".jpg";

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

    @Override
    public String getProjectFolderFile(String projectSelectedId, String filename, String extension) {
        return getAppFolder()
                + File.separator
                + projectSelectedId
                + File.separator
                + filename
                + extension;
    }

    @Override
    public void writeObjectToFile(Object data, String appFolder, PreferenceHelperEnum item) {
        if (!item.getType().isInstance(data)) return;

        File folder = new File(appFolder);

        if (!folder.getParentFile().exists() && !folder.getParentFile().mkdirs())
            return;

        File file = new File(folder, item.getName());

        ObjectOutputStream objectOutputStream = null;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(data);
        } catch (IOException e) {
        }

        if (objectOutputStream == null) return;
        try {
            objectOutputStream.close();
        } catch (IOException e) {
        }
    }

    @Override
    public <T> T readObjectFromFile(String folderPath, PreferenceHelperEnum item, Class<T> class_) {
        if (item.getType() != class_)
            throwExceptionType();

        File file = new File(folderPath, item.getName());
        if (!file.exists()) return null;

        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            return null;
        }

        ObjectInputStream objectStream;
        try {
            objectStream = new ObjectInputStream(fileInputStream);
        } catch (IOException e) {
            return null;
        }

        Object object;
        try {
            object = objectStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            file.delete();
            return null;
        }

        try {
            objectStream.close();
        } catch (IOException e) {
        }

        if (!class_.isInstance(object)) {
            file.delete();
            return null;
        }


        return (T) object;
    }

    @Override
    public String getProjectFolder(String projectSelectedId) {
        return getAppFolder()
                + File.separator
                + projectSelectedId;
    }

    @Override
    public void removeFolder(String projectFolder) {
        File folder = new File(projectFolder);
        deleteRecursive(folder);
    }

    @Override
    public void removeFile(String path) {
        File file = new File(path);
        file.delete();
    }

    private void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();
    }

    private void throwExceptionType() {
        throw new ClassCastException("can't get this enum item type value. Check type in PreferenceHelperEnum");
    }
}
