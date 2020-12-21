package model;

import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class Article {
    @EqualsAndHashCode.Exclude
    private int id;

    private Set<aTag> tags;
    private String header;
    private List<String> paragraphs;

    public Article(Set<aTag> tags, String header, List<String> paragraphs) {
        this.tags = tags;
        this.header = header;
        this.paragraphs = paragraphs;
        this.id = this.hashCode();
    }
}
