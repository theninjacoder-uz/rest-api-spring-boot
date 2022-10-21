package com.epam.esm.service.user;

import com.epam.esm.dto.request.UserRequestDto;
import com.epam.esm.dto.response.AppResponseDto;
import com.epam.esm.dto.response.OrderResponseDto;
import com.epam.esm.dto.response.UserResponseDto;
import com.epam.esm.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService extends BaseService<UserRequestDto, UserResponseDto> {
    AppResponseDto<List<UserResponseDto>> getList(int page, int size, String sortTerm);
    AppResponseDto<List<OrderResponseDto>> getUserOrders(Long userId, int page, int size);
    AppResponseDto<OrderResponseDto> getUserOrder(Long userId, Long orderId);

}
