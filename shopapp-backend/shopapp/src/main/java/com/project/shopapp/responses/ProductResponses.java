package com.project.shopapp.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.models.Product;
import lombok.*;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponses extends BaseReponse{
    //đay là giá trị trả về
    private  String name;

    private Float price;

    private String thumbnail;

    private String description;

    @JsonProperty("category_id")
    private Long categoryId;
    public static ProductResponses fromProduct(Product product){
        ProductResponses productResponses = ProductResponses.builder()
                .name(product.getName())
                .price(product.getPrice())
                .thumbnail(product.getThumbnail())
                .description(product.getDescripstion())
                .categoryId(product.getCategory().getId())
                .build();
        productResponses.setCreateAt(product.getCreateAt());
        productResponses.setUpdateAt(product.getUpdateAt());
        return productResponses;
    }

}
