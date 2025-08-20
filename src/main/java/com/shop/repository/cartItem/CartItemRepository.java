package com.shop.repository.cartItem;

import com.shop.entity.Cart;
import com.shop.entity.CartItem;
import com.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
                                                     //엔티티 타입 //엔티티의 아이디 타입 pk
                                                                            //dsl 쓸거라 추가
public interface CartItemRepository extends JpaRepository<CartItem, Long>, CartItemRepositoryCustom{
    CartItem findByCartAndItem(Cart cart, Item item);
}
