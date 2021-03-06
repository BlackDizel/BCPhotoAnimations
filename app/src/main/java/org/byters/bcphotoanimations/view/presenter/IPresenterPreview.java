package org.byters.bcphotoanimations.view.presenter;

import org.byters.bcphotoanimations.view.presenter.callback.IPresenterPreviewCallback;

public interface IPresenterPreview {
    void selCallback(IPresenterPreviewCallback callback);

    void onCreateView();

    void onClickFrame();

    void onClickPlay();

    void onStart();

    void onStop();

    void onClickFPS();

    void onClickFrameRange();

    void onTrackingStart();

    void onTrackingStop();

    void onFrameSelected(int position);

    void onSelectRange(int value1, int value2);
}
