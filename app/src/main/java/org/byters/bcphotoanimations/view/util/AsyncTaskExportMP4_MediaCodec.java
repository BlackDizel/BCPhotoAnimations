package org.byters.bcphotoanimations.view.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
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

    private static Bitmap bmpTransform(int screenWidth, int screenHeight, String fileUrl) {

        Bitmap background = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);

        Bitmap originalImage = BitmapFactory.decodeFile(fileUrl);

        int rotate = getRotate(fileUrl);

        float sourceWidth = isRotate(rotate) ? originalImage.getHeight() : originalImage.getWidth();
        float sourceHeight = isRotate(rotate) ? originalImage.getWidth() : originalImage.getHeight();

        float scale = Math.min(screenWidth / sourceWidth, screenHeight / sourceHeight); //100,100  -> 1000,2000

        float dX = (screenWidth - originalImage.getWidth()) / 2f;
        float dY = (screenHeight - originalImage.getHeight()) / 2f;

        Matrix transformation = new Matrix();
        transformation.postTranslate(dX, dY);
        transformation.postScale(scale, scale, screenWidth / 2f, screenHeight / 2f);
        transformation.postRotate(rotate, screenWidth / 2f, screenHeight / 2f);

        Paint paint = new Paint();
        paint.setFilterBitmap(true);

        new Canvas(background).drawBitmap(originalImage, transformation, paint);

        return background;
    }

    private static boolean isRotate(int rotate) {
        return rotate == 90 || rotate == 270;
    }

    private static int getRotate(String fileUrl) {
        ExifInterface exif;
        try {
            exif = new ExifInterface(fileUrl);
        } catch (IOException e) {
            return 0;
        }

        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        return orientation == ExifInterface.ORIENTATION_ROTATE_90 ? 90
                : orientation == ExifInterface.ORIENTATION_ROTATE_180 ? 180
                : orientation == ExifInterface.ORIENTATION_ROTATE_270 ? 270
                : 0;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        int num = cacheProjects.getItemFramesNum(projectId);

        MP4Encoder encoder = new MP4Encoder();
        encoder.setOutputFilePath(getFilePath());
        encoder.setOutputSize(w, h);
        encoder.setFrameRate(fps);
        encoder.setEncodeFinishListener(new ListenerFinish());
        encoder.setEncodeFrameListener(new ListenerFrame(num));

        encoder.startEncode();

        for (int i = 0; i < num; ++i) {
            String fileUrl = cacheProjects.getFrameUrl(projectId, i);
            Bitmap bmp = bmpTransform(w, h, fileUrl);
            encoder.addFrame(bmp);
        }
        encoder.notifyLastFrameAdded();
        return true;
    }

    private String getFilePath() {
        return cacheStorage.getProjectOutputFolder(cacheProjects.getItemTitleById(projectId) + cacheStorage.getVideoExt());
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
            refListener.get().onComplete(projectId, getFilePath());
        }
    }
}
