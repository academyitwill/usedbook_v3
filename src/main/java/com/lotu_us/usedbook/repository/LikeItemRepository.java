package com.lotu_us.usedbook.repository;

import com.lotu_us.usedbook.domain.entity.LikeItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeItemRepository extends JpaRepository<LikeItem, Long> {


}
