package com.epam.esm.repository;

import com.epam.esm.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepo extends JpaRepository<Tag, Long> {

    @Query(value = "WITH s AS ( SELECT id, name, create_date, last_update_date FROM tag WHERE name = :tagName ), " +
            " i AS (INSERT INTO tag(name, create_date, last_update_date) SELECT :tagName, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP WHERE NOT EXISTS (SELECT 1 FROM s)" +
            " RETURNING id, name, create_date, last_update_date) "  +
            " SELECT id, name, create_date, last_update_date FROM i UNION ALL SELECT id, name, create_date, last_update_date FROM s;",
            nativeQuery = true)
    Tag saveTagByNameIfNotExists(@Param("tagName") String tagName);

    @Query(value = "SELECT * FROM tag t WHERE t.id = ( " +
            "SELECT gct.tag_id FROM gift_certificate_tag gct WHERE gct.tag_id IN " +
            "(SELECT ord.gift_certificate_id FROM orders ord WHERE ord.user_id = " +
            "(SELECT o.user_id FROM orders o GROUP BY o.user_id ORDER BY SUM(o.price) DESC LIMIT 1)) " +
            "GROUP BY gct.tag_id ORDER BY COUNT(*) DESC LIMIT 1);", nativeQuery = true)
    Optional<Tag> findMostUsedTag();
}
