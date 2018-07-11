package org.byters.bcphotoanimations.view.ui.utils;

import android.os.AsyncTask;

import org.byters.bcphotoanimations.controller.data.memorycache.ICacheProjects;
import org.byters.bcphotoanimations.controller.data.memorycache.ICacheStorage;

import java.lang.ref.WeakReference;

public class AsyncTaskProjectExport extends AsyncTask<Void, Integer, Boolean> {

    private String projectId;
    private WeakReference<AsyncTaskProjectExportListener> refListener;
    private WeakReference<ICacheProjects> refCacheProjects;
    private WeakReference<ICacheStorage> refCacheStorage;

    public AsyncTaskProjectExport(String projectId,
                                  ICacheProjects cacheProjects,
                                  ICacheStorage cacheStorage,
                                  AsyncTaskProjectExportListener listener) {
        this.projectId = projectId;
        this.refCacheProjects = new WeakReference<>(cacheProjects);
        this.refCacheStorage = new WeakReference<>(cacheStorage);
        this.refListener = new WeakReference<>(listener);
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        int framesNum = refCacheProjects.get().getItemFramesNum(projectId);

        refCacheStorage.get().removeFolder(refCacheProjects.get(), projectId);
        for (int i = 0; i < framesNum; ++i) {
            refCacheStorage.get().copyFrame(refCacheProjects.get(), projectId, i, framesNum);
            onProgressUpdate(framesNum, i);
        }

        return true;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        if (refListener == null
                || refListener.get() == null) return;
        refListener.get().onUpdate(projectId, values);
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);

        if (refListener == null || refListener.get() == null) return;
        refListener.get().onComplete(projectId);
    }

}
