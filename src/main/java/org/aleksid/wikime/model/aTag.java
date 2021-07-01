package org.aleksid.wikime.model;

import lombok.Data;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Data
public class aTag {
    private int id;
    private String tag;
    private static Set<aTag> tags = new CopyOnWriteArraySet<>();

    public aTag(String newTag){
        this.id = newTag.hashCode();
        this.tag = newTag;
        tags.add(this);
    }

    @Override
    public String toString() {
        return tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        aTag tag1 = (aTag) o;
        return tag.equals(tag1.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag);
    }

//    public aTag(DBTag dbTag) {
//        this.id = dbTag.getId();
//        this.tag = dbTag.getTag();
//    }
}
