package org.byters.bcphotoanimations.model;

public class ProjectObject extends ProjectObjectBase {

    private String UUID;

    private ProjectObject() {
    }

    public static ProjectObject newItem() {
        ProjectObject item = new ProjectObject();
        item.setId(java.util.UUID.randomUUID().toString());
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
}
