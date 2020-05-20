package org.byters.bcphotoanimations.view.ui.utils;

import android.os.AsyncTask;

import org.byters.bcphotoanimations.ApplicationStopMotion;
import org.byters.bcphotoanimations.controller.data.memorycache.ICacheProjects;
import org.byters.bcphotoanimations.controller.data.memorycache.ICacheStorage;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

public class AsyncTaskProjectExport extends AsyncTask<Void, Integer, Boolean> {

    @Inject
    ICacheProjects cacheProjects;

    @Inject
    ICacheStorage cacheStorage;
    private String projectId;
    private WeakReference<AsyncTaskProjectExportListener> refListener;

    public AsyncTaskProjectExport(String projectId,
                                  AsyncTaskProjectExportListener listener) {
        ApplicationStopMotion.getComponent().inject(this);

        this.projectId = projectId;
        this.refListener = new WeakReference<>(listener);
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        int framesNum = cacheProjects.getItemFramesNum(projectId);

        cacheStorage.removeFolder(cacheProjects, projectId);
        for (int i = 0; i < framesNum; ++i) {
            cacheStorage.copyFrame(cacheProjects, projectId, i, framesNum);
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

        String title = cacheProjects.getItemTitleById(projectId);
        refListener.get().onCompleteFolder(projectId, cacheStorage.getProjectOutputFolder(title));
    }

}
