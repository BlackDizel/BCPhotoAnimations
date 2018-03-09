package org.byters.bcphotoanimations.view.presenter;

public interface IPresenterAdapterFrames {
    boolean isTypeFrame(int viewType);

    boolean isTypeFrameAdd(int viewType);

    void onClickFrameAdd();

    int getItemsNum();

    String getItemImageUri(int position);

    int getItemViewType(int position);
}
