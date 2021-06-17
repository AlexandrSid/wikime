package org.aleksid.wikime.model;

import com.google.gson.Gson;
import org.aleksid.wikime.dto.DBArticle;
import lombok.*;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import java.util.*;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class Article {
    @EqualsAndHashCode.Exclude
    private int id;

    private Set<Tag> tags;
    private String header;
    private List<String> paragraphs;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")

    private User author;

    public String getAuthorName(){
        return (author != null)?author.getUsername():"<none>";
    }

    public Article(Set<Tag> tags, String header, List<String> paragraphs) {
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

    public Article(DBArticle dbArticle) {
        this.id = dbArticle.getId().intValue();
        this.header = dbArticle.getHeader();
        this.paragraphs = Arrays.asList(new Gson().fromJson(dbArticle.getText(), String[].class));
        this.tags = dbArticle.getTags().stream().map(Tag::new).collect(Collectors.toSet());
        this.author = dbArticle.getAuthor();
    }
}
