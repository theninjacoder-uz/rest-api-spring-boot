package com.epam.esm.service.order;

import com.epam.esm.dto.response.AppResponseDto;
import com.epam.esm.dto.response.OrderResponseDto;

import java.util.List;

public interface OrderService{
    AppResponseDto<OrderResponseDto> create(Long userId, Long certificateId);
    AppResponseDto<OrderResponseDto> get(Long orderId);
    AppResponseDto<List<OrderResponseDto>> getPage(int page, int size);

}
