package org.byters.bcphotoanimations.controller.data.memorycache;

import org.byters.bcphotoanimations.controller.data.memorycache.callback.ICacheFramesSelectedCallback;

import javax.inject.Singleton;

@Singleton
public interface ICacheFramesSelected {
    void resetCache();

    void changeSelected(String frameId);

    boolean isModeSelect();

    boolean isSelected(String frameId);

    void addCallback(ICacheFramesSelectedCallback callback);

    void selectCancel();

    void setSelected(String id, boolean isSelected);
}
