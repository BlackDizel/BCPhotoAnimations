package org.byters.bcphotoanimations.view.ui.view;

import android.content.Context;
import android.hardware.Camera;
import android.view.Gravity;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import org.byters.bcphotoanimations.ApplicationStopMotion;
import org.byters.bcphotoanimations.controller.data.memorycache.ICacheInterfaceState;
import org.byters.bcphotoanimations.view.ui.view.callback.ICameraPreviewCallback;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

import javax.inject.Inject;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    @Inject
    ICacheInterfaceState cacheInterfaceState;

    private int cameraId;
    private SurfaceHolder holder;
    private Camera camera;
    private WeakReference<ICameraPreviewCallback> refCallback;

    public CameraPreview(Context context, Camera camera, int cameraId) {
        super(context);
        ApplicationStopMotion.getComponent().inject(this);

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
        updateView(w, h);
    }

    private void updateView(int w, int h) {
        if (this.holder.getSurface() == null)
            return;

        try {
            camera.stopPreview();
            int rotation = checkCameraOrientation();
            checkPreviewSize(rotation, w, h);
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

        int result = info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT
                ? (360 - ((info.orientation + degrees) % 360)) % 360
                : (info.orientation - degrees + 360) % 360;

        camera.setDisplayOrientation(result);

        notifyOrientation(info.orientation);

        return result;
    }

    private void checkPreviewSize(int rotation, int w, int h) {
        if (refCallback == null || refCallback.get() == null)
            return;

        if (w == 0 || h == 0) return;

        Camera.Parameters parameters = camera.getParameters();

        Camera.Size pictureSize = getPictureSize(parameters); //Picture size and preview size must have equal aspect ratio
        Camera.Size previewSize = getPreviewSize(pictureSize, w, h);

        parameters.setPreviewSize(previewSize.width, previewSize.height);

        setFocusMode(parameters, Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        parameters.setPictureSize(pictureSize.width, pictureSize.height);
        parameters.setFlashMode(cacheInterfaceState.isFlashEnabled() ? "on" : "off");

        camera.setParameters(parameters);

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) this.getLayoutParams();
        params.width = w > h ? Math.max(previewSize.width, previewSize.height) : Math.min(previewSize.width, previewSize.height);
        params.height = w > h ? Math.min(previewSize.width, previewSize.height) : Math.max(previewSize.width, previewSize.height);
        params.gravity = Gravity.CENTER;
        this.setLayoutParams(params);

        notifySize(params.width, params.height);
        notifyFlashModes(parameters.getSupportedFlashModes());
        notifyPictureSize(previewSize, pictureSize);
    }

    private Camera.Size getPreviewSize(Camera.Size pictureSize, int w, int h) {

        int maxDisplayDimension = Math.max(w, h);
        int minDisplayDimension = Math.min(w, h);

        Camera.Size result = null;
        for (Camera.Size item : camera.getParameters().getSupportedPreviewSizes()) {

            int maxPreviewDimension = Math.max(item.width, item.height);
            int minPreviewDimension = Math.min(item.width, item.height);

            if (maxPreviewDimension > maxDisplayDimension || minPreviewDimension > minDisplayDimension)
                continue;

            if (ratioIsEqual(item, pictureSize)
                    && (result == null || isAreaMore(item, result)))
                result = item;
        }
        return result;
    }

    private boolean isAreaMore(Camera.Size one, Camera.Size other) {
        return one == null || other == null || one.height * one.width > other.height * other.width;
    }

    private void notifyFlashModes(List<String> supportedFlashModes) {
        if (refCallback == null || refCallback.get() == null) return;
        refCallback.get().onFlashModesGet(supportedFlashModes);
    }

    private Camera.Size getPictureSize(Camera.Parameters parameters) {
        if (parameters == null) return null;
        List<android.hardware.Camera.Size> sizes = parameters.getSupportedPictureSizes();
        if (sizes == null) return null;

        Camera.Size currentSize = null;

        for (Camera.Size size : sizes) {
            if (currentSize == null) {
                currentSize = size;
                continue;
            }

            if (size.width > currentSize.width) currentSize = size;
        }

        if (currentSize == null) return null;

        return currentSize;
    }

    private void notifyPictureSize(Camera.Size previewSize, Camera.Size photoSize) {
        if (refCallback == null || refCallback.get() == null)
            return;
        refCallback.get().onGetPictureSize(previewSize, photoSize);
    }

    private void notifyOrientation(int rotation) {
        if (refCallback == null || refCallback.get() == null)
            return;
        refCallback.get().onGetOrientation(rotation);
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