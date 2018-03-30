package org.byters.bcphotoanimations.view.ui.view;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

import org.byters.bcphotoanimations.view.ui.view.callback.ICameraPreviewCallback;

import java.io.IOException;
import java.lang.ref.WeakReference;

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
        } catch (IOException e) {

        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {

        if (this.holder.getSurface() == null)
            return;

        try {
            camera.stopPreview();
        } catch (Exception e) {
        }

        try {
            int rotation = checkCameraOrientation();
            checkPreviewSize(rotation);
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

    private void checkPreviewSize(int rotation) {
        if (refCallback == null || refCallback.get() == null)
            return;

        Point displaySize = new Point();
        refCallback.get().getDisplaySize(displaySize);

        if (displaySize.x == 0 || displaySize.y == 0) return;

        int maxPreviewWidth = Math.max(displaySize.x, displaySize.y);
        int maxPreviewHeight = Math.min(displaySize.x, displaySize.y);

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

        if (parameters.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_MACRO))
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_MACRO);

        parameters.setRotation(rotation);

        camera.setParameters(parameters);

        ViewGroup.LayoutParams params = this.getLayoutParams();
        params.width = displaySize.x > displaySize.y ? Math.max(bestSize.width, bestSize.height) : Math.min(bestSize.width, bestSize.height);
        params.height = displaySize.x > displaySize.y ? Math.min(bestSize.width, bestSize.height) : Math.max(bestSize.width, bestSize.height);
        this.setLayoutParams(params);

    }
}