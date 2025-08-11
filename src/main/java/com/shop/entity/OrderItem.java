package com.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

//아이템이랑 오더 묶어주는 테이블
@Entity
@Table(name = "order_item")
@Getter
@Setter
public class OrderItem {

    @Id
    @Column(name = "order_item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id") //item의 pk명
    private Item item;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private Integer orderPrice; //생략하면 알아서 낙타 표기법이 스네이크 표기법으로 바뀐 컬럼 생성된다.

    private Integer count;

    private LocalDateTime regTime;

    private LocalDateTime updateTime;
}
