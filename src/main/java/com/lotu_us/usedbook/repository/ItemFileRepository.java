package com.lotu_us.usedbook.repository;

import com.lotu_us.usedbook.domain.entity.ItemFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemFileRepository extends JpaRepository<ItemFile, Long> {
    void deleteByFileName(String fileName);
}
