package com.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

//페이지 196.

@Entity
@Table(name = "cart_item")
@Getter
@Setter
public class CartItem extends BaseEntity {

    @Id
    @Column(name = "cart_item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    //다는 카트 아이템 1은 카트 필드 (흰 글씨)
    @ManyToOne(fetch = FetchType.LAZY) //카트 아이템 쪽이 다이기 때문에
    //아이디 타입 x 엔티티 타입 그대로 써준다?
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id") //부모타입의 pk랑 이름 같아야?한다? 들어가보면  item id
    private Item itme;

    private Integer count; //책에는 그냥 인트라 되어ㅣㅇㅆ는데 엔티티나 디티오같이 객체 필드 선언 할때 어지간하면 래퍼 class로 원시 타입쓰면 불필요하게 박싱 언박싱이나 래퍼타입 들어가야하는 제네릭에는 못 들어가기 때문에 안 사용한다





}
