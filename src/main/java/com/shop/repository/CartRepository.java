package com.shop.repository;

import com.shop.entity.Cart;
import com.shop.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
                                                            //pk의 자바 타입
public interface CartRepository extends JpaRepository<Cart, Long> {

     Cart findByMember(Member member);
}
