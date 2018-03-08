package org.byters.bcphotoanimations.controller;

import org.byters.bcphotoanimations.controller.data.memorycache.CacheProjectSelected;
import org.byters.bcphotoanimations.controller.data.memorycache.CacheProjects;
import org.byters.bcphotoanimations.controller.data.memorycache.ICacheProjectSelected;
import org.byters.bcphotoanimations.controller.data.memorycache.ICacheProjects;
import org.byters.bcphotoanimations.view.INavigator;
import org.byters.bcphotoanimations.view.Navigator;
import org.byters.bcphotoanimations.view.presenter.IPresenterAdapterProjects;
import org.byters.bcphotoanimations.view.presenter.IPresenterProjectEdit;
import org.byters.bcphotoanimations.view.presenter.PresenterAdapterProjects;
import org.byters.bcphotoanimations.view.presenter.PresenterProjectEdit;

public class Injector {

    private static Injector instance;

    private IPresenterAdapterProjects presenterAdapterProjects;
    private IPresenterProjectEdit presenterProjectEdit;

    private ICacheProjects cacheProjects;
    private ICacheProjectSelected cacheProjectSelected;

    private INavigator navigator;

    public Injector() {
        instance = this;
    }

    public static Injector getInstance() {
        return instance;
    }

    public IPresenterAdapterProjects getPresenterAdapterProjects() {
        if (presenterAdapterProjects == null)
            presenterAdapterProjects = new PresenterAdapterProjects(getCacheProjects(), getCacheProjectSelected(), getNavigator());
        return presenterAdapterProjects;
    }

    private ICacheProjectSelected getCacheProjectSelected() {
        if (cacheProjectSelected == null)
            cacheProjectSelected = new CacheProjectSelected(getCacheProjects());
        return cacheProjectSelected;
    }

    private ICacheProjects getCacheProjects() {
        if (cacheProjects == null) cacheProjects = new CacheProjects();
        return cacheProjects;
    }

    public INavigator getNavigator() {
        if (navigator == null) navigator = new Navigator();
        return navigator;
    }

    public IPresenterProjectEdit getPresenterProjectEdit() {
        if (presenterProjectEdit == null)
            presenterProjectEdit = new PresenterProjectEdit(getNavigator(), getCacheProjectSelected());
        return presenterProjectEdit;
    }
}
