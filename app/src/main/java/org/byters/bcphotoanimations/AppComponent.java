package org.byters.bcphotoanimations;

import org.byters.bcphotoanimations.controller.AppModule;
import org.byters.bcphotoanimations.controller.data.memorycache.CacheProjectSelected;
import org.byters.bcphotoanimations.controller.data.memorycache.CacheProjects;
import org.byters.bcphotoanimations.controller.data.memorycache.CacheStorage;
import org.byters.bcphotoanimations.view.presenter.PresenterAdapterFrames;
import org.byters.bcphotoanimations.view.presenter.PresenterAdapterProjects;
import org.byters.bcphotoanimations.view.presenter.PresenterCamera;
import org.byters.bcphotoanimations.view.presenter.PresenterPreview;
import org.byters.bcphotoanimations.view.presenter.PresenterFrames;
import org.byters.bcphotoanimations.view.presenter.PresenterProjectEdit;
import org.byters.bcphotoanimations.view.ui.activity.ActivityMain;
import org.byters.bcphotoanimations.view.ui.adapters.AdapterFrames;
import org.byters.bcphotoanimations.view.ui.adapters.AdapterProjects;
import org.byters.bcphotoanimations.view.ui.fragment.FragmentCamera;
import org.byters.bcphotoanimations.view.ui.fragment.FragmentFrames;
import org.byters.bcphotoanimations.view.ui.fragment.FragmentPreview;
import org.byters.bcphotoanimations.view.ui.fragment.FragmentProjectEdit;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {AppModule.class})
@Singleton
public interface AppComponent {

    void inject(FragmentCamera param);

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
}
