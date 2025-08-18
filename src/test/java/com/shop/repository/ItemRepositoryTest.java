package com.shop.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.constant.ItemSellStatus;
import com.shop.entity.Item;
import com.shop.repository.item.ItemRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;

import static com.shop.entity.QItem.item; //만들어진 Qitem import static 붙이면 필드를 바로 import함
//import  com.shop.entity.QItem; 원형 . 위에 처럼 쓰면 코드 한줄 줄여준다?

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class ItemRepositoryTest {

    @PersistenceContext
    EntityManager em; //jpa가 아니라서 뭐 직접?뭐라더라

    @Autowired
    private ItemRepository itemRepository;

    @Test
    @DisplayName("상품 저장 테스트")
    public void createItemTest() {
        // DB에 값 저장하기(INSERT)하기
        //1. 엔티티(Entity) 객체를 만든다.
        //2. 엔티티 객체에 저장하고 싶은 값을 담는다.
        //3. JPA Repository를 이용해 저장(save ==> persist + flush)한다.
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10_000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        Item savedItem = itemRepository.save(item);
        System.out.println(savedItem);
    }

    @Test
    @DisplayName("상품명 조회 테스트")
    public void findByItemNmTest() {
        createDummyItems();
        //인메모리 db는 일회성이라 이거 10번 반복해도 데이터가 안 쌓여서 받아야함
        List<Item> items = itemRepository.findByItemNm("테스트 상품1");
        for (Item item : items) {
            System.out.println(item);        //출력결과는 1번만 나오면 된다.
        }
   }

    public void createDummyItems() {
        for(int i=1;i<=10;i++){
            Item item = new Item();
            item.setItemNm("테스트 상품" + i);
            item.setPrice(10_000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100 + i);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            //굳이 받아봤자 사용이 안된다 savedItem은
            //Item savedItem = itemRepository.save(item);
            itemRepository.save(item);
        }
    }

    @Test
    @DisplayName("상품명, 상품상세설명 or 테스트")
    public void findByItemNmOrItemDetailTest() {
        createDummyItems();
        createDummyItems();
        List<Item> items = itemRepository.findByItemNmOrItemDetail("테스트 상품1", "테스트 상품 상세 설명5");
        for (Item item : items) {
            System.out.println(item);
        }
    }

    @Test
    @DisplayName("가격 LessThan 테스트")
    public void findByItemNmLessThanTest() {
        createDummyItems();
        List<Item> items = itemRepository.findByPriceLessThan(10_005);
        for (Item item : items) {
            System.out.println(item);
        }
    }

    @Test
    @DisplayName("가격 내림차순 조회 테스트")
    public void findByItemNmGreaterThanTest() {
        createDummyItems();
        List<Item> items = itemRepository.findByPriceLessThanOrderByPriceDesc(10_005);
        for (Item item : items) {
            System.out.println(item);
        }
    }

    @Test
    @DisplayName("@Query를 이용한 상품 조회 테스트")
    public void findByItemDetailTest() {
        createDummyItems();
        List<Item> items = itemRepository.findByItemDetail("테스트 상품 상세 설명");
        for (Item item : items) {
            System.out.println(item);
        }
    }

    @Test
    @DisplayName("@Query를 이용한 상품 조회 테스트 By Native")
    public void findByItemDetailTestByNative() {
        createDummyItems();
        List<Item> items = itemRepository.findByItemDetailByNative("테스트 상품 상세 설명");
        for (Item item : items) {
            System.out.println(item);
        }
    }

    @Test
    @DisplayName("QueryDSL 조회 테스트1")
    public void queryDSLTest() {
        createDummyItems();
        /*
        SELECT *
        FROM item
        WHERE item_sell_status = sell
        AND item_detail LIKE %테스트 상품 상세 설명%
        ORDER BY price DESC
        */

        JPAQueryFactory queryFactory = new JPAQueryFactory(em); //엔티티 매니저를 제공하고 쿼리 팩토리를 받는다?
        JPAQuery<Item> query = queryFactory.selectFrom(item)    /* SELECT * FROM item 까지 쓴거 */
                .where(item.itemSellStatus.eq(ItemSellStatus.SELL)) //eq는 같다.
                .where(item.itemDetail.like("%" + "테스트 상품 상세 설명" + "%"))
                .orderBy(item.price.desc());

        List<Item> items = query.fetch();
        for (Item item : items) {
            System.out.println(item);
        }
    }

    public void createDummyItems2(){
        for(int i=1; i<=5; i++) {
            Item item = new Item();
            item.setItemNm("테스트 상품" + i);
            item.setPrice(10_000 + i);
            item.setItemDetail("테스트 상품 상세 설명");
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100 + i);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            itemRepository.save(item);
        }

        for(int i=6; i<=10; i++) {
            Item item = new Item();
            item.setItemNm("테스트 상품" + i);
            item.setPrice(10_000 + i);
            item.setItemDetail("테스트 상품 상세 설명");
            item.setItemSellStatus(ItemSellStatus.SOLD_OUT);
            item.setStockNumber(0);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            itemRepository.save(item);

        }

    }

    @Test
    @DisplayName("상품 QueryDSL 조회 테스트2")
    public void queryDSLTest2() {
        createDummyItems2();
        //검색 조건
        String itemDetail = "테스트 상품 상세 설명";
        int price = 10_003;
        String itemSellStatus = "SELL"; //문자로 데이터가 넘어옴 클라이언트 쪽에서
        int pageNum = 1;

        Pageable pageable = PageRequest.of(pageNum-1, 5);

        /*
        조건1. 주어진 itemDetail 키워드를 포함
        조건2. 상품 가격이 주어진 price 보다 큰
        조건3. 조회하려는 상태가 SELL 인 경우 상품의 판매 상태가 SELL인 경우 (검색 + 필터 기능
        조건4. 한 페이지 당 5개씩 페이징된 데이터를 조회
        */
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        JPAQuery<Item> baseQuery = queryFactory.selectFrom(item);
        //where절 들어갈 조건을 따로 떼서 가져올 수도 잇다
        //이 위에까지는 쿼리가
        /*
        SELECT * FROM item 까지 작성 된거
         */
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(item.itemDetail.like("%" + itemDetail + "%"));
        booleanBuilder.and(item.price.gt(price));
        if(itemSellStatus.equals("SELL")) {
            booleanBuilder.and(item.itemSellStatus.eq(ItemSellStatus.SELL));
        }
        JPAQuery<Item> conditionedQuery = baseQuery.where(booleanBuilder);

        JPAQuery<Item> pagedQuery = conditionedQuery
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        /*
        SELECT * FROM item
        WHERE itemDetail LIKE ?
        AND price > ?
        ANE item_sell_status = "SELL" <<-- 조건부
        ORDER BY id DESC
        LIMIT 5 OFFSET ?
        */

        //1. 몇번 페이지를 보겠다
        //2. 몇개씩 보겠다.
        //3.
        /* SELECT count(*) FROM item */
        List<Item> contents = pagedQuery.fetch();
        Long totalCount = queryFactory.select(Wildcard.count)
                .from(item)
                .where(booleanBuilder)
                .fetchOne();
        Page<Item> result = new PageImpl<>(contents, pageable, totalCount);

        System.out.println("총 컨텐츠 요소의 수 : " + result.getTotalElements());
        System.out.println("조회가능한 총 페이지 수 : " + result.getTotalPages());
        List<Item> items = result.getContent();
        for (Item item : items) {
            System.out.println(item);
        }

    }
}
