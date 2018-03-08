package org.byters.bcphotoanimations.view;

import android.support.v4.app.FragmentManager;

public interface INavigator {

    void setData(FragmentManager fragmentManager, int flContent);

    void navigateProjectsList();

    void navigateProjectEdit();

    void navigateProjectCreate();

    boolean closeProjectEdit();

    boolean onBackPressed();

    void navigateProject();
}
