package model;

import lombok.Data;

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
}
