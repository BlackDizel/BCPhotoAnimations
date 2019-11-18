package org.byters.bcphotoanimations.view;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import org.byters.bcphotoanimations.ApplicationStopMotion;
import org.byters.bcphotoanimations.R;
import org.byters.bcphotoanimations.view.ui.activity.ActivityBase;
import org.byters.bcphotoanimations.view.ui.fragment.FragmentAbout;
import org.byters.bcphotoanimations.view.ui.fragment.FragmentCamera;
import org.byters.bcphotoanimations.view.ui.fragment.FragmentFrames;
import org.byters.bcphotoanimations.view.ui.fragment.FragmentPreview;
import org.byters.bcphotoanimations.view.ui.fragment.FragmentProjectCreate;
import org.byters.bcphotoanimations.view.ui.fragment.FragmentProjectEdit;
import org.byters.bcphotoanimations.view.ui.fragment.FragmentProjects;
import org.byters.bcphotoanimations.view.ui.service.ServiceProjectExport;
import org.byters.bcphotoanimations.view.util.IHelperDialog;
import org.byters.bcphotoanimations.view.util.IHelperPopup;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

public class Navigator implements INavigator {

    private static final String TAG_PROJECT_EDIT = "TAG_PROJECT_EDIT";

    @Inject
    IHelperDialog helperDialog;

    @Inject
    IHelperPopup helperPopup;

    private WeakReference<FragmentManager> refFragmentManager;
    private int rootViewRes;
    private WeakReference<ActivityBase> refActivityBase;

    public Navigator() {
        ApplicationStopMotion.getComponent().inject(this);
    }

    @Override
    public void setData(ActivityBase activityBase, FragmentManager fragmentManager, int flContent) {
        this.refFragmentManager = new WeakReference<>(fragmentManager);
        this.refActivityBase = new WeakReference<>(activityBase);
        this.rootViewRes = flContent;
        helperDialog.set(activityBase);
        helperPopup.set(activityBase);
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
        if (refFragmentManager == null || refFragmentManager.get() == null) return;

        Fragment fragment = refFragmentManager.get().findFragmentByTag(TAG_PROJECT_EDIT);
        if (fragment != null) return;
        refFragmentManager.get()
                .beginTransaction()
                .add(rootViewRes, new FragmentProjectCreate(), TAG_PROJECT_EDIT)
                .commit();
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
    public void chooseFolder(String folder) {
        if (refActivityBase == null || refActivityBase.get() == null) return;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse(folder);
        intent.setDataAndType(uri, "*/*");

        PackageManager packageManager = refActivityBase.get().getPackageManager();
        if (intent.resolveActivity(packageManager) == null) return;

        refActivityBase.get().startActivity(Intent.createChooser(intent, refActivityBase.get().getString(R.string.action_open_folder)));
    }

    @Override
    public void startExportServiceImages(String projectSelectedId) {
        if (refActivityBase == null || refActivityBase.get() == null) return;
        ServiceProjectExport.startExportImages(refActivityBase.get(), projectSelectedId);
    }

    @Override
    public void startExportServiceMJPEG(String projectSelectedId, int w, int h, int fps) {
        if (refActivityBase == null || refActivityBase.get() == null) return;
        ServiceProjectExport.startExportMJPEG(refActivityBase.get(), projectSelectedId, w, h, fps);
    }

    @Override
    public void startExportServiceMediaCodec(String projectSelectedId, int width, int height, int fps) {
        if (refActivityBase == null || refActivityBase.get() == null) return;
        ServiceProjectExport.startExportMediaCodec(refActivityBase.get(), projectSelectedId, width, height, fps);
    }

    @Override
    public void navigateDiscord() {
        if (refActivityBase == null || refActivityBase.get() == null) return;
        ActivityBase activityBase = refActivityBase.get();
        Uri uri = Uri.parse("https://discord.gg/6V6RNHm");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        if (intent.resolveActivity(activityBase.getPackageManager()) != null)
            activityBase.startActivity(intent);
    }

    @Override
    public void navigateFeedback() {
        if (refActivityBase == null || refActivityBase.get() == null) return;
        Context context = refActivityBase.get();
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
