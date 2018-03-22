package org.byters.bcphotoanimations.view.presenter;

import org.byters.bcphotoanimations.view.presenter.callback.IPresenterAdapterFramesCallback;

import javax.inject.Singleton;

@Singleton
public interface IPresenterAdapterFrames {
    boolean isTypeFrame(int viewType);

    boolean isTypeFrameAdd(int viewType);

    void onClickFrameAdd();

    int getItemsNum();

    String getItemImageUri(int position);

    int getItemViewType(int position);

    void onClickItem(int position);

    void onLongClickItem(int position);

    void setCallback(IPresenterAdapterFramesCallback callback);

    boolean isSelected(int position);
}
