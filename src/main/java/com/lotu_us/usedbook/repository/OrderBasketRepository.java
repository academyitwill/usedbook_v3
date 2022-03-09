package com.lotu_us.usedbook.repository;

import com.lotu_us.usedbook.domain.entity.OrderBasket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderBasketRepository extends JpaRepository<OrderBasket, Long> {
}
