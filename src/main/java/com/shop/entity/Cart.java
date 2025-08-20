package com.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@Entity
@Table(name = "cart")
@Setter
@Getter
@ToString
public class Cart extends BaseEntity {
    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
    member 테이블의 member_id(PK)를 참조하는 cart 테이블의 member_id(FK)
    @JoinColumn()의 어노테이션에 들어가는 name => fk 컬럼 이름 = 부모 테이블의 PK 컬럼 이름이랑 맞춰줘야한다.
     */
    //카드(One) //흰색글씨로 되어있는 멤버(One)
    @OneToOne(fetch = FetchType.LAZY) //카트 읽을때마다 매번 멤버를 같이 조회할 필요 없이 필요한 경우만 사용할 수 있게 지연 로딩 이거 넣으니까 조인이 안 날아감 카트만 조회해왔는데 member의 주소값이 들어가있다? null이 아니네?
    @JoinColumn(name = "member_id", unique = true)
    private Member member;
    //private Long member_id ==> 이렇게 표현 x fk설정할때는 엔티티타입 그대로 넣어준다

    //카트는 최초 한번만 만들어지면 된다. 있으면 조회 없으면 만들기로 사용될 엔티티
    public static Cart createCart(Member member) {
        Cart cart = new Cart();
        cart.setMember(member);
        return cart;
    }
}
