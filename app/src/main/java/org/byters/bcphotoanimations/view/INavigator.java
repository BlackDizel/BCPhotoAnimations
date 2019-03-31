package org.byters.bcphotoanimations.view;

import android.support.v4.app.FragmentManager;

import org.byters.bcphotoanimations.view.ui.activity.ActivityBase;

import javax.inject.Singleton;

@Singleton
public interface INavigator {

    void setData(ActivityBase activity, FragmentManager fragmentManager, int flContent);

    void navigateProjectsList();

    void navigateProjectEdit();

    void navigateAbout();

    void navigateProjectCreate();

    boolean closeProjectEdit();

    boolean onBackPressed();

    void navigateProject();

    void navigateCamera();

    void navigatePlay();

    void chooseFolder(String projectOutputFolder);

    void startExportServiceImages(String projectSelectedId);

    void startExportServiceMJPEG(String projectSelectedId, int w, int h, int fps);

    void navigateFeedback();

    void navigateGooglePlay();
}
