package org.byters.bcphotoanimations.view;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import javax.inject.Singleton;

@Singleton
public interface INavigator {

    void setData(FragmentManager fragmentManager, int flContent);

    void navigateProjectsList();

    void navigateProjectEdit();

    void navigateProjectCreate();

    boolean closeProjectEdit();

    boolean onBackPressed();

    void navigateProject();

    void navigateCamera();

    void navigatePlay();

    void chooseFolder(Activity activity, String projectOutputFolder);
}
