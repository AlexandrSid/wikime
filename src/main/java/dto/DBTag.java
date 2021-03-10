package dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity(name = "TAGS")
public class DBTag {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String tag;
    @ManyToMany(mappedBy = "tags")
    private Set<DBArtilce> articles;

}
