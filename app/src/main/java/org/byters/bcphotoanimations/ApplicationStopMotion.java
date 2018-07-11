package org.byters.bcphotoanimations;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
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
        Fabric.with(this, new Crashlytics());

        component = buildComponent();
    }

    private AppComponent buildComponent() {
        return DaggerAppComponent
                .builder()
                .appModule(new AppModule(this, new LibDataPlayBilling(this)))
                .build();
    }
}
