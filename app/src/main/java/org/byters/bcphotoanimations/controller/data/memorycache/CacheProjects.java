package org.byters.bcphotoanimations.controller.data.memorycache;

import android.text.TextUtils;

import org.byters.bcphotoanimations.ApplicationStopMotion;
import org.byters.bcphotoanimations.controller.data.memorycache.callback.ICacheProjectsCallback;
import org.byters.bcphotoanimations.model.FrameObject;
import org.byters.bcphotoanimations.model.PreferenceHelperEnum;
import org.byters.bcphotoanimations.model.ProjectModel;
import org.byters.bcphotoanimations.model.ProjectObject;
import org.byters.bcphotoanimations.model.ProjectObjectBase;
import org.byters.bcphotoanimations.model.ProjectObjectNew;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import javax.inject.Inject;

public class CacheProjects implements ICacheProjects {

    @Inject
    ICacheStorage cacheStorage;
    private ProjectModel model;
    private HashMap<String, WeakReference<ICacheProjectsCallback>> refListeners;

    public CacheProjects() {
        ApplicationStopMotion.getComponent().inject(this);
        resetCache();
    }

    private void resetCache() {
        model = cacheStorage.readObjectFromFile(cacheStorage.getAppFolder(), PreferenceHelperEnum.PROJECTS_CACHE, ProjectModel.class);
        if (model == null) {
            model = new ProjectModel();
            model.init();
        }
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
        return model == null ? 0 : model.getSize();
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
        model.addItem(item);

        storeCache();

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

    @Override
    public void addFrame(String projectId, FrameObject frameObject) {
        ProjectObject item = getItemById(projectId);
        if (item == null) return;
        item.addFrame(frameObject);
        notifyListeners();
    }

    @Override
    public void storeCache() {
        cacheStorage.writeObjectToFile(model, cacheStorage.getAppFolder(), PreferenceHelperEnum.PROJECTS_CACHE);
    }

    private ProjectObject getItemById(String projectSelectedId) {
        if (model == null || TextUtils.isEmpty(projectSelectedId)) return null;
        return model.getItemById(projectSelectedId);
    }

    private ProjectObjectBase getItem(int position) {
        return model == null ? null : model.getItem(position);
    }
}
