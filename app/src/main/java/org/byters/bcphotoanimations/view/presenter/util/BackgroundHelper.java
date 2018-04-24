package org.byters.bcphotoanimations.view.presenter.util;

import org.byters.bcphotoanimations.controller.data.memorycache.ICacheProjectSelected;
import org.byters.bcphotoanimations.controller.data.memorycache.ICacheStorage;

import java.lang.ref.WeakReference;

public class BackgroundHelper {

    private boolean isWork;
    private WeakReference<IBackgroundHelperCallback> refCallback;

    public BackgroundHelper() {
        isWork = false;
    }

    public void writeProjectBackground(ICacheStorage cacheStorage, ICacheProjectSelected cacheProjectSelected) {

        //todo replace with ForegroundService or IntentService with notification with progressbar

        if (isWork) return;

        RunnableWrite runnableWrite = new RunnableWrite();

        runnableWrite.setData(cacheStorage, cacheProjectSelected);
        Thread tBackground = new Thread(runnableWrite);
        isWork = true;
        tBackground.start();
    }

    public void setListener(IBackgroundHelperCallback callback) {
        this.refCallback = new WeakReference<>(callback);
    }

    private void notifyListenersError() {
        if (refCallback == null || refCallback.get() == null) return;
        refCallback.get().onError();
    }

    private void notifyListeners(String id) {
        if (refCallback == null || refCallback.get() == null) return;
        refCallback.get().onSuccess(id);
    }

    private class RunnableWrite implements Runnable {
        private WeakReference<ICacheStorage> refCacheStorage;
        private WeakReference<ICacheProjectSelected> refCacheProjectSelected;

        @Override
        public void run() {
            if (refCacheStorage == null
                    || refCacheStorage.get() == null
                    || refCacheProjectSelected == null
                    || refCacheProjectSelected.get() == null) {
                isWork = false;
                notifyListenersError();
                return;
            }

            String id = refCacheProjectSelected.get().getProjectSelectedId();
            refCacheStorage.get().writeProject(refCacheProjectSelected.get());
            isWork = false;
            notifyListeners(id);
        }

        public void setData(ICacheStorage cacheStorage, ICacheProjectSelected cacheProjectSelected) {
            this.refCacheStorage = new WeakReference<>(cacheStorage);
            this.refCacheProjectSelected = new WeakReference<>(cacheProjectSelected);
        }
    }
}
