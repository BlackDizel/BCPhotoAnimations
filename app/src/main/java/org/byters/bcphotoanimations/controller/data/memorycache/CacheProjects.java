package org.byters.bcphotoanimations.controller.data.memorycache;

import android.text.TextUtils;

import org.byters.bcphotoanimations.controller.data.memorycache.callback.ICacheProjectsCallback;
import org.byters.bcphotoanimations.model.ProjectObject;
import org.byters.bcphotoanimations.model.ProjectObjectBase;
import org.byters.bcphotoanimations.model.ProjectObjectNew;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

public class CacheProjects implements ICacheProjects {

    private ArrayList<ProjectObjectBase> data;
    private HashMap<String, WeakReference<ICacheProjectsCallback>> refListeners;

    public CacheProjects() {
        resetCache();
    }

    private void resetCache() {
        data = new ArrayList<>();
        data.add(new ProjectObjectNew());
        notifyListeners();
    }

    private void notifyListeners() {
        if (refListeners == null || refListeners.keySet() == null) return;
        for (String key : refListeners.keySet()) {
            if (key == null) continue;
            WeakReference<ICacheProjectsCallback> refcallback = refListeners.get(key);
            if (refcallback == null || refcallback.get() == null) continue;
            refcallback.get().onUpdate();
        }
    }

    @Override
    public void addCallback(ICacheProjectsCallback callback) {
        if (refListeners == null)
            refListeners = new HashMap<>();

        refListeners.put(callback.getClass().getName(), new WeakReference<>(callback));
    }

    @Override
    public int getDataSize() {
        return data == null ? 0 : data.size();
    }

    @Override
    public int getItemType(int position) {
        ProjectObjectBase item = getItem(position);
        if (item == null) return ProjectObjectBase.TYPE_UNKNOWN;
        if (item instanceof ProjectObjectNew) return ProjectObjectNew.getType();
        if (item instanceof ProjectObject) return ProjectObject.getType();
        return ProjectObjectBase.TYPE_UNKNOWN;
    }

    @Override
    public String getProjectId(int position) {
        ProjectObjectBase item = getItem(position);

        if (item == null || !(item instanceof ProjectObject)) return null;

        return ((ProjectObject) item).getId();
    }

    @Override
    public String editProject(String projectSelectedId, String title) {
        ProjectObject item = getItemById(projectSelectedId);
        if (item != null) {
            item.setTitle(title);
            notifyListeners();
            return item.getId();
        }

        item = ProjectObject.newItem(title);
        data.add(item);
        notifyListeners();
        return item.getId();
    }

    @Override
    public String getItemTitle(int position) {
        ProjectObjectBase item = getItem(position);
        if (item == null || !(item instanceof ProjectObject)) return null;
        return ((ProjectObject) item).getTitle();
    }

    @Override
    public boolean isProjectNew(int position) {
        ProjectObjectBase item = getItem(position);
        return item instanceof ProjectObjectNew;
    }

    @Override
    public int getItemFramesNum(String projectId) {
        ProjectObject item = getItemById(projectId);
        return item == null ? 0 : item.getFramesNum();
    }

    @Override
    public String getItemImagePreview(String projectId, int position) {
        ProjectObject item = getItemById(projectId);
        return item == null ? null : item.getFramePreview(position);
    }

    private ProjectObject getItemById(String projectSelectedId) {
        if (data == null || TextUtils.isEmpty(projectSelectedId)) return null;
        for (ProjectObjectBase item : data) {
            if (!(item instanceof ProjectObject)) continue;
            if (((ProjectObject) item).getId().equals(projectSelectedId))
                return (ProjectObject) item;

        }
        return null;
    }

    private ProjectObjectBase getItem(int position) {
        if (data == null || data.size() == 0 || position < 0 || position >= data.size())
            return null;
        return data.get(position);
    }
}
