package org.byters.bcphotoanimations.controller.data.memorycache;

import android.text.TextUtils;

import java.lang.ref.WeakReference;

public class CacheProjectSelected implements ICacheProjectSelected {

    private WeakReference<ICacheProjects> refCacheProjects;
    private String projectSelectedId;
    private String titleEdit;

    public CacheProjectSelected(ICacheProjects cacheProjects) {
        this.refCacheProjects = new WeakReference<>(cacheProjects);
    }

    @Override
    public void setSelectedProject(int position) {
        this.projectSelectedId = refCacheProjects.get().getProjectId(position);
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
        projectSelectedId = refCacheProjects.get().editProject(projectSelectedId, titleEdit);
    }

    @Override
    public String getProjectSelectedId() {
        return projectSelectedId;
    }
}
