package model;

import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.util.List;
import java.util.Objects;
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
        this.id = this.hashCode();//пока так и сохраняется в БД, что при первом апдейте делаает ID нерелевантным, думаю лучше будет сделать генерацию идшника в БД и его возврат при добавлении
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return Objects.equals(tags, article.tags) && header.equals(article.header) && Objects.equals(paragraphs, article.paragraphs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tags, header, paragraphs);
    }
}
