package com.shop.exception;

public class OutOfStockException extends RuntimeException { //exception이 발생할수도 아닐수도 있어서 runtimeException이 적당?
    public OutOfStockException(String message) {
        super(message); //부모 메시지 부름

    }
}
