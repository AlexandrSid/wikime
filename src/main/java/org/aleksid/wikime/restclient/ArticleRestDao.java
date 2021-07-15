package org.aleksid.wikime.restclient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleRestDao {
    private String articleId;
    private String text;
}