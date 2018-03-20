package org.byters.bcphotoanimations.view.ui.fragment;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;

import org.byters.bcphotoanimations.ApplicationStopMotion;
import org.byters.bcphotoanimations.R;
import org.byters.bcphotoanimations.view.presenter.IPresenterCamera;
import org.byters.bcphotoanimations.view.presenter.callback.IPresenterCameraCallback;

import javax.inject.Inject;

public class FragmentCamera extends FragmentBase implements View.OnClickListener {

    //todo show frame num
    //todo show prev frame transparent
    //todo show camera settings (frame size, flash)

    @Inject
    IPresenterCamera presenterCamera;

    private IPresenterCameraCallback presenterCallback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ApplicationStopMotion.getComponent().inject(this);

        presenterCallback = new PresenterCallback();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        presenterCamera.onCreateView(view);

        return view;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        view.findViewById(R.id.ivCapture).setOnClickListener(this);
        view.findViewById(R.id.ivSettings).setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenterCamera.onActivityCreated(getActivity());
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ivCapture)
            presenterCamera.takePicture();
        if (view.getId() == R.id.ivSettings)
            presenterCamera.onClickSettings();
    }

    @Override
    public void onStart() {
        super.onStart();
        presenterCamera.setCallback(presenterCallback);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenterCamera.onResume(getView());
    }

    @Override
    public void onPause() {
        super.onPause();
        presenterCamera.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (!presenterCamera.onRequestPermissionsResult(getView(), requestCode, permissions, grantResults))
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private class PresenterCallback implements IPresenterCameraCallback {
        @Override
        public void requestPermissionCamera(String[] permissions, int requestCode) {
            requestPermissions(permissions, requestCode);
        }

        @Override
        public void finishView() {
            if (getActivity() == null) return;
            getActivity().onBackPressed();
        }

        @Override
        public int getScreenRotation() {
            return getActivity() == null ? Surface.ROTATION_0 : getActivity().getWindowManager().getDefaultDisplay().getRotation();
        }

        @Override
        public int getScreenOrientation() {
            return getResources().getConfiguration().orientation;
        }

        @Override
        public void getDisplaySize(Point displaySize) {
            if (getActivity() == null) return;
            getActivity().getWindowManager().getDefaultDisplay().getSize(displaySize);
        }
    }

}
