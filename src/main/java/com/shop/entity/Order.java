package com.shop.entity;

import com.shop.constant.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "orders") //sql에는 order by가 있어서 테이블 이름 order로는 못함
@Getter
@Setter
public class Order extends BaseEntity {
    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    //어떤 멤버가 한 주문인지?
    @ManyToOne(fetch = FetchType.LAZY) //(To 앞에 있는게 public class에 있는 Order클래스) 뒤에 있는게 흰글씨 멤버 클래스
    @JoinColumn(name = "member_id") //멤버의 pk는 멤버 아이디
    private Member member;

    //아까는 카트가 멤버 아이디 갖고 있어서 카트 조회하면 멤버 조회됏는데 이번에는 오더 조회해도 오더 아이템 fk가 없어서 조회가 안 된다. 그래서 방법이 one to many?
    //얘는 데이터베이스 보다는 JPA 개념
    //@OneToMany
    //일대 다
    //일 : Order
    //다 : OrderItem
    @OneToMany(mappedBy = "order", //원투매니는 원래 패치 타입이 레이지 인데 명확하게 그냥 써준다
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    ) //얘는 읽기 전용 왜? => DB 에서 표현 불가 DB 에서 표현되는 컬럼이 아니라 그냥 오더랑 연관있는 오더 아이템을 읽어주는것 디비에 아무리 찾아봐도 오더에 오더 아이템은 없다
    List<OrderItem> orderItems = new ArrayList<>(); //오더 하나당 오더 아이템이 여러개일수도 있는데 그냥 OrderItem orderItems 하면 1개여서 안 된다 그래서 리스트로 받아야함 그냥 널 보다는 빈 리스트라도 있는게 나을거같아서 바로 초기화해줌  new ArrayList<>();
    //mappedBy = "order" 쓰는 이유? 그냥 매핑만 하는거라고 order는 필드명 orderItems는 그냥 order를 매핑만 한거다? orderItem에 오더라는 필드의 필드명
    //양방향 매핑? 오더 아이템에서는 다대일로 단방향
    //오더에서도.
    //db에서 하는 관계 설정은 fk만 가능한거고 이거는 그냥 자바에서만 잇는거
    //mappedBy로 주인 설정 fk를 실제로 갖고 fk를 조작할 수 있는 쪽 mappedBy에 오더 아이템에 있는 오더 필드 명을 써준거임 오더 아이템에 있는 오더에서 매핑만 해온거다
    //다대다 매핑은 반드시 중간 테이블이 필요하다?
    //JPA 다대다는 잘 사용 안함.

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; //enum 타입  넣어줄때 @Enumerated 넣어줘야함

    //프라이빗으로 한 이유는 이 안에서만 호출할거라 명확하게 하려고
    private void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.setOrder(this); //오더 아이템의 오더 필드도 현재 오더로 세팅해준다? 서로 연관 시킨다? fk 쪽에도 오더 넣어주고 오더에서 참조하는 오더 아이템에도 추가해준다.

    }

    public static Order createOrder(Member member, List<OrderItem> orderItemList) { //누가 주문했는지 필요해서 멤버를 매개변수로 받고 누가 어떤 품목 몇개씩 주문했다 오더 아이템 리스트, 단일 오더 아이템으로 안 받는 이유? 나중에 장바구니 만들어야하니까 그냥 여러개 받을 수 있는거로 만든다.
        Order order = new Order();
        order.setMember(member);
        for (OrderItem orderItem : orderItemList) { //밖ㅇ에서 들어온거 채우는거
            order.addOrderItem(orderItem);
        }

        //고정 값
        order.setOrderStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getOrderPrice();
        }
        return totalPrice;
    }
//
//    private LocalDateTime regTime;
//
//    private LocalDateTime updateTime;


}
