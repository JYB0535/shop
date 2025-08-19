package com.shop.dto;

import com.shop.constant.OrderStatus;
import com.shop.entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderHistDto { //화면에 표시될거

    //생성자
    public OrderHistDto(Order order) { //오더 엔티티에 밑에 있는 것들이 있어서 활용
        this.orderId = order.getId();
        this.orderDate = order.getOrderDate().format(//날짜 타입인거 문자 타입으로 변환
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        );
        this.orderStatus = order.getOrderStatus();
    }

    private List<OrderItemDto> orderItemDtoList = new ArrayList<>(); //아까 만든거 리스트로 받아야 한 주문에 여러개 상품 산거 가능
    private Long orderId;
    private String orderDate; //날짜인데 왜 스트링으로 했을까? 오더 엔티티를 보면 다시 들고와도 LocalDateTime임 왜 여기는 스트링이니? 그냥 데이트객체 그대로 내려서 자바 스크림트로
    //이래저래 해도 되는데 꼭 화면에 나올때 얘가 꼭 날짜일 필요는 없으니까 고려해야 할 점? 자바스크립트의 날짜와 자바의 날짜는 다름 즉 숫자와 문자 제외하고는 공유 안하는게 좋다.
    private OrderStatus orderStatus;

    public void addOrderItemDto(OrderItemDto orderItemDto) {
        this.orderItemDtoList.add(orderItemDto);
    }






}
