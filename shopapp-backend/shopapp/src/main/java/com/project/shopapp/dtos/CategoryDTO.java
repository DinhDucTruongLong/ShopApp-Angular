package com.project.shopapp.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data // toString de hien thi tt chi tiet cua 1 doi tuong
@Setter
@Getter
//@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    @NotEmpty(message = "can not empty") // ko cho de trong
    private String name;

    public String getName() {
        return name;
    }

    public CategoryDTO(String name) {
        this.name = name;
    }
}
