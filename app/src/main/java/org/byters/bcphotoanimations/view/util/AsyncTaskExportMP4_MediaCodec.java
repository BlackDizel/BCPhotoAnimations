package org.byters.bcphotoanimations.view.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.media.ExifInterface;

import com.ctech.bitmp4.Encoder;
import com.ctech.bitmp4.MP4Encoder;

import org.byters.bcphotoanimations.ApplicationStopMotion;
import org.byters.bcphotoanimations.controller.data.memorycache.ICacheProjects;
import org.byters.bcphotoanimations.controller.data.memorycache.ICacheStorage;
import org.byters.bcphotoanimations.view.ui.utils.AsyncTaskProjectExportListener;

import java.io.IOException;
import java.lang.ref.WeakReference;

import javax.inject.Inject;

public final class AsyncTaskExportMP4_MediaCodec extends AsyncTask<Void, Integer, Boolean> {

    private static final String EXT_MP4 = ".mp4";

    private final String projectId;
    private final WeakReference<AsyncTaskProjectExportListener> refListener;
    private final int fps, w, h;
    private final Handler handler;

    @Inject
    ICacheStorage cacheStorage;

    @Inject
    ICacheProjects cacheProjects;

    public AsyncTaskExportMP4_MediaCodec(String projectId, int fps, int w, int h, AsyncTaskProjectExportListener listener) {
        this.projectId = projectId;
        this.fps = fps;
        this.w = w;
        this.h = h;
        this.refListener = new WeakReference<>(listener);
        ApplicationStopMotion.getComponent().inject(this);

        handler = new Handler();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        int num = cacheProjects.getItemFramesNum(projectId);

        MP4Encoder encoder = new MP4Encoder();
        encoder.setOutputFilePath(cacheStorage.getProjectOutputFolder(cacheProjects.getItemTitleById(projectId) + EXT_MP4));
        encoder.setOutputSize(w, h);
        encoder.setFrameRate(fps);
        encoder.setEncodeFinishListener(new ListenerFinish());
        encoder.setEncodeFrameListener(new ListenerFrame(num));

        encoder.startEncode();

        for (int i = 0; i < num; ++i) {
            String fileUrl = cacheProjects.getFrameUrl(projectId, i);
            Bitmap bmp = BitmapFactory.decodeFile(fileUrl);
            bmp = bmpScale(bmp, w, h);
            bmp = bmpOrientation(bmp, fileUrl);

            encoder.addFrame(bmp);
        }
        encoder.notifyLastFrameAdded();
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
    }

    private final class ListenerFinish implements Encoder.EncodeFinishListener {
        @Override
        public void onEncodeFinished() {
            handler.post(new RunnableSuccess());
        }
    }

    private final class ListenerFrame implements Encoder.EncodeFrameListener {
        private final int frames;

        ListenerFrame(int frames) {
            this.frames = frames;
        }

        @Override
        public void onFrameEncoded(int index) {
            onProgressUpdate(frames, index, 1);
        }
    }

    private class RunnableSuccess implements Runnable {
        @Override
        public void run() {
            if (refListener == null || refListener.get() == null) return;
            refListener.get().onComplete(projectId);
        }
    }
}
