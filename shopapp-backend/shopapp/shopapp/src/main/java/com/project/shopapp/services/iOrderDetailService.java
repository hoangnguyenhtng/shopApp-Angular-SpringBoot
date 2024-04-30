package com.project.shopapp.services;


import com.project.shopapp.dtos.OrderDetailDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.OrderDetail;

import java.util.List;

public interface iOrderDetailService {
    OrderDetail createOrderDetail (OrderDetailDTO newOrderDetailDTO) throws Exception;
    OrderDetail getOrderDetail(Long id) throws DataNotFoundException;
    OrderDetail updateOrderDetail(Long id, OrderDetailDTO newOrderDetailData);
    void deleteOrderDetail(Long id);
    List<OrderDetail> findByOrderId(Long orderId);

}
