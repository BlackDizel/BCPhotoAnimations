package org.byters.bcphotoanimations.controller.data.memorycache;

public interface ICachePreview {
    void resetCache(int projectSelectedFramesNum);

    void stop();

    void play();

    boolean isPlaying();

    int getNextFrameIndex();

    long getFrameDuration();

    void changeFPS();

    int getFrameRangeIndexFrom();

    int getFrameRangeIndexTo();

    int getFrameCurrentIndex();

    void setFrameFromView(int position);

    void selectRange(int from, int to);
}
