package com.epam.esm.controller;

import com.epam.esm.dto.response.AppResponseDto;
import com.epam.esm.dto.response.OrderResponseDto;
import com.epam.esm.link.LinkProvider;
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

    private static final String PAGE = "0"; // default page
    private static final String SIZE = "10"; // default size
    private final OrderService orderService;
    private final LinkProvider linkProvider;

    //Create order with userId and certificateId
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponseDto<OrderResponseDto>> create(
            @RequestParam("user_id") Long userId, @RequestParam("certificate_id") Long certificateId
    ){
        AppResponseDto<OrderResponseDto> appResponseDto = orderService.create(userId, certificateId);
        linkProvider.addLinkToOrderResponse(appResponseDto.getData());  //link Hateoas
        return ResponseEntity.ok(appResponseDto);
    }

    //Get a certificate by id
    @GetMapping(value ="/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponseDto<OrderResponseDto>> get(
            @PathVariable("id") Long id
    ){
        AppResponseDto<OrderResponseDto> appResponseDto = orderService.get(id);
        linkProvider.addLinkToOrderResponse(appResponseDto.getData());  //link Hateoas
        return ResponseEntity.ok(appResponseDto);
    }

    // get order page
    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponseDto<List<OrderResponseDto>>> getPage(
            @RequestParam(required = false, name = "page", defaultValue = PAGE) int page,
            @RequestParam(required = false, name = "size", defaultValue = SIZE) int size
    ){
        AppResponseDto<List<OrderResponseDto>> appResponseDto = orderService.getPage(page, size);
        if(appResponseDto.getData().size() > 0){
            linkProvider.addLinkToOrderResponse(appResponseDto.getData().get(0));  //link Hateoas
        }
        return ResponseEntity.ok(appResponseDto);
    }
}
