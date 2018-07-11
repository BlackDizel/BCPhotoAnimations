package org.byters.bcphotoanimations;

import android.app.Application;

import org.byters.bcphotoanimations.controller.AppModule;
import org.byters.dataplaybilling.LibDataPlayBilling;

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
                .builder()
                .appModule(new AppModule(this, new LibDataPlayBilling(this)))
                .build();
    }
}
