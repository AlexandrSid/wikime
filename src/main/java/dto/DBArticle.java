package dto;

import com.google.gson.Gson;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.Article;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity(name = "ARTICLES")
@Data
@NoArgsConstructor
public class DBArticle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String header;
    private String text;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "article_tag",
            joinColumns = {@JoinColumn(name = "article_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")})
    private Set<DBTag> tags = new HashSet<>();

    public DBArticle(Article article) {
        this.header = article.getHeader();
        this.text = new Gson().toJson(article.getParagraphs());
        this.tags = article.getTags().stream().map(DBTag::new).collect(Collectors.toSet());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DBArticle dbArticle = (DBArticle) o;
        return id == dbArticle.id && header.equals(dbArticle.header) && Objects.equals(text, dbArticle.text);
    }

//Только по ID нельзя, так как в начале его нету и в сете они перетирают друг друга
    @Override
    public int hashCode() {
        return Objects.hash(id, header, text);
    }
}
