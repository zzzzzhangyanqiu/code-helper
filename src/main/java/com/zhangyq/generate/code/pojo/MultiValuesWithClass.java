package com.zhangyq.generate.code.pojo;

import java.util.HashSet;
import java.util.Set;

public class MultiValuesWithClass {
    private Object object;
    private Set<String> names;

    public MultiValuesWithClass(Object object) {
        this.object = object;
        this.names = new HashSet<>();
    }

    public void addNames(String name) {
        names.add(name);
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Set<String> getNames() {
        return names;
    }

    public void setNames(Set<String> names) {
        this.names = names;
    }
}
