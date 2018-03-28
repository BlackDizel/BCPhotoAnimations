package org.byters.bcphotoanimations.view.presenter.callback;


public interface IPresenterFramesCallback {
    void setSelectedMode(boolean isModeSelected);

    void showAlertSelectRange(int framesNum);

    void showAlertCopyToPosition(int framesNum);
}
