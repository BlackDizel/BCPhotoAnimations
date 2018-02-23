package org.byters.bcphotoanimations;

import android.app.Application;

import org.byters.bcphotoanimations.controller.Injector;

public class ApplicationStopMotion extends Application {

    private Injector injector;

    @Override
    public void onCreate() {
        super.onCreate();

        injector = new Injector();

    }
}
