package com.epam.esm.service.user;

import com.epam.esm.dto.request.UserRequestDto;
import com.epam.esm.dto.response.AppResponseDto;
import com.epam.esm.dto.response.GiftCertificateResponseDto;
import com.epam.esm.dto.response.OrderResponseDto;
import com.epam.esm.dto.response.UserResponseDto;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ResourceAlreadyExistException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.repository.OrderRepo;
import com.epam.esm.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final OrderRepo orderRepo;
    private final ModelMapper modelMapper;

    @Override
    public AppResponseDto<UserResponseDto> create(UserRequestDto type) {
        try {
            User user = modelMapper.map(type, User.class);
            return new AppResponseDto<>(HttpStatus.CREATED.value(), "user successfully created",
                    modelMapper.map(userRepo.save(user), UserResponseDto.class));
        } catch (Exception e) {
            throw new ResourceAlreadyExistException("user already exist with the username: " + type.getUsername());
        }
    }

    @Override
    public AppResponseDto<UserResponseDto> get(Long id) {
        User user = userRepo.findById(id).orElseThrow(() -> {
            throw new ResourceNotFoundException(id);
        });
        return new AppResponseDto<>(HttpStatus.OK.value(), "user", modelMapper.map(user, UserResponseDto.class));
    }


    @Override
    public AppResponseDto<List<UserResponseDto>> getList(int page, int size, String sortTerm) {
        Sort multiSort = getSortingParams(sortTerm);
        PageRequest pageRequest = multiSort != null ? PageRequest.of(page, size, multiSort) : PageRequest.of(page, size);
        return new AppResponseDto<>(HttpStatus.OK.value(), "user list",
                userRepo.findAll(pageRequest).map(user -> modelMapper.map(user, UserResponseDto.class)).toList()
        );
    }

    @Override
    public AppResponseDto<List<OrderResponseDto>> getUserOrders(Long userId, int page, int size) {

        boolean exists = userRepo.existsById(userId);
        if (!exists) {
            throw new ResourceNotFoundException("user not found with the id: " + userId);
        }
        PageRequest pageRequest = PageRequest.of(page, size);
        return new AppResponseDto<>(
                HttpStatus.OK.value(),
                "user order list",
                orderRepo.findAllByUser_Id(userId, pageRequest).map(order -> modelMapper.map(order, OrderResponseDto.class)).toList()
        );
    }

    @Override
    public AppResponseDto<OrderResponseDto> getUserOrder(Long userId, Long orderId) {
        Order order = orderRepo.findOrderByIdAndUser_Id(orderId, userId).orElseThrow(() -> {
            throw new ResourceNotFoundException(String.format("user with the id: %d and order with the id: %d is not exist", userId, orderId));
        });

        return new AppResponseDto<>(
                HttpStatus.OK.value(),
                "user order",
                modelMapper.map(order, OrderResponseDto.class));
    }


    private Sort getSortingParams(String sortTerm) {
        if (sortTerm == null) {
            return null;
        }
        String[] strings = sortTerm.split(",");
        Sort multiSort = null;
        for (String string : strings) {
            Sort tempSort = null;
            switch (string.trim()) {
                case "-username":
                    tempSort = Sort.by("username").descending();
                    break;
                case "username":
                    tempSort = Sort.by("username").ascending();
                    break;
                case "-create_date":
                    tempSort = Sort.by("create_date").descending();
                    break;
                case "create_date":
                    tempSort = Sort.by("create_date").ascending();
                    break;
                case "-update_date":
                    tempSort = Sort.by("update_date").descending();
                    break;
                case "update_date":
                    tempSort = Sort.by("update_date").ascending();
                    break;
            }
            if (tempSort != null) {
                if (multiSort == null) {
                    multiSort = tempSort;
                } else {
                    multiSort = multiSort.and(tempSort);
                }
            }
        }
        return multiSort;
    }

    @Override
    public AppResponseDto<UserResponseDto> update(Long id, UserRequestDto type) {
        return null;
    }

    @Override
    public AppResponseDto<Boolean> delete(Long id) {
        return null;
    }
}
