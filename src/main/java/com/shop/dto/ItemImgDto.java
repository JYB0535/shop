package com.shop.dto;

import com.shop.entity.ItemImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class ItemImgDto {
    //FK 제외하고 필드 구성 따라감
    private Long id;
    private String imgName;
    private String oriImgName;
    private String imgUrl;
    private String repImgYn;

    private static ModelMapper modelMapper = new ModelMapper();

    public static ItemImgDto of(ItemImg itemImg) {

        //이렇게 써도 상관은 없음 쓸때마다 모델매퍼 객체가 만들었다가 없어지긴 하지만
//        ModelMapper mapper = new ModelMapper();
//        return mapper.map(itemImg, ItemImgDto.class);

        //if 100명이 동시에 호출한다? 순식간에 100개가 생겨났다가 순식간에 가비지 컬렉터가 수거해감 이러면 스레드를 팍팍 씀
        //차선 책은? static 필드(정적 필드)로 초기화를 하나 함 저기 위에 씀  private static ModelMapper modelMapper = new ModelMapper();



        return modelMapper.map(itemImg, ItemImgDto.class);

        //modelMapper를 사용해서 엔티티 to DTO 변환된 객체 반환

        //스프링 빈은 내부적으로 싱글톤으로 관리됨(스프링 빈 객체가 1개만 존재)

    }
}
