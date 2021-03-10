package dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "ARTICLES")
@Data
@NoArgsConstructor
public class DBArticle {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String header;
    private String text;
    @ManyToMany
    @JoinTable(name = "article_tag",
            joinColumns = {@JoinColumn(name = "article_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")})
    private Set<DBTag> tags = new HashSet<>();


}
