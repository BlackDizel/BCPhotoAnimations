package org.byters.bcphotoanimations.model;

public enum PreferenceHelperEnum {
    PROJECTS_CACHE("cache_projects", ProjectModel.class);

    private final String name;
    private final Class type;

    PreferenceHelperEnum(String name, Class type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Class getType() {
        return type;
    }
}
