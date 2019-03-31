package org.byters.bcphotoanimations.view.presenter;

import org.byters.bcphotoanimations.view.presenter.callback.IPresenterProjectCreateListener;

public interface IPresenterProjectCreate {
    void onClickContinue(String s);

    void setListener(IPresenterProjectCreateListener listener);
}
