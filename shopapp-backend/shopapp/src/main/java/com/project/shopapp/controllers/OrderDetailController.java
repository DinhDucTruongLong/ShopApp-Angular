package com.project.shopapp.controllers;


import com.project.shopapp.dtos.OrderDetailDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.OrderDetail;
import com.project.shopapp.responses.OrderDetailResponse;
import com.project.shopapp.services.OrderDetailsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/order_details")
@RequiredArgsConstructor
public class OrderDetailController {
    private final OrderDetailsService orderDetailsService;
    // them moi 1 orderdetail
    @PostMapping("")
    public ResponseEntity<?> createOrderDetail(
            @Valid @RequestBody OrderDetailDTO orderDetailDTO){
        try {
            OrderDetail newOrderDetail = orderDetailsService.createOrderDetail(orderDetailDTO);
            return ResponseEntity.ok().body(OrderDetailResponse.fromOrderDetail(newOrderDetail));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(
            @Valid @PathVariable long id) throws DataNotFoundException {
        orderDetailsService.getOrderDetail(id);
        OrderDetail orderDetail = orderDetailsService.getOrderDetail(id);
        return ResponseEntity.ok().body(OrderDetailResponse.fromOrderDetail(orderDetail));
        //return ResponseEntity.ok(orderDetail);
    }

    // lay ra danh sach orderdetail tu 1 don hang
    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderDetails(@Valid @PathVariable("orderId") Long orderId
    ){
        List<OrderDetail> orderDetails = orderDetailsService.findByOrderId(orderId);//tra theo 2 kieu
        List<OrderDetailResponse> orderDetailResponses = orderDetails
                .stream()
                .map(OrderDetailResponse::fromOrderDetail)
                .toList();
        return  ResponseEntity.ok(orderDetails);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(
            @Valid @PathVariable("id") Long id,
            @RequestBody OrderDetailDTO orderDetailDTO ){//tra ve chuoi co day du thong tin
        try {
            OrderDetail orderDetail= orderDetailsService.updateOrdeDetail(id, orderDetailDTO);
            return ResponseEntity.ok().body(orderDetail);
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderDetail(
            @Valid @PathVariable("id") Long id){
        orderDetailsService.deleteById(id);
        return ResponseEntity.ok().body("Delete order detail with id: "+ id +" successfully");
       // return ResponseEntity.noContent().build();
    }

}
