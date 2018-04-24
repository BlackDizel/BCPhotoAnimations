package org.byters.bcphotoanimations.view;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import org.byters.bcphotoanimations.R;
import org.byters.bcphotoanimations.view.ui.fragment.FragmentCamera;
import org.byters.bcphotoanimations.view.ui.fragment.FragmentFrames;
import org.byters.bcphotoanimations.view.ui.fragment.FragmentPreview;
import org.byters.bcphotoanimations.view.ui.fragment.FragmentProjectEdit;
import org.byters.bcphotoanimations.view.ui.fragment.FragmentProjects;

import java.lang.ref.WeakReference;

public class Navigator implements INavigator {

    public static final String TAG_PROJECT_EDIT = "TAG_PROJECT_EDIT";
    private WeakReference<FragmentManager> refFragmentManager;
    private int rootViewRes;

    @Override
    public void setData(FragmentManager fragmentManager, int flContent) {
        this.refFragmentManager = new WeakReference<>(fragmentManager);
        this.rootViewRes = flContent;
    }

    @Override
    public void navigateProjectsList() {
        if (refFragmentManager == null || refFragmentManager.get() == null) return;
        refFragmentManager.get()
                .beginTransaction()
                .replace(rootViewRes, new FragmentProjects())
                .commit();
    }

    @Override
    public void navigateProjectEdit() {
        if (refFragmentManager == null || refFragmentManager.get() == null) return;

        Fragment fragment = refFragmentManager.get().findFragmentByTag(TAG_PROJECT_EDIT);
        if (fragment != null) return;
        refFragmentManager.get()
                .beginTransaction()
                .add(rootViewRes, new FragmentProjectEdit(), TAG_PROJECT_EDIT)
                .commit();
    }


    @Override
    public void navigateProject() {
        if (refFragmentManager == null || refFragmentManager.get() == null) return;
        refFragmentManager.get()
                .beginTransaction()
                .replace(rootViewRes, new FragmentFrames())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void navigateCamera() {
        if (refFragmentManager == null || refFragmentManager.get() == null) return;
        refFragmentManager.get()
                .beginTransaction()
                .replace(rootViewRes, new FragmentCamera())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void navigatePlay() {
        if (refFragmentManager == null || refFragmentManager.get() == null) return;
        refFragmentManager.get()
                .beginTransaction()
                .replace(rootViewRes, new FragmentPreview())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void navigateProjectCreate() {
        navigateProjectEdit();
    }

    @Override
    public boolean closeProjectEdit() {
        if (refFragmentManager == null || refFragmentManager.get() == null) return false;

        Fragment fragment = refFragmentManager.get().findFragmentByTag(TAG_PROJECT_EDIT);
        if (fragment == null) return false;
        refFragmentManager.get()
                .beginTransaction()
                .remove(fragment)
                .commit();

        return true;
    }

    @Override
    public boolean onBackPressed() {
        if (closeProjectEdit()) return true;

        return false;
    }

    @Override
    public void chooseFolder(Activity activity, String folder) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse(folder);
        intent.setDataAndType(uri, "*/*");

        PackageManager packageManager = activity.getPackageManager();
        if (intent.resolveActivity(packageManager) == null) return;

        activity.startActivity(Intent.createChooser(intent, activity.getString(R.string.action_open_folder)));
    }
}
