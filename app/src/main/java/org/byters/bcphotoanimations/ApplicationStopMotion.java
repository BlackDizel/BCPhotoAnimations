package org.byters.bcphotoanimations;

import android.app.Application;

public class ApplicationStopMotion extends Application {

    private static AppComponent component;

    public static AppComponent getComponent() {
        return component;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        component = buildComponent();
    }

    private AppComponent buildComponent() {
        return DaggerAppComponent
                .builder().build();
    }
}
