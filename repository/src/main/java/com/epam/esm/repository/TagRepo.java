package com.epam.esm.repository;

import com.epam.esm.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepo extends JpaRepository<Tag, Long> {

    @Query(value = "WITH s AS ( SELECT id, name, create_date, last_update_date FROM tag WHERE name = :tagName ), " +
            " i AS (INSERT INTO tag(name, create_date, last_update_date) SELECT :tagName, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM s)" +
            " RETURNING id, name, create_date, last_update_date) "  +
            " SELECT id, name, create_date, last_update_date FROM i UNION ALL SELECT id, name, create_date, last_update_date FROM s;",
            nativeQuery = true)
    Tag saveTagByNameIfNotExists(@Param("tagName") String tagName);

    @Query("SELECT t FROM Tag t WHERE t.name IN :name")
    List<Tag> findAllByNameIsIn(@Param("name") Iterable<String> tagNameList);
}
