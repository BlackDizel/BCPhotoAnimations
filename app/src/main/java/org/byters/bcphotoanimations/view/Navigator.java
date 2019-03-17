package org.byters.bcphotoanimations.view;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import org.byters.bcphotoanimations.R;
import org.byters.bcphotoanimations.view.ui.activity.ActivityBase;
import org.byters.bcphotoanimations.view.ui.fragment.FragmentAbout;
import org.byters.bcphotoanimations.view.ui.fragment.FragmentCamera;
import org.byters.bcphotoanimations.view.ui.fragment.FragmentFrames;
import org.byters.bcphotoanimations.view.ui.fragment.FragmentPreview;
import org.byters.bcphotoanimations.view.ui.fragment.FragmentProjectEdit;
import org.byters.bcphotoanimations.view.ui.fragment.FragmentProjects;
import org.byters.bcphotoanimations.view.ui.service.ServiceProjectExport;

import java.lang.ref.WeakReference;

public class Navigator implements INavigator {

    public static final String TAG_PROJECT_EDIT = "TAG_PROJECT_EDIT";
    private WeakReference<FragmentManager> refFragmentManager;
    private int rootViewRes;
    private WeakReference<ActivityBase> refActivityBase;

    @Override
    public void setData(ActivityBase activityBase, FragmentManager fragmentManager, int flContent) {
        this.refFragmentManager = new WeakReference<>(fragmentManager);
        this.refActivityBase = new WeakReference<>(activityBase);
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
    public void navigateAbout() {
        if (refFragmentManager == null || refFragmentManager.get() == null) return;
        refFragmentManager.get()
                .beginTransaction()
                .replace(rootViewRes, new FragmentAbout())
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

    @Override
    public void startExportService(Context context, String projectSelectedId) {
        ServiceProjectExport.start(context, projectSelectedId);
    }

    @Override
    public void navigateFeedback(Context context) {
        if (context == null) return;
        Intent intentSend = getIntentSendEmail(
                context.getString(R.string.email_support)
                , context.getString(R.string.email_support_title)
                , context.getString(R.string.email_support_message));

        if (intentSend.resolveActivity(context.getPackageManager()) == null) {
            Toast.makeText(context, R.string.email_app_error_no_found, Toast.LENGTH_SHORT).show();
            return;
        }
        context.startActivity(intentSend);
    }

    @Override
    public void navigateGooglePlay() {
        if (refActivityBase == null || refActivityBase.get() == null) return;
        ActivityBase activityBase = refActivityBase.get();
        Uri uri = Uri.parse("market://details?id=" + activityBase.getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);


        if (myAppLinkToMarket.resolveActivity(activityBase.getPackageManager()) != null)
            activityBase.startActivity(myAppLinkToMarket);
    }

    @NonNull
    private Intent getIntentSendEmail(String email_url, String title, String body) {

        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(email_url));
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        intent.putExtra(Intent.EXTRA_TEXT, body);

        return intent;
    }

}
