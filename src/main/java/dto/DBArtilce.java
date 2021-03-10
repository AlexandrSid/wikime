package dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity(name = "ARTICLES")
public class DBArtilce {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String header;
    private String text;
    @ManyToMany
    @JoinTable(name = "article_tag",
            joinColumns = {@JoinColumn(name = "artilce_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")})
    private Set<DBTag> tags;

}
