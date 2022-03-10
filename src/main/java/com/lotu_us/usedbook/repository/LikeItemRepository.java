package com.lotu_us.usedbook.repository;

import com.lotu_us.usedbook.domain.entity.LikeItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeItemRepository extends JpaRepository<LikeItem, Long> {

    @Modifying
    void deleteByMemberIdAndItemId(@Param("memberId") Long memberId, @Param("itemId") Long itemId);

    Page<LikeItem> findByMemberId(Long memberId, Pageable pageable);

    Optional<LikeItem> findByMemberIdAndItemId(Long memberId, Long itemId);
}
