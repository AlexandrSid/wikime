package org.aleksid.wikime.repository;

import org.aleksid.wikime.dto.DBArticle;
import org.aleksid.wikime.dto.DBTag;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ArticleRepo extends CrudRepository<DBArticle, Long> {
    List<DBArticle> findAllByTagsIn(List<DBTag> tags);
}
