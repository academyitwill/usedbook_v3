package com.lotu_us.usedbook.repository;

import com.lotu_us.usedbook.domain.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {
    List<Item> findAllByIdIn(List<Long> itemIds);
}
