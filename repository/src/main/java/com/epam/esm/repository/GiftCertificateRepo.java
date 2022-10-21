package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GiftCertificateRepo extends JpaRepository<GiftCertificate, Long> {
    @Query("SELECT g FROM GiftCertificate g INNER JOIN g.tags t GROUP BY g " +
            "HAVING SUM (CASE WHEN t.name in (:tagNameList) then 1 else 0 end) = :tagListLength")
    Page<GiftCertificate> getPageBySearchingTermAndSort(
            @Param("tagNameList") List<String> tagNameList,
            @Param("tagListLength") long len,
            Pageable pageable
    );

    Page<GiftCertificate> searchGiftCertificateByNameContainingOrDescriptionContaining(String name, String description, Pageable pageable);

}
