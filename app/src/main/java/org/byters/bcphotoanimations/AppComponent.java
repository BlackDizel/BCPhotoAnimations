package org.byters.bcphotoanimations;

import org.byters.bcphotoanimations.controller.AppModule;
import org.byters.bcphotoanimations.controller.data.device.CachePreference;
import org.byters.bcphotoanimations.controller.data.memorycache.CacheExportAttempts;
import org.byters.bcphotoanimations.controller.data.memorycache.CacheInterfaceState;
import org.byters.bcphotoanimations.controller.data.memorycache.CacheProjectSelected;
import org.byters.bcphotoanimations.controller.data.memorycache.CacheProjects;
import org.byters.bcphotoanimations.controller.data.memorycache.CacheStorage;
import org.byters.bcphotoanimations.view.Navigator;
import org.byters.bcphotoanimations.view.presenter.PresenterAdapterFrames;
import org.byters.bcphotoanimations.view.presenter.PresenterAdapterProjects;
import org.byters.bcphotoanimations.view.presenter.PresenterCamera;
import org.byters.bcphotoanimations.view.presenter.PresenterFrames;
import org.byters.bcphotoanimations.view.presenter.PresenterPreview;
import org.byters.bcphotoanimations.view.presenter.PresenterProjectCreate;
import org.byters.bcphotoanimations.view.presenter.PresenterProjectEdit;
import org.byters.bcphotoanimations.view.presenter.PresenterProjectExportJCodec;
import org.byters.bcphotoanimations.view.presenter.PresenterProjectExportMediaCodec;
import org.byters.bcphotoanimations.view.ui.activity.ActivityMain;
import org.byters.bcphotoanimations.view.ui.adapters.AdapterFrames;
import org.byters.bcphotoanimations.view.ui.adapters.AdapterProjects;
import org.byters.bcphotoanimations.view.ui.dialog.DialogExportJCodec;
import org.byters.bcphotoanimations.view.ui.dialog.DialogExportMediaCodec;
import org.byters.bcphotoanimations.view.ui.fragment.FragmentAbout;
import org.byters.bcphotoanimations.view.ui.fragment.FragmentCamera;
import org.byters.bcphotoanimations.view.ui.fragment.FragmentFrames;
import org.byters.bcphotoanimations.view.ui.fragment.FragmentPreview;
import org.byters.bcphotoanimations.view.ui.fragment.FragmentProjectCreate;
import org.byters.bcphotoanimations.view.ui.fragment.FragmentProjectEdit;
import org.byters.bcphotoanimations.view.ui.fragment.FragmentProjects;
import org.byters.bcphotoanimations.view.ui.service.ServiceProjectExport;
import org.byters.bcphotoanimations.view.ui.utils.AsyncTaskProjectExport;
import org.byters.bcphotoanimations.view.ui.view.CameraPreview;
import org.byters.bcphotoanimations.view.util.AsyncTaskExportMP4_JCodec;
import org.byters.bcphotoanimations.view.util.AsyncTaskExportMP4_MediaCodec;
import org.byters.bcphotoanimations.view.util.NotificationExportComplete;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {AppModule.class})
@Singleton
public interface AppComponent {

    void inject(FragmentCamera param);

    void inject(CacheInterfaceState param);

    void inject(FragmentProjects param);

    void inject(FragmentAbout param);

    void inject(FragmentFrames param);

    void inject(AdapterFrames param);

    void inject(FragmentProjectEdit param);

    void inject(AdapterProjects param);

    void inject(PresenterAdapterFrames param);

    void inject(CacheProjectSelected param);

    void inject(PresenterAdapterProjects param);

    void inject(ActivityMain param);

    void inject(PresenterProjectEdit param);

    void inject(PresenterCamera param);

    void inject(CacheStorage param);

    void inject(CacheProjects param);

    void inject(PresenterFrames params);

    void inject(FragmentPreview params);

    void inject(PresenterPreview presenterPreview);

    void inject(ServiceProjectExport param);

    void inject(CacheExportAttempts cacheExportAttempts);

    void inject(CachePreference cachePreference);

    void inject(CameraPreview cameraPreview);

    void inject(AsyncTaskExportMP4_JCodec runnableConvert);

    void inject(FragmentProjectCreate fragmentProjectCreate);

    void inject(Navigator navigator);

    void inject(DialogExportJCodec dialogExportMJPEG);

    void inject(PresenterProjectExportJCodec presenterProjectExportMJPEG);

    void inject(PresenterProjectCreate presenterProjectCreate);

    void inject(PresenterProjectExportMediaCodec presenterProjectExportMediaCodec);

    void inject(AsyncTaskExportMP4_MediaCodec asyncTaskExportMP4_mediaCodec);

    void inject(DialogExportMediaCodec dialogExportMediaCodec);

    void inject(AsyncTaskProjectExport asyncTaskProjectExport);

    void inject(NotificationExportComplete notificationExportComplete);
}
