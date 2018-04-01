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
}
