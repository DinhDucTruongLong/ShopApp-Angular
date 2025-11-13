package com.project.shopapp.services;

import com.project.shopapp.dtos.OrderDetailDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.OrderDetail;

import java.util.List;

public interface IOrderDetailsService {

    OrderDetail createOrderDetail(OrderDetailDTO newOrderDetail) throws Exception;

    OrderDetail getOrderDetail(Long id) throws DataNotFoundException;

    OrderDetail updateOrdeDetail(Long id, OrderDetailDTO newOrderDetailData) throws DataNotFoundException;

    void deleteById(Long id);

    List<OrderDetail> findByOrderId(Long orderId);

}
