package dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import model.aTag;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "TAGS")
@Data
@NoArgsConstructor
public class DBTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String tag;
    @ManyToMany(mappedBy = "tags", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<DBArticle> articles = new ArrayList<>();// хотя бы на одной стороне должен быть List, если Set и тут и там, то Connection Lick при get()

    public DBTag(aTag aTag) {
        this.tag = aTag.getTag();
    }

    @Override//если не переопределить, то StackOverflow и ругается на строчку с @Data аннотацией
    public String toString() {
        return "DBTag{" +
                "tag='" + tag + '\'' +
                '}';
    }

    public DBTag(String tag) {
        this.tag = tag;
    }
}
