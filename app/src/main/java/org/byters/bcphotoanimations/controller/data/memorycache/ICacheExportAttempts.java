package org.byters.bcphotoanimations.controller.data.memorycache;

public interface ICacheExportAttempts {
    boolean isEnoughAttempts();

    void decreaseAttempts();

    int getAttempts();

    void setAttemptsUnlimited();
}
