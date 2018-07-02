package com.github.riteshkukreja.modal;

public class Path implements Comparable {

    private String path;
    private MethodType method;


    @Override
    public int compareTo(Object o) {
        Path item = (Path) o;
        return path.compareTo(item.path);
    }

    @Override
    public boolean equals(Object obj) {
        Path item = (Path) obj;
        return path.equals(item.path) && method == item.method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public MethodType getMethod() {
        return method;
    }

    public void setMethod(MethodType method) {
        this.method = method;
    }

    public Path(String path, MethodType method) {
        this.path = path;
        this.method = method;
    }
}
