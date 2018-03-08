package org.byters.bcphotoanimations.model;

public class ProjectObject extends ProjectObjectBase {

    private String UUID;
    private String title;

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
}
