package dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import model.aTag;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "TAGS")
@Data
@NoArgsConstructor
public class DBTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String tag;
    @ManyToMany(mappedBy = "tags", fetch = FetchType.EAGER)
    private Set<DBArticle> articles = new HashSet<>();

    public DBTag(aTag aTag) {
        this.tag = aTag.getTag();
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
}
