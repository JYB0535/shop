package com.shop.repository.order;

import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.shop.entity.QOrder.order;

@RequiredArgsConstructor
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public List<Order> findOrders(String email, Pageable pageable) {
        /*
        SELECT *
        FROM orders o
        INNER JOIN member m //m은 엘리야스
        ON o.member_id = m.member_id
        WHERE m.member_id = ?
        ORDER BY o.order_date DESC
         */

//        List<Order> orders = jpaQueryFactory
//                .select(order)
//                .from(order)
//                .innerJoin(order.member)
//                .where(order.member.email.eq(email))
//                .orderBy(order.orderDate.desc())
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
//
//        return orders;

        //바로 위에거 이렇게 써도 된다.
        return jpaQueryFactory
                .select(order)
                .from(order)
                .innerJoin(order.member)
                .where(order.member.email.eq(email))
                .orderBy(order.orderDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();



    }

    @Override
    public Long countOrder(String email) {

        //쿼리 교육용 말고는 dsl로 하거나 mybatis 로만 하라함
        /*
        SELECT count(*)
        FROM orders
        INNER JOIN member m
        ON o.member_id = m.member_id
        WHERE m.email=?
         */

        Long total = jpaQueryFactory
                .select(Wildcard.count)
                .from(order)
                .innerJoin(order.member)
                .where(order.member.email.eq(email))
                .fetchOne();

        //레코드가 하나도 없으면 null이 나올 수 있기 떄문에 optional로 처리
        Optional<Long> totalCount = Optional.ofNullable(total);

        return totalCount.orElse(0L);
    }
}
