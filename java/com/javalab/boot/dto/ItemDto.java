package com.javalab.boot.dto;

import com.javalab.boot.domain.category.Category;
import com.javalab.boot.domain.item.Item;
import com.javalab.boot.domain.user.User;
import lombok.*;
import lombok.extern.log4j.Log4j2;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Log4j2
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ItemDto {
    private long id;
    @NotBlank(message = "상품 이름은 필수 입력 값입니다.")
    private String name;
    @NotBlank(message = "가격은 필수 입력 값입니다.")
    private int price;
    @NotBlank(message = "상품 재고는 필수 입력 값입니다.")
    private int stock;
    private boolean isSoldout;
    private int count; // 팔린 수량
    @NotBlank(message = "상품 설명은 필수 입력 값입니다.")
    private String text;
    //추가-----------------------
    @NotNull(message = "카타고리 입력은 필수 입력 값입니다.")
    private Category categoryId;
    //---------------------------
    private String filename;
    private String filepath;

    private int userId;

    /**
     * Dto -> Entity 변환 메소드
     */
    public static Item dtoToEntity(ItemDto itemDto){
        Item item = Item.builder()
                .name(itemDto.getName())
                .price(itemDto.getPrice())
                .stock(itemDto.getStock())
                .isSoldout(itemDto.isSoldout())
                .count(itemDto.getCount())
                .text(itemDto.getText())
                .category(itemDto.getCategoryId())
                .filename(itemDto.getFilename())
                .filepath(itemDto.getFilepath())
                .build();
        // userId가 있을 경우 User 엔터티를 생성하여 설정
        if (itemDto.getUserId() > 0) {
            User user = new User();
            user.setId(itemDto.getUserId());
            item.setUser(user);

        }

        return item;

    }

}
