package com.lotu_us.usedbook.repository;

import com.lotu_us.usedbook.domain.dto.ItemDTO;
import com.lotu_us.usedbook.domain.entity.Item;
import com.lotu_us.usedbook.domain.entity.QItem;
import com.lotu_us.usedbook.domain.enums.Category;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    private QItem item = QItem.item;

    @Override
    public Page<ItemDTO.ListResponse> findAllWithSearch(String category, String search, Pageable pageable) {

        QueryResults<Item> results = jpaQueryFactory
                .select(item)
                .from(item)
                .where(
                        categoryIsExist(category),
                        searchIsExist(search)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(sort(pageable))
                .fetchResults();
        //QueryResults는 페이징 관련 내용이 포함된 타입


        List<ItemDTO.ListResponse> content = new ArrayList<>();
        for (Item resultItem : results.getResults()) {
            content.add(ItemDTO.entityToDtoListResponse(resultItem));
        }

        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    //결과가 null이면 where에서 무시된다.
    private BooleanExpression categoryIsExist(String category){
        if(StringUtils.isBlank(category)){  //null, "", " " 일때 true
            return null;
        }
        return item.category.eq(Category.valueOf(category.toUpperCase()));
    }

    private BooleanExpression searchIsExist(String search) {
        if(search == null){
            return null;
        }

        StringTokenizer st = new StringTokenizer(search,",");
        String type = st.nextToken();
        String value = st.nextToken();

        switch(type){
            case "1" : return item.title.containsIgnoreCase(value);
            case "2" : return item.seller.nickname.containsIgnoreCase(value);
            case "3" : return item.content.containsIgnoreCase(value);
            case "4" : return item.title.containsIgnoreCase(value)
                              .or(item.content.containsIgnoreCase(value));
            default : return null;
        }
    }

    private OrderSpecifier<?> sort(Pageable pageable){
        if(pageable.getSort().isEmpty()){
           return null;
        }

        Sort sort = pageable.getSort();
        String field = "";
        Order direction = null;

        //sort가 여러개일 경우
        for (Sort.Order order : sort) {
            field = order.getProperty();
            direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;

            switch (field){
                case "title" : return new OrderSpecifier(direction, item.title);
                case "saleStatus" : return new OrderSpecifier(direction, item.saleStatus);
                case "seller" : return new OrderSpecifier(direction, item.seller);
                case "createTime" : return new OrderSpecifier(direction, item.createTime);
                case "viewCount" : return new OrderSpecifier(direction, item.viewCount);
            }
        }

        return null;
    }

}
