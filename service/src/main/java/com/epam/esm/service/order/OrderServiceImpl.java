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
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepo orderRepo;
    private final UserRepo userRepo;
    private final ModelMapper modelMapper;
    private final GiftCertificateRepo giftCertificateRepo;


    @Override
    public AppResponseDto<OrderResponseDto> create(Long userId, Long certificateId) {
        GiftCertificate certificate = giftCertificateRepo.findById(certificateId)
                .orElseThrow(() -> {
                    throw new ResourceNotFoundException(certificateId);
                });
        User user = userRepo.findById(userId).orElseThrow(() -> {
            throw new ResourceNotFoundException(userId);
        });

        return new AppResponseDto<>(HttpStatus.CREATED.value(), "order created",
                modelMapper.map(orderRepo.save(new Order(certificate.getPrice(), certificate, user)), OrderResponseDto.class)
        );
    }

    @Override
    public AppResponseDto<OrderResponseDto> get(Long orderId) {
        Order order = orderRepo.findById(orderId).orElseThrow(() -> {
            throw new ResourceNotFoundException(orderId);
        });
        return new AppResponseDto<>(HttpStatus.CREATED.value(),
                "order created",
                modelMapper.map(order, OrderResponseDto.class)
        );
    }

    @Override
    public AppResponseDto<List<OrderResponseDto>> getPage(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return new AppResponseDto<>(HttpStatus.CREATED.value(),
                "order created",
                orderRepo.findAll(pageRequest).map(order -> modelMapper.map(order, OrderResponseDto.class)).toList()
        );
    }
}
