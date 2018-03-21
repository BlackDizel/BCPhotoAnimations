package org.byters.bcphotoanimations.model;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;

public class ProjectModel implements Serializable {
    ArrayList<ProjectObjectBase> data;

    public ProjectModel() {
    }

    public void init(){
        data = new ArrayList<>();
        data.add(new ProjectObjectNew());
    }

    public int getSize() {
        return data == null ? 0 : data.size();
    }

    public void addItem(ProjectObject item) {
        if (data == null) data = new ArrayList<>();
        data.add(item);
    }

    public ProjectObject getItemById(String projectSelectedId) {
        if (data == null || TextUtils.isEmpty(projectSelectedId)) return null;
        for (ProjectObjectBase item : data) {
            if (!(item instanceof ProjectObject)) continue;
            if (((ProjectObject) item).getId().equals(projectSelectedId))
                return (ProjectObject) item;

        }
        return null;
    }

    public ProjectObjectBase getItem(int position) {
        if (data == null || data.size() == 0 || position < 0 || position >= data.size())
            return null;
        return data.get(position);
    }
}
