package org.byters.bcphotoanimations.view.presenter;

import org.byters.bcphotoanimations.view.presenter.callback.IPresenterProjectEditCallback;

public interface IPresenterProjectEdit {
    void onClickClose();

    void onClickSave();

    void onClickRoot();

    void onTitleEdit(String title);

    void onStart();

    void setCallback(IPresenterProjectEditCallback callback);
}
