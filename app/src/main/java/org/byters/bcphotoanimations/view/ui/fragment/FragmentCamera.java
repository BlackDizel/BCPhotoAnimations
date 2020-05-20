package org.byters.bcphotoanimations.view.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.byters.bcphotoanimations.ApplicationStopMotion;
import org.byters.bcphotoanimations.R;
import org.byters.bcphotoanimations.view.presenter.IPresenterCamera;
import org.byters.bcphotoanimations.view.presenter.callback.IPresenterCameraCallback;

import javax.inject.Inject;

public class FragmentCamera extends FragmentBase implements View.OnClickListener {

    @Inject
    IPresenterCamera presenter;

    private IPresenterCameraCallback presenterCallback;

    private ImageView ivLastFrame, ivLastFrame2;
    private ImageView ivFlash, ivGrid, ivFramesOverlay, ivCameraType;
    private TextView tvCameraPictureSize;
    private View vGrid;
    private View flSettings;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ApplicationStopMotion.getComponent().inject(this);
        presenter.setCallback(presenterCallback = new PresenterCallback());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        ivFramesOverlay = view.findViewById(R.id.ivLastFrameShow);
        ivFlash = view.findViewById(R.id.ivFlash);
        tvCameraPictureSize = view.findViewById(R.id.tvCameraPictureSize);
        ivGrid = view.findViewById(R.id.ivGrid);
        ivCameraType = view.findViewById(R.id.ivCameraType);

        ivLastFrame = view.findViewById(R.id.ivLastFrame);
        ivLastFrame2 = view.findViewById(R.id.ivLastFrame2);

        vGrid = view.findViewById(R.id.flGrid);

        flSettings = view.findViewById(R.id.flSettings);

        presenter.onCreateView(view);

        return view;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {

        view.findViewById(R.id.ivCapture).setOnClickListener(this);
        view.findViewById(R.id.ivSettings).setOnClickListener(this);
        flSettings.setOnClickListener(this);

        ivFramesOverlay.setOnClickListener(this);
        ivFlash.setOnClickListener(this);
        ivGrid.setOnClickListener(this);
        ivCameraType.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter.onActivityCreated(getActivity());
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ivSettings
                || view.getId() == R.id.flSettings)
            presenter.onClickSettings();
        if (view.getId() == R.id.ivCapture)
            presenter.takePicture();
        if (view.getId() == R.id.ivLastFrameShow)
            presenter.onClickLastFrameShow();
        if (view.getId() == R.id.ivFlash)
            presenter.onClickFlash();
        if (view.getId() == R.id.ivGrid)
            presenter.onClickGrid();
        if (view.getId() == R.id.ivCameraType)
            presenter.onClickCameraType(getView());
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume(getView());
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (!presenter.onRequestPermissionsResult(getView(), requestCode, permissions, grantResults))
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private class PresenterCallback implements IPresenterCameraCallback {
        @Override
        public void requestPermissionCamera(String[] permissions, int requestCode) {
            if (!isAdded()) return;
            requestPermissions(permissions, requestCode);
        }

        @Override
        public void finishView() {
            if (!isAdded()) return;
            getActivity().onBackPressed();
        }

        @Override
        public int getScreenRotation() {
            return getActivity() == null ? Surface.ROTATION_0 : getActivity().getWindowManager().getDefaultDisplay().getRotation();
        }

        @Override
        public void showFlash() {
            if (!isAdded()) return;
            View view = getView().findViewById(R.id.vFlash);
            AlphaAnimation animation = new AlphaAnimation(1, 0);
            animation.setDuration(250);
            animation.setFillAfter(true);
            view.setVisibility(View.VISIBLE);
            view.startAnimation(animation);
        }

        @Override
        public void showLastFrame(String path, boolean isFace) {
            imageInto(ivLastFrame, path, isFace);
        }

        @Override
        public void showLastFrame2(String path, boolean isFace) {
            imageInto(ivLastFrame2, path, isFace);
        }

        private void imageInto(ImageView view, String path, boolean isFace) {
            if (!isAdded()) return;

            view.setVisibility(TextUtils.isEmpty(path) ? View.GONE : View.VISIBLE);

            if (TextUtils.isEmpty(path))
                view.setImageDrawable(null);

            else {
                Glide.with(getContext())
                        .load(path)
                        .into(view);

                view.setScaleX(isFace ? -1 : 1);
            }

        }

        @Override
        public void setLastFrameShowDrawable(int drawableRes) {
            if (!isAdded()) return;
            ivFramesOverlay.setImageResource(drawableRes);
        }

        @Override
        public void setLastFrameSize(int width, int height) {
            if (!isAdded()) return;
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) ivLastFrame.getLayoutParams();
            params.width = width;
            params.height = height;
            ivLastFrame.setLayoutParams(params);
            ivLastFrame2.setLayoutParams(params);
            vGrid.setLayoutParams(params);
        }

        @Override
        public void setFlashVisible(boolean isFlashSupported) {
            if (!isAdded()) return;
            ivFlash.setVisibility(isFlashSupported ? View.VISIBLE : View.GONE);
        }

        @Override
        public void setButtonFlashImage(int drawableRes) {
            if (!isAdded()) return;
            ivFlash.setImageResource(drawableRes);
        }

        @Override
        public void showPictureSize(String message) {
            if (!isAdded()) return;
            tvCameraPictureSize.setText(message);
        }

        @Override
        public void setButtonGridImage(int drawableRes) {
            ivGrid.setImageResource(drawableRes);
        }

        @Override
        public void setButtonGridVisible(boolean gridEnabled) {
            vGrid.setVisibility(gridEnabled ? View.VISIBLE : View.GONE);
        }

        @Override
        public void setSettingsVisibility(boolean settingsVisible) {
            flSettings.setVisibility(settingsVisible ? View.VISIBLE : View.GONE);
        }
    }

}
