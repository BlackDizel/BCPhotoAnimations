package org.byters.bcphotoanimations.controller.data.memorycache;

import android.text.TextUtils;

import org.byters.bcphotoanimations.ApplicationStopMotion;

import javax.inject.Inject;

public class CacheProjectSelected implements ICacheProjectSelected {

    @Inject
    ICacheProjects cacheProjects;

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
}
