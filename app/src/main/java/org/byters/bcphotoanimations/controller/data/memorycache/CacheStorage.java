package org.byters.bcphotoanimations.controller.data.memorycache;


import android.media.ExifInterface;
import android.os.Environment;
import android.text.TextUtils;

import org.byters.bcphotoanimations.BuildConfig;
import org.byters.bcphotoanimations.model.PreferenceHelperEnum;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;

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

    @Override
    public void copyFrame(ICacheProjects cacheProjects, String projectId, int i, int framesNum) {

        String title = cacheProjects.getItemTitleById(projectId);
        if (TextUtils.isEmpty(title)) return;

        String appFolder = getProjectOutputFolder(title);

        File folder = new File(appFolder);

        String fileUrl = cacheProjects.getFrameUrl(projectId, i);
        if (TextUtils.isEmpty(fileUrl)) return;

        String format = "%0" + String.valueOf(framesNum / 10 + 1).length() + "d";
        copyFile(fileUrl, folder + File.separator + String.format(format, i + 1) + getImageExt());
    }

    @Override
    public void removeFolder(ICacheProjects cacheProjects, String projectId) {

        String title = cacheProjects.getItemTitleById(projectId);
        if (TextUtils.isEmpty(title)) return;

        String projectFolder = getProjectOutputFolder(title);
        removeFolder(projectFolder);
    }

    @Override
    public void writeExif(String path, HashMap<String, String> params) {
        if (params == null || params.isEmpty()) return;

        try {
            ExifInterface exif = new ExifInterface(path);

            for (String key : params.keySet())
                exif.setAttribute(key, params.get(key));

            exif.saveAttributes();
        } catch (IOException e) {

        }

    }

    @Override
    public void writeProject(ICacheProjectSelected cacheProjectSelected) {

        String title = cacheProjectSelected.getProjectTitle();
        if (TextUtils.isEmpty(title)) return;

        String projectFolder = getProjectOutputFolder(title);

        File folder = new File(projectFolder);
        removeFolder(projectFolder);

        for (int i = 0; i < cacheProjectSelected.getFramesNum(); ++i) {
            String fileUrl = cacheProjectSelected.getFrameUrl(i);
            if (TextUtils.isEmpty(fileUrl)) continue;
            copyFile(fileUrl, folder + File.separator + i + getImageExt());
        }
    }

    @Override
    public String getProjectOutputFolder(String title) {
        return getAppFolder()
                + File.separator
                + title;
    }

    private void copyFile(String fileUrl, String outFileUrl) {
        File fileIn = new File(fileUrl);
        if (!fileIn.exists()) return;
        File fileOut = new File(outFileUrl);

        if (!fileOut.getParentFile().exists() && !fileOut.getParentFile().mkdirs())
            return;

        try {
            InputStream in = new FileInputStream(fileIn);
            try {
                OutputStream out = new FileOutputStream(fileOut);
                try {
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                } finally {
                    out.close();
                }
            } finally {
                in.close();
            }
        } catch (IOException e) {
        }
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
