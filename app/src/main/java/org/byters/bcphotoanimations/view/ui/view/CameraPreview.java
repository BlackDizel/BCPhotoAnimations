package org.byters.bcphotoanimations.view.ui.view;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.view.Gravity;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import org.byters.bcphotoanimations.view.ui.view.callback.ICameraPreviewCallback;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private int cameraId;
    private SurfaceHolder holder;
    private Camera camera;

    private WeakReference<ICameraPreviewCallback> refCallback;

    public CameraPreview(Context context, Camera camera, int cameraId) {
        super(context);
        this.camera = camera;
        this.cameraId = cameraId;

        holder = getHolder();
        holder.addCallback(this);
    }

    public void setCallback(ICameraPreviewCallback callback) {
        this.refCallback = new WeakReference<>(callback);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (IOException | RuntimeException e) {

        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        updateView(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
    }

    private void updateView(String focusMode) {
        if (this.holder.getSurface() == null)
            return;

        try {
            camera.stopPreview();
            int rotation = checkCameraOrientation();
            checkPreviewSize(rotation, focusMode);
            camera.setPreviewDisplay(this.holder);
            camera.startPreview();

        } catch (Exception e) {

        }
    }

    private int checkCameraOrientation() {
        Camera.CameraInfo info = new Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);

        int rotation = refCallback != null && refCallback.get() != null ? refCallback.get().requestRotation() : 0;
        int degrees = 0;

        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        result = (info.orientation - degrees + 360) % 360;
        camera.setDisplayOrientation(result);
        return result;
    }

    private void checkPreviewSize(int rotation, String focusMode) {
        if (refCallback == null || refCallback.get() == null)
            return;

        int w = getResources().getDisplayMetrics().widthPixels;
        int h = getResources().getDisplayMetrics().heightPixels;
        if (w == 0 || h == 0) return;

        //FIXME на highscreen высота превью равна высоте экрана, ширина превью больше ширины экрана

        int maxPreviewWidth = Math.max(w, h);
        int maxPreviewHeight = Math.min(w, h);

        Camera.Size bestSize = null;
        for (Camera.Size item : camera.getParameters().getSupportedPreviewSizes()) {

            Point point = new Point(
                    Math.max(item.width, item.height),
                    Math.min(item.width, item.height));

            if (point.x > maxPreviewWidth || point.y > maxPreviewHeight)
                continue;

            if (bestSize == null
                    || point.y > Math.min(bestSize.width, bestSize.height)
                    || point.x > Math.max(bestSize.width, bestSize.height)) {
                bestSize = item;
            }
        }
        if (bestSize == null) return;

        Camera.Parameters parameters = camera.getParameters();
        parameters.setPreviewSize(bestSize.width, bestSize.height);
        setPictureSize(parameters); //Picture size and preview size must have equal aspect ratio

        setFocusMode(parameters, focusMode);

        parameters.setRotation(rotation);

        camera.setParameters(parameters);

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) this.getLayoutParams();
        params.width = w > h ? Math.max(bestSize.width, bestSize.height) : Math.min(bestSize.width, bestSize.height);
        params.height = w > h ? Math.min(bestSize.width, bestSize.height) : Math.max(bestSize.width, bestSize.height);
        params.gravity = Gravity.CENTER;
        this.setLayoutParams(params);
        notifySize(params.width, params.height);
    }

    private void setPictureSize(Camera.Parameters parameters) {
        if (parameters == null) return;
        List<android.hardware.Camera.Size> sizes = parameters.getSupportedPictureSizes();
        if (sizes == null) return;
        Camera.Size currentSize = null;
        for (Camera.Size size : sizes) {
            if (currentSize == null) {
                currentSize = size;
                continue;
            }

            if (ratioIsEqual(size, parameters.getPreviewSize()) && size.width > currentSize.width)
                currentSize = size;
        }

        parameters.setPictureSize(currentSize.width, currentSize.height);
    }

    private boolean ratioIsEqual(Camera.Size size, Camera.Size previewSize) {
        return Math.abs(size.height / (float) size.width - previewSize.height / (float) previewSize.width) < 0.000001;
    }

    private void notifySize(int width, int height) {
        if (refCallback == null || refCallback.get() == null) return;
        refCallback.get().onSizeChanged(width, height);
    }

    private void setFocusMode(Camera.Parameters parameters, String mode) {
        if (parameters.getSupportedFocusModes().contains(mode))
            parameters.setFocusMode(mode);
    }
}