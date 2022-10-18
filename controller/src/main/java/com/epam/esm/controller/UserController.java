package com.epam.esm.controller;

import com.epam.esm.dto.request.UserRequestDto;
import com.epam.esm.dto.response.AppResponseDto;
import com.epam.esm.dto.response.GiftCertificateResponseDto;
import com.epam.esm.dto.response.UserResponseDto;
import com.epam.esm.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<AppResponseDto<UserResponseDto>> create(@RequestBody UserRequestDto userRequestDto){
        return ResponseEntity.ok(userService.create(userRequestDto));
    }

    @GetMapping("/{id}")
    public  ResponseEntity<AppResponseDto<UserResponseDto>> get(@PathVariable Long id){
        return ResponseEntity.ok(userService.get(id));
    }

}
