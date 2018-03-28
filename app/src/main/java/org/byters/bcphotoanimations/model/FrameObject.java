package org.byters.bcphotoanimations.model;

import java.io.Serializable;
import java.util.UUID;

public class FrameObject implements Serializable {

    private String urlFile;
    private String id;

    public FrameObject() {
        id = UUID.randomUUID().toString();
    }

    public static FrameObject newInstance(FrameObject frame) {
        if (frame == null) return null;

        FrameObject result = new FrameObject();
        result.urlFile = frame.urlFile;
        return result;
    }

    public String getPreview() {
        return urlFile;
    }

    public void setUrlFile(String urlFile) {
        this.urlFile = urlFile;
    }

    public String getId() {
        return id;
    }

    public String getFileUrl() {
        return urlFile;
    }
}
