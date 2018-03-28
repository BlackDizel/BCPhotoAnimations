package org.byters.bcphotoanimations.controller.data.memorycache;

import android.text.TextUtils;

import org.byters.bcphotoanimations.ApplicationStopMotion;
import org.byters.bcphotoanimations.model.FrameObject;

import java.util.ArrayList;
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
                cacheFramesSelected.setSelected(frame.getId(), false);
                //cacheStorage.removeFile(frame.getFileUrl()); //fixme several frames can link to same file
                itr.remove();
            }

        }

        cacheProjects.storeCache();

    }

    @Override
    public void revertSelected() {
        //todo implement
    }

    @Override
    public int getFramesNum() {
        return cacheProjects.getItemFramesNum(projectSelectedId);
    }

    @Override
    public void copySelectedFramesTo(int position) {
        ArrayList<FrameObject> result = copySelectedTo(position);

        cacheFramesSelected.selectCancel();
        setSelected(result);

        cacheProjects.storeCache();
    }

    @Override
    public void moveSelectedFramesTo(int position) {
        ArrayList<FrameObject> result = copySelectedTo(position);

        removeSelected();
        setSelected(result);

        cacheProjects.storeCache();
    }

    private void setSelected(ArrayList<FrameObject> result) {
        if (result == null) return;
        for (FrameObject item : result)
            cacheFramesSelected.setSelected(item.getId(), true);
    }


    private ArrayList<FrameObject> copySelectedTo(int position) {
        ArrayList<FrameObject> result = null;

        Iterator itr = cacheProjects.getFramesIterator(projectSelectedId);
        if (itr == null) return null;

        while (itr.hasNext()) {

            FrameObject frame = (FrameObject) itr.next();

            if (cacheFramesSelected.isSelected(frame.getId())) {

                if (result == null) result = new ArrayList<>();
                FrameObject item = FrameObject.newInstance(frame);
                result.add(item);
            }
        }

        if (result == null) return null;

        cacheProjects.addFrames(projectSelectedId, result, position);

        return result;
    }

    @Override
    public void selectRange(int val1, int val2) {
        int size = cacheProjects.getItemFramesNum(projectSelectedId);
        if (size == 0) return;

        int from, to;
        from = Math.max(Math.min(val1, val2), 0);
        to = Math.min(Math.max(val1, val2), size - 1);

        for (int i = from; i <= to; ++i) {
            String id = cacheProjects.getFrameId(projectSelectedId, i);
            if (TextUtils.isEmpty(id))
                continue;

            cacheFramesSelected.setSelected(id, true);
        }
    }
}
