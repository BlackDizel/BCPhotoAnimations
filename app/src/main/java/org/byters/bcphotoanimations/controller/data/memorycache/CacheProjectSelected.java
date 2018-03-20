package org.byters.bcphotoanimations.controller.data.memorycache;

import android.text.TextUtils;

import org.byters.bcphotoanimations.ApplicationStopMotion;
import org.byters.bcphotoanimations.model.FrameObject;

import java.io.File;
import java.util.UUID;

import javax.inject.Inject;

public class CacheProjectSelected implements ICacheProjectSelected {

    @Inject
    ICacheProjects cacheProjects;

    @Inject
    ICacheStorage cacheStorage;

    private String projectSelectedId;
    private String titleEdit;

    public CacheProjectSelected() {
        ApplicationStopMotion.getComponent().inject(this);
    }

    @Override
    public void setSelectedProject(int position) {
        this.projectSelectedId = cacheProjects.getProjectId(position);
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
        this.titleEdit = null;
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

        String path = cacheStorage.getAppFolder()
                + File.separator
                + projectSelectedId
                + File.separator
                + UUID.randomUUID()
                + cacheStorage.getImageExt();

        frameObject.setUrlFile(path);

        cacheProjects.addFrame(projectSelectedId, frameObject);

        cacheStorage.writeObjectToFile(data, path);

    }
}
