package org.aleksid.wikime.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.aleksid.wikime.model.Tag;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity(name = "TAGS")
@Data
@NoArgsConstructor
public class DBTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tag;
    @ManyToMany(mappedBy = "tags", fetch = FetchType.EAGER)
    private Set<DBArticle> articles = new HashSet<>();

    public DBTag(Tag Tag) {
        this.tag = Tag.getTag();
    }

    public DBTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "DBTag{" +
                "id=" + id +
                ", tag='" + tag + '\'' +
                ", used in number of articles ='" + articles.size() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DBTag tag1 = (DBTag) o;
        return Objects.equals(tag, tag1.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag);
    }
}
