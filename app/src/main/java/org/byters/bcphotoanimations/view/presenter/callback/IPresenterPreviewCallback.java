package org.byters.bcphotoanimations.view.presenter.callback;


public interface IPresenterPreviewCallback {
    void setButtonPlayVisibility(boolean isVisible);

    void setImage(String path);

    void setFPS(int fps);

    void setFrameCurrent(int frameRangeIndexFrom, int to, int current, boolean b);

    void setFramesRange(int from, int to);

    void showAlertFrameRange(int frameRangeFrom, int frameRangeTo, int projectSelectedFramesNum);
}
