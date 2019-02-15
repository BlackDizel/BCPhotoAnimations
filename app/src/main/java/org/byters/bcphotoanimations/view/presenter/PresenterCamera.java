package org.byters.bcphotoanimations.view.presenter;

import android.hardware.Camera;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;

import org.byters.bcphotoanimations.ApplicationStopMotion;
import org.byters.bcphotoanimations.R;
import org.byters.bcphotoanimations.controller.data.memorycache.ICacheProjects;
import org.byters.bcphotoanimations.view.ui.view.CameraPreview;
import org.byters.bcphotoanimations.view.ui.view.callback.ICameraPreviewCallback;

import java.util.List;

import javax.inject.Inject;

public class PresenterCamera extends PresenterCameraBase {
    private static final int CAMERA_UNDEFINED = -1;
    private static final String CAMERA_FLASH_ON = "on";
    private static final String CAMERA_FLASH_OFF = "off";

    @Inject
    ICacheProjects cacheProjects;

    @Nullable
    private Camera camera;

    private CameraCallback cameraCallback;
    private Camera.PictureCallback pictureCallback;

    public PresenterCamera() {
        pictureCallback = new CameraPictureCallback();

        ApplicationStopMotion.getComponent().inject(this);
    }

    private Camera getCameraInstance(int cameraId) {
        if (cameraId == CAMERA_UNDEFINED) return null;

        Camera c = null;
        try {
            c = Camera.open(cameraId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }

    @Override
    public void onClickFlash() {
        super.onClickFlash();
        if (camera != null) {
            Camera.Parameters parameters = camera.getParameters();
            parameters.setFlashMode(cacheInterfaceState.isFlashEnabled() ? CAMERA_FLASH_ON : CAMERA_FLASH_OFF);
            camera.setParameters(parameters);
        }
    }

    private int getCameraId() {
        int numberOfCameras = Camera.getNumberOfCameras();
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                return i;
            }
        }
        return CAMERA_UNDEFINED;
    }

    @Override
    public void onResume(View view) {
        setShowLastFrame();
        initCamera(view);
    }

    @Override
    public void onPause() {
        cacheProjects.storeCache();

        if (camera == null) return;
        camera.release();
        camera = null;
    }

    @Override
    public void takePicture() {
        if (camera == null) return;
        try {
            camera.takePicture(null, null, pictureCallback);
            showFlash();
        } catch (RuntimeException e) {
        }
    }

    private void initCamera(View view) {
        if (view == null) return;

        if (!isPermissionExist(view.getContext())) {
            requestCameraPermission();
            return;
        }

        FrameLayout preview = view.findViewById(R.id.flContent);
        preview.removeAllViews();

        int cameraId = getCameraId();
        camera = getCameraInstance(cameraId);
        if (camera == null) return;

        CameraPreview cameraPreview = new CameraPreview(view.getContext(), camera, cameraId);
        cameraPreview.setCallback(cameraCallback = new CameraCallback());
        preview.addView(cameraPreview);
    }

    @Override
    void onPermissionGranted(View view) {
        initCamera(view);
    }

    private class CameraPictureCallback implements Camera.PictureCallback {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            cacheProjectSelected.addFrame(data);
            setShowLastFrame();

            try {
                camera.startPreview();
            } catch (RuntimeException e) {
            }
        }
    }

    private class CameraCallback implements ICameraPreviewCallback {
        @Override
        public int requestRotation() {
            return refCallback != null && refCallback.get() != null ? refCallback.get().getScreenRotation() : 0;
        }

        @Override
        public void onSizeChanged(int width, int height) {
            if (refCallback == null || refCallback.get() == null) return;
            refCallback.get().setLastFrameSize(width, height);
        }

        @Override
        public void onFlashModesGet(List<String> supportedFlashModes) {
            if (refCallback == null || refCallback.get() == null) return;

            boolean isFlashSupported = supportedFlashModes != null
                    && supportedFlashModes.contains(CAMERA_FLASH_ON)
                    && supportedFlashModes.contains(CAMERA_FLASH_OFF);

            refCallback.get().setFlashVisible(isFlashSupported);
        }

        @Override
        public void onGetPictureSize(int width, int height) {
            if (refCallback == null || refCallback.get() == null) return;
            refCallback.get().setPictureSize(width, height);
        }
    }
}
