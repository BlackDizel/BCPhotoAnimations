package org.byters.bcphotoanimations.controller.data.memorycache;


public class CachePreview implements ICachePreview {

    private boolean isPlaying;
    private int currentFrame;
    private long frameDurationMillis;

    private int[] arrDuration = {100, 200, 500, 1000};

    public CachePreview() {
        resetCache();
    }

    @Override
    public void resetCache() {
        isPlaying = false;
        currentFrame = 0;
        frameDurationMillis = arrDuration[0];
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
    public int getNextFrameIndex(int framesNum) {
        ++currentFrame;
        currentFrame = currentFrame % framesNum;
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
}
