package com.epam.esm.service.user;

import com.epam.esm.dto.request.UserRequestDto;
import com.epam.esm.dto.response.AppResponseDto;
import com.epam.esm.dto.response.GiftCertificateResponseDto;
import com.epam.esm.dto.response.OrderResponseDto;
import com.epam.esm.dto.response.UserResponseDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ResourceAlreadyExistException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.repository.OrderRepo;
import com.epam.esm.repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepo userRepo;
    @Mock
    private OrderRepo orderRepo;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserResponseDto responseDto;
    private UserRequestDto requestDto;

    private List<Order> orderList;

    @BeforeEach
    void setUp() {
        Tag tag = new Tag("testTag");
        tag.setId(1L);
        List<Tag> tagList = List.of(tag);

        GiftCertificate certificate = new GiftCertificate("testCertificate", "testing purpose", BigDecimal.ONE, 10, tagList);
        certificate.setId(1L);

        orderList = List.of(new Order(BigDecimal.ONE, certificate, user));
        user = new User("testUser", orderList);

        requestDto = new UserRequestDto("testUser");
        responseDto = new UserResponseDto(1L, "testUser");
    }

    @Test
    void createWithSuccess() {
        when(modelMapper.map(requestDto, User.class)).thenReturn(user);
        when(userRepo.save(user)).thenReturn(user);
        when(modelMapper.map(user, UserResponseDto.class)).thenReturn(responseDto);
        AppResponseDto<UserResponseDto> appResponseDto = userService.create(requestDto);
        assertNotNull(appResponseDto);
        assertEquals(appResponseDto.getHttpStatus(), HttpStatus.CREATED.value());
        assertEquals(appResponseDto.getMessage(), "user successfully created");
    }

    @Test
    void createWithError() {
        when(userRepo.findById(anyLong())).thenThrow(new ResourceNotFoundException(1L));
        when(userRepo.save(user)).thenReturn(user);
        ResourceAlreadyExistException exception = assertThrows(ResourceAlreadyExistException.class, () ->
                userService.create(requestDto));

        assertEquals(exception.getMessage(), "user already exist with the username: " + requestDto.getUsername());
    }

    @Test
    void getWithSuccess() {
        when(userRepo.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(modelMapper.map(user, UserResponseDto.class)).thenReturn(responseDto);


        AppResponseDto<UserResponseDto> appResponseDto = userService.get(1L);

        assertNotNull(appResponseDto);
        assertEquals(appResponseDto.getMessage(), "user");
        assertEquals(appResponseDto.getData(), responseDto);
    }

    @Test
    void getWithError() {
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> userService.get(1L));

        assertEquals(exception.getMessage(), "Requested resource not found with the id: 1");
    }

    @Test
    void getListWithSuccess() {
        PageRequest pageable = PageRequest.of(0, 10, Sort.by("username").ascending());

        when(userRepo.findAll(pageable))
                .thenReturn(new PageImpl<>(List.of(user)));
        when(modelMapper.map(user, UserResponseDto.class)).thenReturn(responseDto);
        AppResponseDto<List<UserResponseDto>> appResponseDto = userService.getList(0, 10, "username");
        assertNotNull(appResponseDto);
        assertEquals(appResponseDto.getMessage(), "user list");
    }


    @Test
    void getUserOrdersWithSuccess() {
        when(userRepo.existsById(anyLong())).thenReturn(true);
        when(orderRepo.findAllByUser_Id(anyLong(), any())).thenReturn(new PageImpl<>(orderList));
        AppResponseDto<List<OrderResponseDto>> appResponseDto = userService.getUserOrders(1L, 0, 10);
        assertNotNull(appResponseDto);
        assertEquals(appResponseDto.getMessage(), "user order list");
    }

    @Test
    void getUserOrdersWithError() {
        when(userRepo.existsById(anyLong())).thenReturn(false);
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> userService.getUserOrders(1L, 0, 10));

        assertEquals(exception.getMessage(), "user not found with the id: 1");
    }

    @Test
    void getUserOrderWithSuccess() {
        when(orderRepo.findOrderByIdAndUser_Id(anyLong(), anyLong())).thenReturn(Optional.ofNullable(orderList.get(0)));
        when(modelMapper.map(orderList.get(0), OrderResponseDto.class)).thenReturn(new OrderResponseDto());
        AppResponseDto<OrderResponseDto> appResponseDto = userService.getUserOrder(1L, 1L);
        assertNotNull(appResponseDto);
        assertEquals(appResponseDto.getMessage(), "user order");
    }

    @Test
    void getUserOrderWithError() {
        when(orderRepo.findOrderByIdAndUser_Id(anyLong(), anyLong())).thenThrow(new ResourceNotFoundException("user with the id: 1 and order with the id: 1 is not exist"));
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> userService.getUserOrder(1L, 1L));
        assertEquals(exception.getMessage(), "user with the id: 1 and order with the id: 1 is not exist");
    }
}

