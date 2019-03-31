package org.byters.bcphotoanimations.view.presenter;

import org.byters.bcphotoanimations.ApplicationStopMotion;
import org.byters.bcphotoanimations.R;
import org.byters.bcphotoanimations.controller.data.memorycache.ICacheProjectSelected;
import org.byters.bcphotoanimations.view.INavigator;
import org.byters.bcphotoanimations.view.util.IHelperPopup;

import javax.inject.Inject;

public class PresenterProjectExportJCodec implements IPresenterProjectExportJCodec {

    @Inject
    ICacheProjectSelected cacheProjectSelected;

    @Inject
    INavigator navigator;

    @Inject
    IHelperPopup viewPopup;

    public PresenterProjectExportJCodec() {
        ApplicationStopMotion.getComponent().inject(this);
    }

    @Override
    public void onClickExport(String sFps, String sWidth, String sHeight) {

        int fps = 0;
        int width = 0;
        int height = 0;

        try {
            fps = Integer.valueOf(sFps);
            width = Integer.valueOf(sWidth);
            height = Integer.valueOf(sHeight);
        } catch (NumberFormatException e) {

        }

        if (fps <= 0 || width <= 0 || height <= 0) {
            viewPopup.showMessage(R.string.error_export_mjpeg_meta);
            return;
        }

        navigator.startExportServiceMJPEG(cacheProjectSelected.getProjectSelectedId(), width, height, fps);
    }
}
