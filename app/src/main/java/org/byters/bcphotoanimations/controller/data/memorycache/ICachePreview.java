package org.byters.bcphotoanimations.controller.data.memorycache;

public interface ICachePreview {
    void resetCache();

    void stop();

    void play();

    boolean isPlaying();

    int getNextFrameIndex(int framesNum);

    long getFrameDuration();

    void changeFPS();
}
