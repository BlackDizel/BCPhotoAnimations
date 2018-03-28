package org.byters.bcphotoanimations.controller.data.memorycache;

import org.byters.bcphotoanimations.controller.data.memorycache.callback.ICacheFramesSelectedCallback;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

public class CacheFramesSelected implements ICacheFramesSelected {

    private ArrayList<String> selectedIds;
    private HashMap<String, WeakReference<ICacheFramesSelectedCallback>> callbacks;

    @Override
    public void resetCache() {
        selectedIds = null;
        notifyListeners();
    }

    private void notifyListeners() {
        if (callbacks == null || callbacks.keySet() == null) return;
        for (String key : callbacks.keySet()) {
            if (key == null) continue;
            WeakReference<ICacheFramesSelectedCallback> refcallback = callbacks.get(key);
            if (refcallback == null || refcallback.get() == null) continue;
            refcallback.get().onUpdate();
        }
    }

    @Override
    public void changeSelected(String frameId) {
        if (selectedIds == null) selectedIds = new ArrayList<>();

        if (selectedIds.contains(frameId))
            selectedIds.remove(frameId);
        else
            selectedIds.add(frameId);

        notifyListeners();
    }

    @Override
    public void setSelected(String id, boolean isSelected) {
        if (isSelected && selectedIds == null)
            selectedIds = new ArrayList<>();

        if (isSelected && !selectedIds.contains(id))
            selectedIds.add(id);

        if (!isSelected)
            selectedIds.remove(id);

        notifyListeners();
    }

    @Override
    public boolean isModeSelect() {
        return selectedIds != null && selectedIds.size() > 0;
    }

    @Override
    public boolean isSelected(String frameId) {
        return selectedIds != null && selectedIds.contains(frameId);
    }

    @Override
    public void addCallback(ICacheFramesSelectedCallback callback) {
        if (callbacks == null)
            callbacks = new HashMap<>();

        callbacks.put(callback.getClass().getName(), new WeakReference<>(callback));
    }

    @Override
    public void selectCancel() {
        resetCache();
    }
}
