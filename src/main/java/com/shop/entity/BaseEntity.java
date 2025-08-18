package com.shop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import java.time.LocalDateTime;

@EntityListeners(AbstractMethodError.class)
@MappedSuperclass
@Getter
public abstract class BaseEntity extends BaseTimeEntity{

    @CreatedBy
    @Column(updatable = false) //얘도 값이 안 바뀌어야 의미있는 거라서 이거 달아줌
    private String createdBy; //생성자

    @LastModifiedBy
    private String modifiedBy; //수정자






}
