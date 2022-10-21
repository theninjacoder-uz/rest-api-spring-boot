package com.epam.esm.controller;

import com.epam.esm.dto.request.UserRequestDto;
import com.epam.esm.dto.response.AppResponseDto;
import com.epam.esm.dto.response.OrderResponseDto;
import com.epam.esm.dto.response.UserResponseDto;
import com.epam.esm.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final static String PAGE = "0";
    private final static String SIZE = "10";
    @PostMapping( consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponseDto<UserResponseDto>> create(@RequestBody UserRequestDto userRequestDto){
        return ResponseEntity.ok(userService.create(userRequestDto));
    }

    @GetMapping(value = "/{id}",  consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public  ResponseEntity<AppResponseDto<UserResponseDto>> get(@PathVariable Long id){
        return ResponseEntity.ok(userService.get(id));
    }

    @GetMapping( consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponseDto<List<UserResponseDto>>> getList(
            @RequestParam(value = "page", required = false, defaultValue = PAGE) int page,
            @RequestParam(value = "size", required = false, defaultValue = SIZE) int size,
            @RequestParam(value = "sort", required = false) String sortTerm
    ){
        return ResponseEntity.ok(userService.getList(page, size, sortTerm));
    }

    @GetMapping(value = "/{userId}/orders/{orderId}",  consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponseDto<OrderResponseDto>> getUserOrder(
            @PathVariable("userId") Long userId,
            @PathVariable("orderId") Long orderId
    ){
        return ResponseEntity.ok(userService.getUserOrder(userId, orderId));
    }

    @GetMapping(value = "/{userId}/orders",  consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponseDto<List<OrderResponseDto>>> getUserOrders(
            @PathVariable("userId") Long userId,
            @RequestParam(value = "page", required = false, defaultValue = PAGE) int page,
            @RequestParam(value = "size", required = false, defaultValue = SIZE) int size
    ){
        return ResponseEntity.ok(userService.getUserOrders(userId, page, size));
    }


}
