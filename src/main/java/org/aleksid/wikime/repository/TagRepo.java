package org.aleksid.wikime.repository;

import org.aleksid.wikime.dto.DBTag;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

public interface TagRepo extends CrudRepository<DBTag, Long> {
    List<DBTag> findAllByTagIn(List<String> tags);
}
