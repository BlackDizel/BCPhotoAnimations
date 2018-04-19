package org.byters.bcphotoanimations.controller.data.memorycache;


public class CachePreview implements ICachePreview {

    private boolean isPlaying;
    private int currentFrame;
    private long frameDurationMillis;

    private int[] arrDuration = {100, 200, 500, 1000};
    private int frameRangeFrom;
    private int frameRangeTo;

    public CachePreview() {
        resetCache(0);
    }

    @Override
    public void resetCache(int projectSelectedFramesNum) {
        isPlaying = false;
        currentFrame = 0;
        frameDurationMillis = arrDuration[0];
        frameRangeFrom = 0;
        frameRangeTo = projectSelectedFramesNum;
    }

    @Override
    public void stop() {
        isPlaying = false;
    }

    @Override
    public void play() {
        isPlaying = true;
    }

    @Override
    public boolean isPlaying() {
        return isPlaying;
    }

    @Override
    public int getNextFrameIndex() {
        ++currentFrame;
        currentFrame = currentFrame >= frameRangeTo ? frameRangeFrom : currentFrame;
        return currentFrame;
    }

    @Override
    public long getFrameDuration() {
        return frameDurationMillis;
    }

    @Override
    public void changeFPS() {
        for (int i = 0; i < arrDuration.length; ++i) {
            if (frameDurationMillis != arrDuration[i]) continue;

            int index = (i + 1) % arrDuration.length;
            frameDurationMillis = arrDuration[index];
            return;
        }
        frameDurationMillis = arrDuration[0];
    }

    @Override
    public int getFrameRangeFrom() {
        return frameRangeFrom;
    }

    @Override
    public int getFrameRangeTo() {
        return frameRangeTo;
    }

    @Override
    public int getFrameCurrentIndex() {
        return currentFrame;
    }

    @Override
    public void setFrameFromView(int position) {
        currentFrame = frameRangeFrom + position;
    }
}
