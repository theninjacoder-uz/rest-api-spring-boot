package com.epam.esm.controller;

import com.epam.esm.dto.request.UserRequestDto;
import com.epam.esm.dto.response.AppResponseDto;
import com.epam.esm.dto.response.OrderResponseDto;
import com.epam.esm.dto.response.UserResponseDto;
import com.epam.esm.link.LinkProvider;
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
    private final LinkProvider linkProvider;
    private final static String PAGE = "0";
    private final static String SIZE = "10";
    @PostMapping( consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponseDto<UserResponseDto>> create(@RequestBody UserRequestDto userRequestDto){
        AppResponseDto<UserResponseDto> appResponseDto = userService.create(userRequestDto);
        linkProvider.addLinkToUserResponse(appResponseDto.getData());
        return ResponseEntity.ok(appResponseDto);
    }

    @GetMapping(value = "/{id}",  consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public  ResponseEntity<AppResponseDto<UserResponseDto>> get(@PathVariable Long id){
        AppResponseDto<UserResponseDto> appResponseDto = userService.get(id);
        linkProvider.addLinkToUserResponse(appResponseDto.getData());
        return ResponseEntity.ok(appResponseDto);
    }

    @GetMapping( consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponseDto<List<UserResponseDto>>> getList(
            @RequestParam(value = "page", required = false, defaultValue = PAGE) int page,
            @RequestParam(value = "size", required = false, defaultValue = SIZE) int size,
            @RequestParam(value = "sort", required = false) String sortTerm
    ){
        AppResponseDto<List<UserResponseDto>> appResponseDto = userService.getList(page, size, sortTerm);
        userService.getList(page, size, sortTerm);
        return ResponseEntity.ok(appResponseDto);
    }

    @GetMapping(value = "/{userId}/orders/{orderId}",  consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponseDto<OrderResponseDto>> getUserOrder(
            @PathVariable("userId") Long userId,
            @PathVariable("orderId") Long orderId
    ){
        AppResponseDto<OrderResponseDto> appResponseDto = userService.getUserOrder(userId, orderId);
        userService.getUserOrder(userId, orderId);
        return ResponseEntity.ok(appResponseDto);
    }

    @GetMapping(value = "/{userId}/orders",  consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponseDto<List<OrderResponseDto>>> getUserOrders(
            @PathVariable("userId") Long userId,
            @RequestParam(value = "page", required = false, defaultValue = PAGE) int page,
            @RequestParam(value = "size", required = false, defaultValue = SIZE) int size
    ){
        AppResponseDto<List<OrderResponseDto>> appResponseDto = userService.getUserOrders(userId, page, size);
        linkProvider.addLinkToOrderResponse(appResponseDto.getData().get(0));
        return ResponseEntity.ok(appResponseDto);
    }


}
