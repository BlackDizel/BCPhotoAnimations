package org.byters.bcphotoanimations.view.presenter.callback;


public interface IPresenterProjectEditCallback {
    void showErrorTitleEmpty();

    void setTitle(String title);

    void hideKeyboard();

    void showDialogProgressExport(String projectTitle);

    void setExportAttempts(int attempts);

    void setExportAttemptsUnlimited();

    void setExportAttemptsNotEnough();
}
