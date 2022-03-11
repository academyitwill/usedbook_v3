package com.lotu_us.usedbook.repository;

import com.lotu_us.usedbook.domain.entity.Item;
import com.lotu_us.usedbook.domain.enums.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {

    List<Item> findAllByIdIn(List<Long> itemIds);

    @Query("select i from Item i where i.category = :category")
    List<Item> findAllByCategory(@Param("category") Category category, Pageable pageable);
}
