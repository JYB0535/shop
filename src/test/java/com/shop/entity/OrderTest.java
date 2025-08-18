package com.shop.entity;

import com.shop.constant.ItemSellStatus;
import com.shop.repository.item.ItemRepository;
import com.shop.repository.MemberRepository;
import com.shop.repository.OrderItemRepository;
import com.shop.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class OrderTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @PersistenceContext
    EntityManager em;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    public Item createItem() {
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10_000);
        item.setItemDetail("상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        return item;
    }

    @Test
    @DisplayName("고아 객체 제거 테스트")
    public void orphanRemovalTest() {
        Order order = createOrder();
        order.getOrderItems().remove(0); //오더가 부모 오더 아이템이 자식 오더에서 인덱스 0번째인거 없앴으니 오더 아이템에 있는 거는 고아 객체가 됐음
        em.flush();
    }

    @Test
    @DisplayName("지연 로딩 테스트") //오더 아이템만 조회해도 연관된게 다 즉시 조회되어야함 //멤버는 왜 조인이 됐을까? 어디서 튀어나왔을까? 왜? 오더 아이템에는 멤버 필드 없음 아이템이랑 오더는 조회하는거 그렇다 치고 얘네 안에 있느 ㄴ연관관계는? 오더 안에 연관관계로 멤버가 잇음 얘는 many to one이라 디폴트값이 즉시로딩임
    public void lazyLoadingTest() {
        Order order = createOrder(); //아이템 3개 오더아이템 3개 오더1개 멤버 1개 갖춰진 더미데이터 생성
        OrderItem orderItem = order.getOrderItems().get(0);
        Long orderItemId = orderItem.getId();

        em.flush(); //쿼리 날아가는거 봐야해서 1차 캐시 비워줌
        em.clear();

        OrderItem savedOrderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(EntityNotFoundException::new);

        System.out.println("Order class :" + savedOrderItem.getOrder().getClass());
        //오더아이템에가서 @manytoone두개있는거에 레이지 속성 달아줌
        System.out.println("=====================================");
        savedOrderItem.getOrder().getOrderDate(); //오더아이템에서 오더에 접근에서 오더에만 있는 필드에 접근? 지연로딩이라서 프록시 들어있다가 이제서야 쿼리 날아감
        System.out.println("=====================================");
        savedOrderItem.getItem().getItemDetail(); //오더아이템에서 아이템에 접근에서 아이템에만 있는 필드에 접근? 쿼리가 트랜잭션으로 다 묶여있어서 결국은 다 나와야 한다? 멤버는 패치타입 레이지가 없어서 조인 나옴
        //여러번 하니 조인쿼리가 안 나옴 캐싱된거 같다고 함.. 정확한거는 모르겠음
        //지연로딩이라고 값을 비워두면 널 포인터 exception이 날 수 있으니 그 자리르 ㄹ프록시로 채워둠 그러고 있다가 사용한다고 감지되면 그때 프록시에서 쿼리 날림
    }



    @Test
    @DisplayName("영속성 전이 테스트")
    public void cascadeTest() {
        //1. 주문 엔티티 생성
        Order order = new Order(); //주문을 생성
        //orderRepository.save(order);
        //for문의 역할
        //아이템 기준정보 생성, 그 기준정보로 주문-아이템 생성
        for (int i = 0; i < 3; i++) {
            //2. 아이템 엔티티 저장 (아이템이 있어야 주문 엔티티를 구성할 수 있으니까)
            Item item = createItem(); //크리에이트 아이템하면 아이템 하나 줌
            itemRepository.save(item); //아이템 저장
            //3. 위에서 저장된 아이템으로 주문-아이템 (주문과 특정 아이템을 연결해주는 역할) 저장
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setOrder(order); //오더 아이템이 가진 fk 두개
            orderItem.setCount(10);
            orderItem.setOrderPrice(1_000);
            //orderItemRepository.save(orderItem)
            /* 현재 Order는 저장이 안됨, order 엔티티의 orderItems 리스트에 orderItem 엔티티를 추가 */
            //빈 리스트 객체 가져오고 //여기다가 값 담아줌
            order.getOrderItems().add(orderItem);
        }



        /* 지금 Order는 저장 전에 OrderItem 엔티티를 3개 포함한 상태 */
        //최종 저장은 오더 아이템이 아니라 오더임

        orderRepository.saveAndFlush(order); //orderitem은 자식?이라서 부모가 먼저 있어야한다? 그래서 order만들고 order아이템 3번 넣음
        em.clear(); //이렇게 해야 한 번 더 조회했을때 쿼리가 날아간다?

        Order savedOrder = orderRepository.findById(order.getId())
                .orElseThrow(EntityNotFoundException::new);
        //조회된 오더에서 오더아이템즈에서 사이즈를 꺼냄 getOrderItems.size에서 조인 일어남?
        assertEquals(3, savedOrder.getOrderItems().size());
    }

    public Order createOrder() { //어떤 유저가 무슨 물건 주문했는지 반환?
        Order order = new Order();
        for (int i = 0; i < 3; i++) {
            //2. 아이템 엔티티 저장 (아이템이 있어야 주문 엔티티를 구성할 수 있으니까)
            Item item = createItem(); //크리에이트 아이템하면 아이템 하나 줌 //아이템 포문엣서 3개 만들어주고
            itemRepository.save(item); //아이템 저장
            //3. 위에서 저장된 아이템으로 주문-아이템 (주문과 특정 아이템을 연결해주는 역할) 저장
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setOrder(order); //오더 아이템이 가진 fk 두개
            orderItem.setCount(10);
            orderItem.setOrderPrice(1_000);
            //orderItemRepository.save(orderItem)
            /* 현재 Order는 저장이 안됨, order 엔티티의 orderItems 리스트에 orderItem 엔티티를 추가 */
            //빈 리스트 객체 가져오고 //여기다가 값 담아줌
            order.getOrderItems().add(orderItem);
        }
        Member member = new Member();
        memberRepository.save(member); //이렇게 저장하면 아이디만 있고 나머지는 비어있음

        order.setMember(member);
        orderRepository.save(order); //저장(영속화)
        return order;


    }
}
