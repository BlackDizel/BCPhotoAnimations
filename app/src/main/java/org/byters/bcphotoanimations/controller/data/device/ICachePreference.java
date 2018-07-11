package org.byters.bcphotoanimations.controller.data.device;

public interface ICachePreference {
    boolean isExportAttemptsExist();

    void saveInitExportAttempts();

    int getExportAttemptsNum();

    boolean isExportAttemptsUnlimited();

    void saveExportAttemptsNum(int num);

    void saveAttemptsUnlimited();
}
