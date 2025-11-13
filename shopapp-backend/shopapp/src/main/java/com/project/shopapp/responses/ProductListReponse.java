package com.project.shopapp.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductListReponse {
    private List<ProductResponses> products;
    private int totalPages;
}
