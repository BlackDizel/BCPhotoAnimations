package org.byters.bcphotoanimations.model;

import java.io.Serializable;
import java.util.UUID;

public class FrameObject implements Serializable{

    private String urlFile;
    private String id;

    public FrameObject() {
        id = UUID.randomUUID().toString();
    }

    public String getPreview() {
        return urlFile;
    }

    public void setUrlFile(String urlFile) {
        this.urlFile = urlFile;
    }
}
