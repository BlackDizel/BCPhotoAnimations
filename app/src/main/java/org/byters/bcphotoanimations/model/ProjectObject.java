package org.byters.bcphotoanimations.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.ListIterator;

public class ProjectObject extends ProjectObjectBase implements Serializable {

    private String UUID;
    private String title;
    private ArrayList<FrameObject> frames;

    private ProjectObject() {
    }

    public static ProjectObject newItem(String title) {
        ProjectObject item = new ProjectObject();
        item.setId(java.util.UUID.randomUUID().toString());
        item.setTitle(title);
        return item;
    }

    public static int getType() {
        return ProjectObjectBase.TYPE_PROJECT;
    }

    public String getId() {
        return UUID;
    }

    private void setId(String id) {
        this.UUID = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getFramesNum() {
        return frames == null ? 0 : frames.size();
    }

    public String getFramePreview(int position) {
        FrameObject item = getItem(position);
        return item == null ? null : item.getPreview();
    }

    private FrameObject getItem(int position) {
        if (frames == null || frames.size() <= position || position < 0) return null;
        return frames.get(position);
    }

    public void addFrame(FrameObject frameObject) {
        if (frames == null) frames = new ArrayList<>();
        frames.add(frameObject);
    }

    public String getFrameId(int position) {
        if (frames == null || position < 0 || position >= getFramesNum())
            return null;
        return frames.get(position).getId();
    }

    public void addFrames(ArrayList<FrameObject> param, int position) {
        if (param == null) return;

        if (this.frames == null) {
            this.frames = param;
            return;
        }

        if (position > this.frames.size() || position < 0) return;

        this.frames.addAll(position, param);
    }

    public ListIterator<FrameObject> getFramesIteratorList() {
        return frames == null ? null : frames.listIterator();
    }

    public ListIterator<FrameObject> getFramesIteratorListReverse() {
        return frames == null ? null : frames.listIterator(frames.size());
    }

    public String getFrameLastPreview() {
        return frames == null || frames.size() == 0 ? null : frames.get(frames.size() - 1).getPreview();
    }

    public String getFrameUrl(int position) {
        return frames == null || position >= frames.size() || position < 0 ? null : frames.get(position).getFileUrl();
    }
}
