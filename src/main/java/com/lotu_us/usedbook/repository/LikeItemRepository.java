package com.lotu_us.usedbook.repository;

import com.lotu_us.usedbook.domain.entity.LikeItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeItemRepository extends JpaRepository<LikeItem, Long> {

    @Modifying
    void deleteByMemberIdAndItemId(@Param("memberId") Long memberId, @Param("itemId") Long itemId);

    List<LikeItem> findAllByMemberId(@Param("memberId") Long memberId);
}
