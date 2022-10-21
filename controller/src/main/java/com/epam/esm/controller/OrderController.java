package com.epam.esm.controller;

import com.epam.esm.dto.response.AppResponseDto;
import com.epam.esm.dto.response.OrderResponseDto;
import com.epam.esm.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private static final String PAGE = "0";
    private static final String SIZE = "10";
    private final OrderService orderService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponseDto<OrderResponseDto>> create(
            @RequestParam("user_id") Long userId, @RequestParam("certificate_id") Long certificateId
    ){
        return ResponseEntity.ok(orderService.create(userId, certificateId));
    }

    @GetMapping(value ="/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponseDto<OrderResponseDto>> get(
            @PathVariable("id") Long id
    ){
        return ResponseEntity.ok(orderService.get(id));
    }

    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponseDto<List<OrderResponseDto>>> get(
            @RequestParam(required = false, name = "page", defaultValue = PAGE) int page,
            @RequestParam(required = false, name = "size", defaultValue = SIZE) int size
    ){
        return ResponseEntity.ok(orderService.getPage(page, size));
    }
}
