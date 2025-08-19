package com.shop.service;

import com.shop.dto.OrderDto;
import com.shop.dto.OrderHistDto;
import com.shop.dto.OrderItemDto;
import com.shop.entity.*;
import com.shop.repository.ItemImgRepository;
import com.shop.repository.MemberRepository;
import com.shop.repository.order.OrderRepository;
import com.shop.repository.item.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor //repository주입받아야해서 이거 필요함
public class OrderService {

    private final ItemRepository itemRepository; //아이템 엔티티를 조회하기 때문에 이거 필요
    private final MemberRepository memberRepository; //유저 엔티티도 조회해야해서 이거 필요
    private final OrderRepository orderRepository;
    private final ItemImgRepository itemImgRepository;


    //오더 아이템 리포지토리는 필요없나? ==> 머 캐스케이드 어쩌고 해서 말햇는데 기억 안남

    public Long order(OrderDto orderDto, String email) {
        /*
        1. orderDto.itemId ==> 아이템 엔티티 조회
        2. email ==> 유저 엔티티 조회
        3. 오더 아이템 엔티티 생성
        4. 오더 엔티티 생성 ==> 오더 엔티티.orderItems(List)에 오더 아이템 엔티티 추가
        5. 오더 엔티티 저장 ==> 오더 아이템 엔티티 저장

         */

        //1.
        Item item = itemRepository.findById(orderDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);

        Member member = memberRepository.findByEmail(email); //리포지토리에 추상 메서드로 선언된 쿼리 메서드??



        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount()); //스태틱 메서드로 만들어놨었다?? 기억이 안 남.
        orderItemList.add(orderItem);

                                            //이거 받아야하는 타입이 리스트임 값이 1개만 나오더라도 타입 맞추기 위해서 위에 리스트 생성함
        Order order = Order.createOrder(member, orderItemList ); //오더 엔티티 생성, 스태틱 메서드 만들어둔거 있다?

        orderRepository.save(order);
        return order.getId();
    }

    @Transactional(readOnly = true)
    public Page<OrderHistDto> getOrderHist(String email, Pageable pageable) { //pageable 쓸때 import가 import org.springframework.data.domain.Pageable; 맞는지 항상 확인

        //아까 dsl로 만든 메서드들 호출
        List<Order> orders = orderRepository.findOrders(email, pageable);
        Long totalCount = orderRepository.countOrder(email);

        List<OrderHistDto> orderHistDtoList = new ArrayList<>();

        //이거 한 사이클 돌아야 오더히스트 디티오가 한개 만들어짐 (주문 내역 안에는 여러 품목이 존재
        for(Order order : orders) {
            OrderHistDto orderHistDto = new OrderHistDto(order);
            List<OrderItem> orderItems = order.getOrderItems();
            for(OrderItem orderItem : orderItems) {
                ItemImg itemImg = itemImgRepository.findByItemIdAndRepImgYn(
                        //1.아이템 아이디               //대표 이미지만 골라냄
                        orderItem.getItem().getId(), "Y");
                OrderItemDto orderItemDto = new OrderItemDto(orderItem, itemImg.getImgUrl());
                orderHistDto.addOrderItemDto(orderItemDto);

            }
            orderHistDtoList.add(orderHistDto);
        }
        return new PageImpl<>(orderHistDtoList, pageable, totalCount); //페이지 객체 만들어서 반환
    }
}
