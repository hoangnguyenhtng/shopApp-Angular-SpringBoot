package com.project.shopapp.controllers;

import com.project.shopapp.dtos.OrderDetailDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order_details")
public class OrderDetailController {
    @PostMapping
    public ResponseEntity<?> createOrderDetail(
            @Valid @RequestBody OrderDetailDTO newOrderDetail
    ) {
        return ResponseEntity.ok("Create Order Detail");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(
            @Valid @PathVariable("id") Long id) {
        return ResponseEntity.ok("Get order detail with ID " + id
        );
    }

    @GetMapping("/order/{orderId}")
    //lay danh sach order detail cua 1 order
    public ResponseEntity<?> getOrderDetails(@Valid @PathVariable("orderId") Long orderId) {
        return ResponseEntity.ok("order Details with id " + orderId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(
            @Valid @PathVariable("id") Long id,
            @RequestBody OrderDetailDTO newOrderDetailData
    ) {
        return ResponseEntity.ok("update Order Detail with ID = " + id + ", new Order Detail Data: " + newOrderDetailData);
    }

    @DeleteMapping("/{id}")
    //Bat buoc tham so truyen vao phai la Void hoac Object
    public ResponseEntity<Void> deleteOrderDetails(@Valid @PathVariable("id") Long id) {
        return ResponseEntity.noContent().build();
    }
}
