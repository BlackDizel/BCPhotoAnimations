package org.byters.bcphotoanimations.controller.data.memorycache;

import org.byters.bcphotoanimations.ApplicationStopMotion;
import org.byters.bcphotoanimations.controller.data.device.ICachePreference;

import javax.inject.Inject;

public class CacheExportAttempts implements ICacheExportAttempts {

    @Inject
    ICachePreference cachePreference;

    public CacheExportAttempts() {
        ApplicationStopMotion.getComponent().inject(this);
    }

    @Override
    public boolean isEnoughAttempts() {
        return getAttempts() > 0 || cachePreference.isExportAttemptsUnlimited();
    }

    @Override
    public void decreaseAttempts() {
        if (cachePreference.isExportAttemptsUnlimited()) return;
        int exportAttemptsNum = cachePreference.getExportAttemptsNum();
        cachePreference.saveExportAttemptsNum(--exportAttemptsNum);
    }

    @Override
    public int getAttempts() {
        if (!cachePreference.isExportAttemptsExist())
            cachePreference.saveInitExportAttempts();

        return cachePreference.getExportAttemptsNum();
    }

    @Override
    public void setAttemptsUnlimited() {
        cachePreference.saveAttemptsUnlimited();
    }
}
