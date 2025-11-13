package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.*;

@Data
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDTO {

    @JsonProperty("order_id")
    @Min(value = 1, message = "Orders ID must be >0 ")
    private Long orderId;

    @JsonProperty("product_id")
    @Min(value = 1, message = "Product ID must be >0 ")
    private Long productId;

    @Min(value = 0, message = "Price must be >- 0")
    private Float price;

    @JsonProperty("number_of_products")
    @Min(value = 1, message = " number_of_products must be >0 ")
    private int numberOfProducts;

    @Min(value = 0, message = " total_money must be >- 0 ")
    @JsonProperty("total_money")
    private Float totalMoney;

    private String color;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public int getNumberOfProducts() {
        return numberOfProducts;
    }

    public void setNumberOfProducts(int numberOfProducts) {
        this.numberOfProducts = numberOfProducts;
    }

    public Float getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Float totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "OrderDetailDTO{" +
                "orderId=" + orderId +
                ", productId=" + productId +
                ", price=" + price +
                ", numberOfProducts=" + numberOfProducts +
                ", totalMoney=" + totalMoney +
                ", color='" + color + '\'' +
                '}';
    }
}
