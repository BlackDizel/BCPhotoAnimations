package org.byters.bcphotoanimations.controller;

import org.byters.bcphotoanimations.controller.data.memorycache.CacheProjectSelected;
import org.byters.bcphotoanimations.controller.data.memorycache.CacheProjects;
import org.byters.bcphotoanimations.controller.data.memorycache.ICacheProjectSelected;
import org.byters.bcphotoanimations.controller.data.memorycache.ICacheProjects;
import org.byters.bcphotoanimations.view.INavigator;
import org.byters.bcphotoanimations.view.Navigator;
import org.byters.bcphotoanimations.view.presenter.IPresenterAdapterFrames;
import org.byters.bcphotoanimations.view.presenter.IPresenterAdapterProjects;
import org.byters.bcphotoanimations.view.presenter.IPresenterCamera;
import org.byters.bcphotoanimations.view.presenter.IPresenterProjectEdit;
import org.byters.bcphotoanimations.view.presenter.PresenterAdapterFrames;
import org.byters.bcphotoanimations.view.presenter.PresenterAdapterProjects;
import org.byters.bcphotoanimations.view.presenter.PresenterCamera;
import org.byters.bcphotoanimations.view.presenter.PresenterProjectEdit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    @Provides
    @Singleton
    IPresenterAdapterProjects getPresenterAdapterProjects() {
        return new PresenterAdapterProjects();
    }

    @Provides
    @Singleton
    ICacheProjectSelected getCacheProjectSelected() {
        return new CacheProjectSelected();
    }

    @Provides
    @Singleton
    ICacheProjects getCacheProjects() {
        return new CacheProjects();
    }

    @Provides
    @Singleton
    INavigator getNavigator() {
        return new Navigator();
    }

    @Provides
    @Singleton
    IPresenterProjectEdit getPresenterProjectEdit() {
        return new PresenterProjectEdit();
    }

    @Provides
    @Singleton
    IPresenterAdapterFrames getPresenterAdapterFrames() {
        return new PresenterAdapterFrames();
    }

    @Provides
    @Singleton
    IPresenterCamera getPresenterCamera() {
        return new PresenterCamera();
    }
}
