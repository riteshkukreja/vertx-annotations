package com.github.riteshkukreja.modal;

public class GeneratorItem {

    private String parentName;
    private String name;
    private String packageName;

    public GeneratorItem(String parentName, String name, String packageName) {
        this.parentName = parentName;
        this.name = name;
        this.packageName = packageName;
    }

    public String getParentName() {

        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
