package com.lotu_us.usedbook.repository;

import com.lotu_us.usedbook.domain.dto.ItemDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {
    Page<ItemDTO.ListResponse> findAllWithSearch(String category, String search, Pageable pageable);
}
