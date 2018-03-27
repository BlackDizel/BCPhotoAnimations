package org.byters.bcphotoanimations.controller.data.memorycache;

import android.text.TextUtils;

import org.byters.bcphotoanimations.ApplicationStopMotion;
import org.byters.bcphotoanimations.model.FrameObject;

import java.util.Iterator;
import java.util.UUID;

import javax.inject.Inject;

public class CacheProjectSelected implements ICacheProjectSelected {

    @Inject
    ICacheProjects cacheProjects;

    @Inject
    ICacheStorage cacheStorage;

    @Inject
    ICacheFramesSelected cacheFramesSelected;

    private String projectSelectedId;
    private String titleEdit;

    public CacheProjectSelected() {
        ApplicationStopMotion.getComponent().inject(this);
    }

    @Override
    public void setSelectedProject(int position) {
        String projectId = cacheProjects.getProjectId(position);
        if (projectId != null && projectId.equals(this.projectSelectedId))
            return;

        this.projectSelectedId = projectId;
        cacheFramesSelected.resetCache();
    }

    @Override
    public void resetCache() {
        projectSelectedId = null;
    }

    @Override
    public String getProjectTitleEdit() {
        return titleEdit;
    }

    @Override
    public void setProjectTitleEdit(String title) {
        this.titleEdit = title;
    }

    @Override
    public void resetTitleEdit() {
        if (TextUtils.isEmpty(getProjectSelectedId())) {
            this.titleEdit = null;
            return;
        }

        this.titleEdit = cacheProjects.getItemTitleById(projectSelectedId);
    }

    @Override
    public void saveProject() {
        if (TextUtils.isEmpty(titleEdit)) return;
        projectSelectedId = cacheProjects.editProject(projectSelectedId, titleEdit);
    }

    @Override
    public String getProjectSelectedId() {
        return projectSelectedId;
    }

    @Override
    public void addFrame(byte[] data) {
        FrameObject frameObject = new FrameObject();

        String path = cacheStorage.getProjectFolderFile(projectSelectedId, UUID.randomUUID().toString(), cacheStorage.getImageExt());

        frameObject.setUrlFile(path);

        cacheProjects.addFrame(projectSelectedId, frameObject);

        cacheStorage.writeObjectToFile(data, path);

    }

    @Override
    public boolean isEdit() {
        return !TextUtils.isEmpty(getProjectSelectedId());
    }

    @Override
    public void removeProject() {
        cacheProjects.removeProject(projectSelectedId);
    }

    @Override
    public String getFrameId(int position) {
        return cacheProjects.getFrameId(projectSelectedId, position);
    }

    @Override
    public void removeSelected() {

        Iterator itr = cacheProjects.getFramesIterator(projectSelectedId);
        if (itr == null) return;

        while (itr.hasNext()) {

            FrameObject frame = (FrameObject) itr.next();

            if (cacheFramesSelected.isSelected(frame.getId())) {
                cacheFramesSelected.changeSelected(frame.getId());
                cacheStorage.removeFile(frame.getFileUrl());
                itr.remove();
            }

        }

        cacheProjects.storeCache();

    }

    @Override
    public void revertSelected() {
        //todo implement
    }
}
