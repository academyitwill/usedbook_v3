package com.lotu_us.usedbook.repository;

import com.lotu_us.usedbook.domain.entity.OrderBasket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderBasketRepository extends JpaRepository<OrderBasket, Long> {

    Optional<OrderBasket> findByMemberIdAndItemId(Long memberId, Long itemId);

    void deleteByMemberIdAndItemId(Long memberId, Long itemId);

    List<OrderBasket> findAllByMemberId(Long id);

    List<OrderBasket> findAllByItemIdIn(List itemIdList);
}
