package org.byters.bcphotoanimations.view.presenter;

import org.byters.bcphotoanimations.view.presenter.callback.IPresenterFramesCallback;

import javax.inject.Singleton;

@Singleton
public interface IPresenterFrames {
    void setCallback(IPresenterFramesCallback callback);

    void onResume();

    void onClickPlay();

    void onClickSelectCancel();

    void onClickSelectRange();

    void onClickRemove();

    void onClickCopy();

    void onClickMove();

    void onClickRevert();

    void onSelectRange(int from, int to);

    void onSelectCopyPosition(int position);
}
