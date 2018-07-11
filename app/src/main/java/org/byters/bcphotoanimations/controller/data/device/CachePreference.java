package org.byters.bcphotoanimations.controller.data.device;

import android.content.Context;
import android.content.SharedPreferences;

import org.byters.bcphotoanimations.ApplicationStopMotion;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

public class CachePreference implements ICachePreference {

    private static final String PREF_STORAGE = "stop_motion_app_preferences";
    private static final String PREF_EXPORT_ATTEMPTS = "export_attempts";
    private static final String PREF_EXPORT_ATTEMPTS_UNLIMIT = "export_attempts_unlimit";

    private static final int EXPORT_ATTEMPTS_INIT = 5;

    @Inject
    WeakReference<Context> refContext;

    public CachePreference() {
        ApplicationStopMotion.getComponent().inject(this);
    }

    @Override
    public boolean isExportAttemptsExist() {
        return getSharedPreferences()
                .contains(PREF_EXPORT_ATTEMPTS);
    }

    @Override
    public void saveInitExportAttempts() {
        saveExportAttemptsNum(EXPORT_ATTEMPTS_INIT);
    }

    private SharedPreferences getSharedPreferences() {
        return refContext.get().getSharedPreferences(PREF_STORAGE, Context.MODE_PRIVATE);
    }

    @Override
    public int getExportAttemptsNum() {
        return getSharedPreferences().getInt(PREF_EXPORT_ATTEMPTS, 0);
    }

    @Override
    public boolean isExportAttemptsUnlimited() {
        return getSharedPreferences().getBoolean(PREF_EXPORT_ATTEMPTS_UNLIMIT, false);
    }


    @Override
    public void saveAttemptsUnlimited() {
        getSharedPreferences().edit().putBoolean(PREF_EXPORT_ATTEMPTS_UNLIMIT, true).commit();
    }

    @Override
    public void saveExportAttemptsNum(int num) {
        getSharedPreferences().edit().putInt(PREF_EXPORT_ATTEMPTS, num).commit();
    }

}

