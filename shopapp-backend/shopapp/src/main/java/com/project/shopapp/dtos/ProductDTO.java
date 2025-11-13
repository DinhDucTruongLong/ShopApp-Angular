package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data // toString de hien thi tt chi tiet cua 1 doi tuong
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {
    @NotBlank(message = "yêu cầu nhập tên")// thong bao bat buoc nhap name
    @Size(min = 3 ,max = 200, message = "Tên nhập ít nhất 3 kí tự dài nhất 200 kí tự")
    private  String name;

    @Min(value = 0, message = "giá tiền lớn hơn ỏ bang 0")
    @Max(value = 10000000, message = " tối đa 10.000.000")

    private Float price;

    private String thumbnail;

    private String description;

    @JsonProperty("category_id")
    private Long categoryId;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



}
