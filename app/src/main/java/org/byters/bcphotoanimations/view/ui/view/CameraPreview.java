package org.byters.bcphotoanimations.view.ui.view;

import android.content.Context;
import android.hardware.Camera;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import org.byters.bcphotoanimations.ApplicationStopMotion;
import org.byters.bcphotoanimations.controller.data.memorycache.ICacheInterfaceState;
import org.byters.bcphotoanimations.model.CameraSize;
import org.byters.bcphotoanimations.view.ui.utils.CameraSizeComparator;
import org.byters.bcphotoanimations.view.ui.utils.CameraUtils;
import org.byters.bcphotoanimations.view.ui.view.callback.ICameraPreviewCallback;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
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

    private static List<CameraSize> getCameraSizes(android.hardware.Camera camera, Camera.Parameters parameters) {
        List<CameraSize> result = new ArrayList<>();

        List<android.hardware.Camera.Size> pictureSizes = parameters.getSupportedPictureSizes();
        for (Camera.Size size : pictureSizes) {
            CameraSize item = new CameraSize();
            item.pictureSize = size;
            item.previewSize = CameraUtils.getPreviewSize(size, camera.getParameters().getSupportedPreviewSizes());
            if (item.previewSize != null) result.add(item);
        }

        return result;
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
        int degrees = CameraUtils.getDegrees(rotation); //0..360

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

        List<CameraSize> sizes = getCameraSizes(camera, parameters);
        Collections.sort(sizes, new CameraSizeComparator());
        Collections.reverse(sizes);

        CameraSize size = sizes.get(0); //todo check sizes length>0

        parameters.setPictureSize(size.pictureSize.width, size.pictureSize.height);
        parameters.setPreviewSize(size.previewSize.width, size.previewSize.height);
        parameters.setFlashMode(cacheInterfaceState.isFlashEnabled() ? "on" : "off");
        setFocusMode(parameters, Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);

        camera.setParameters(parameters);

        FrameLayout.LayoutParams params = setViewSize(w, h, size, rotation);

        notifySize(params.width, params.height);
        notifyFlashModes(parameters.getSupportedFlashModes());
        notifyPictureSize(size.previewSize, size.pictureSize);
    }

    @NotNull
    private FrameLayout.LayoutParams setViewSize(int w, int h, CameraSize size, int rotation) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) this.getLayoutParams();

        boolean isRotated = (rotation / 90) % 2 == 1;

        int previewWidth = (isRotated ? size.previewSize.height : size.previewSize.width);
        int previewHeight = (isRotated ? size.previewSize.width : size.previewSize.height);

        float factor = Math.min(w / (float) previewWidth, h / (float) previewHeight);

        params.width = (int) (previewWidth * factor);
        params.height = (int) (previewHeight * factor);

        params.gravity = Gravity.CENTER;

        this.setLayoutParams(params);
        return params;
    }

    private void notifyFlashModes(List<String> supportedFlashModes) {
        if (refCallback == null || refCallback.get() == null) return;
        refCallback.get().onFlashModesGet(supportedFlashModes);
    }

    private void notifyPictureSize(Camera.Size previewSize, Camera.Size photoSize) {
        if (refCallback == null || refCallback.get() == null) return;
        refCallback.get().onGetPictureSize(previewSize, photoSize);
    }

    private void notifyOrientation(int rotation) {
        if (refCallback == null || refCallback.get() == null) return;
        refCallback.get().onGetOrientation(rotation);
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