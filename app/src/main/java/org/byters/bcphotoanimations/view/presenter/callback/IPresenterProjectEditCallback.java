package org.byters.bcphotoanimations.view.presenter.callback;


public interface IPresenterProjectEditCallback {
    void showErrorTitleEmpty();

    void setTitle(String title);

    void hideKeyboard();

    void setProjectEditVisibility(boolean isVisible);

    void showDialogProgressExport(String projectTitle);

    void exportProject(String projectSelectedId);
}
