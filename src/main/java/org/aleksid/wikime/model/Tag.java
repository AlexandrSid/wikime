package org.aleksid.wikime.model;

import org.aleksid.wikime.dto.DBTag;
import lombok.Data;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Data
public class Tag {
    private int id;
    private String tag;
    private static Set<Tag> tags = new CopyOnWriteArraySet<>();

    public Tag(String newTag){
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
        Tag tag1 = (Tag) o;
        return tag.equals(tag1.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag);
    }

    public Tag(DBTag dbTag) {
        this.id = dbTag.getId().intValue();
        this.tag = dbTag.getTag();
    }
}
