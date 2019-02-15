package org.byters.bcphotoanimations.view.presenter;

import org.byters.bcphotoanimations.view.presenter.callback.IPresenterAdapterProjectsCallback;

import javax.inject.Singleton;

@Singleton
public interface IPresenterAdapterProjects {
    int getItemsSize();

    boolean isTypeProject(int viewType);

    boolean isTypeProjectNew(int viewType);

    void setCallback(IPresenterAdapterProjectsCallback callback);

    int getItemViewType(int position);

    void onClickLong(int position);

    void onClickItem(int position);

    String getItemTitle(int position);

    void onClickSettings(int position);
}
