package org.byters.bcphotoanimations.view.presenter;


import android.support.v4.app.FragmentActivity;
import android.view.View;

import org.byters.bcphotoanimations.view.presenter.callback.IPresenterCameraCallback;

import javax.inject.Singleton;

@Singleton
public interface IPresenterCamera {
    void onActivityCreated(FragmentActivity activity);

    void onResume(View context);

    void onPause();

    boolean onRequestPermissionsResult(View view, int requestCode, String[] permissions, int[] grantResults);

    void takePicture();

    void setCallback(IPresenterCameraCallback callback);

    void onCreateView(View view);

    void onClickSettings();

    void onClickLastFrameShow();

    void onClickFlash();
}
