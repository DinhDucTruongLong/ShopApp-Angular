package com.project.shopapp.services;

import com.project.shopapp.dtos.OrderDetailDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.Order;
import com.project.shopapp.models.OrderDetail;
import com.project.shopapp.models.Product;
import com.project.shopapp.repositories.OrderDetailRepository;
import com.project.shopapp.repositories.OrderRepository;
import com.project.shopapp.repositories.ProductRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service

public class OrderDetailsService implements IOrderDetailsService{
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;
    @Override
    public OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception {
        // tim xem orderId co ton tai hay ko
        Order order = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(()-> new DataNotFoundException(
                        "cannot find Order with id: "+orderDetailDTO.getOrderId()));
        // tim product theo id
        Product product = productRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(()-> new DataNotFoundException(
                        "cannot find Order with id: "+orderDetailDTO.getOrderId()));
        // xet doi tuong order moiw
        OrderDetail orderDetail = OrderDetail.builder()
                .order(order)
                .product(product)
                .numberOfProduct(orderDetailDTO.getNumberOfProducts())
                .price(orderDetailDTO.getPrice())
                .totalMoney(orderDetailDTO.getTotalMoney())
                .color(orderDetailDTO.getColor())
                .build();
        //luu vao db
        return orderDetailRepository.save(orderDetail);

    }

    @Override
    public OrderDetail getOrderDetail(Long id) throws DataNotFoundException {
        return orderDetailRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException("cannot find orderdetail with id: "+id));
    }

    @Override
    public OrderDetail updateOrdeDetail(Long id, OrderDetailDTO orderDetailDTO)
            throws DataNotFoundException {
        // tim xem orderdetail co ton tai hay ko
        OrderDetail existingOrderDetail = orderDetailRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException("Cannot find order detail with id: "+ id));
        Order existingOrder = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(()-> new DataNotFoundException("Cannot find order with id: "+id));
        Product existingProduct = productRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(()-> new DataNotFoundException(
                        "cannot find Order with id: "+orderDetailDTO.getOrderId()));
        existingOrderDetail.setPrice(orderDetailDTO.getPrice());
        existingOrderDetail.setNumberOfProduct(orderDetailDTO.getNumberOfProducts());
        existingOrderDetail.setTotalMoney(orderDetailDTO.getTotalMoney());
        existingOrderDetail.setColor(orderDetailDTO.getColor());
        existingOrderDetail.setOrder(existingOrder);
        existingOrderDetail.setProduct(existingProduct);// mua san nay roi muon doi sp khac
        return orderDetailRepository.save(existingOrderDetail);
    }

    @Override
    public void deleteById(Long id) {
        // xoa cung phan nay
        orderDetailRepository.deleteById(id);

    }

    @Override
    public List<OrderDetail> findByOrderId(Long orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }
}
