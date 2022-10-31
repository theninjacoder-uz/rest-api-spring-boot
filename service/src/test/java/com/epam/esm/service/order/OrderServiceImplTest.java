package com.epam.esm.service.order;

import com.epam.esm.dto.response.AppResponseDto;
import com.epam.esm.dto.response.OrderResponseDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.repository.GiftCertificateRepo;
import com.epam.esm.repository.OrderRepo;
import com.epam.esm.repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private  OrderRepo orderRepo;
    @Mock
    private  UserRepo userRepo;
    @Mock
    private  ModelMapper modelMapper;
    @Mock
    private  GiftCertificateRepo giftCertificateRepo;

    @InjectMocks
    private OrderServiceImpl orderService;

    private GiftCertificate certificate;

    private User user;

    private Order order;


    @BeforeEach
    void setUp(){
        certificate = new GiftCertificate("testCertificate", "testing purpose", BigDecimal.ONE, 10, new ArrayList<>());
        certificate.setId(1L);
        user = new User("testUser", new ArrayList<>());
        order  = new Order(BigDecimal.ONE, certificate, user);
    }

    @Test
    void createWithSuccess() {
        when(giftCertificateRepo.findById(anyLong())).thenReturn(Optional.ofNullable(certificate));
        when(userRepo.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(orderRepo.save(any())).thenReturn(order);
        when(modelMapper.map(order, OrderResponseDto.class)).thenReturn(new OrderResponseDto());

        AppResponseDto<OrderResponseDto> appResponseDto = orderService.create(1L, 1L);
        assertNotNull(appResponseDto);
        assertEquals(appResponseDto.getMessage(), "order created");
    }

    @Test
    void createWithError() {
//        when(giftCertificateRepo.findById(anyLong())).thenThrow(ResourceNotFoundException.class);
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> orderService.create(1L, 1L));
        assertEquals(exception.getMessage(), "Requested resource not found with the id: 1");
    }

    @Test
    void getWithSuccess() {
        when(orderRepo.findById(anyLong())).thenReturn(Optional.ofNullable(order));
        when(modelMapper.map(order, OrderResponseDto.class)).thenReturn(new OrderResponseDto());
        AppResponseDto<OrderResponseDto> appResponseDto = orderService.get(1L);

        assertNotNull(appResponseDto);
        assertEquals(appResponseDto.getMessage(), "order");
    }


    @Test
    void getWithError() {
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> orderService.get(1L));
        assertEquals(exception.getMessage(), "Requested resource not found with the id: 1");
    }

    @Test
    void getPage() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        when(orderRepo.findAll(pageRequest)).thenReturn(new PageImpl<>(List.of(order)));
        when(modelMapper.map(order, OrderResponseDto.class)).thenReturn(new OrderResponseDto());

        AppResponseDto<List<OrderResponseDto>> appResponseDto = orderService.getPage(0, 10);


        assertNotNull(appResponseDto);
        assertEquals(appResponseDto.getMessage(), "order page");
    }
}