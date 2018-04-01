package org.byters.bcphotoanimations.view.presenter.callback;


public interface IPresenterPreviewCallback {
    void setButtonPlayVisibility(boolean isVisible);

    void setImage(String path);

    void setFPS(int fps);
}
