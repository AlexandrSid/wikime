package dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "TAGS")
@Data
@NoArgsConstructor
public class DBTag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String tag;
    @ManyToMany(mappedBy = "tags")
    private List<DBArticle> articles = new ArrayList<>();// хотя бы на одной стороне должен быть List, если Set и тут и там, то Connection Lick при get()

    @Override//если не переопределить, то StackOverflow и ругается на строчку с @Data аннотацией
    public String toString() {
        return "DBTag{" +
                "tag='" + tag + '\'' +
                '}';
    }
}
