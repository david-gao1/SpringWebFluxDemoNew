package com.solinvictus.SpringWebFluxDemoNew.branchFlow;

public class TaskPojo {
    private String name;
    private String type;

    public TaskPojo(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
