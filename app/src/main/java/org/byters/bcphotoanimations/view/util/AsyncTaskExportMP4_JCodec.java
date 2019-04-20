package org.byters.bcphotoanimations.view.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.support.media.ExifInterface;

import org.byters.bcphotoanimations.ApplicationStopMotion;
import org.byters.bcphotoanimations.controller.data.memorycache.ICacheProjects;
import org.byters.bcphotoanimations.controller.data.memorycache.ICacheStorage;
import org.byters.bcphotoanimations.view.ui.utils.AsyncTaskProjectExportListener;
import org.jcodec.api.android.AndroidSequenceEncoder;
import org.jcodec.common.io.FileChannelWrapper;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.Rational;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

import javax.inject.Inject;

public class AsyncTaskExportMP4_JCodec extends AsyncTask<Void, Integer, Boolean> {

    private static final String EXT_MP4 = ".mp4";

    private final String projectId;
    private final WeakReference<AsyncTaskProjectExportListener> refListener;
    private final int fps, w, h;

    @Inject
    ICacheStorage cacheStorage;

    @Inject
    ICacheProjects cacheProjects;

    public AsyncTaskExportMP4_JCodec(String projectId, int fps, int w, int h, AsyncTaskProjectExportListener listener) {
        this.projectId = projectId;
        this.fps = fps;
        this.w = w;
        this.h = h;
        this.refListener = new WeakReference<>(listener);
        ApplicationStopMotion.getComponent().inject(this);
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        int num = cacheProjects.getItemFramesNum(projectId);

        File file = new File(cacheStorage.getProjectOutputFolder(cacheProjects.getItemTitleById(projectId) + EXT_MP4));

        FileChannelWrapper out = null;
        try {

            out = NIOUtils.writableFileChannel(file.getAbsolutePath());

            AndroidSequenceEncoder encoder = new AndroidSequenceEncoder(out, Rational.R(fps, 1));

            for (int i = 0; i < num; ++i) {
                int success = 1;
                String fileUrl = cacheProjects.getFrameUrl(projectId, i);
                Bitmap bmp = BitmapFactory.decodeFile(fileUrl);

                if (bmp == null)
                    success = 0;
                else {
                    bmp = bmpScale(bmp, w, h);
                    bmp = bmpOrientation(bmp, fileUrl);
                    encoder.encodeImage(bmp);
                    bmp.recycle();

                }

                onProgressUpdate(num, i, success);

            }
            encoder.finish();


        } catch (IOException | IllegalArgumentException e) {
            return false;
        } finally {
            NIOUtils.closeQuietly(out);
        }

        return true;
    }

    private Bitmap bmpScale(Bitmap bmp, int w, int h) {
        return Bitmap.createScaledBitmap(bmp, w, h, true);
    }

    private Bitmap bmpOrientation(Bitmap bmp, String fileUrl) {

        ExifInterface exif;
        try {
            exif = new ExifInterface(fileUrl);
        } catch (IOException e) {
            return bmp;
        }

        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        Matrix matrix = new Matrix();

        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
            matrix.postRotate(90);
        } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
            matrix.postRotate(180);
        } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
            matrix.postRotate(270);
        } else return bmp;

        return Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        if (refListener == null
                || refListener.get() == null) return;
        refListener.get().onUpdate(projectId, values);
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);

        if (refListener == null || refListener.get() == null) return;
        refListener.get().onComplete(projectId);
    }
}
