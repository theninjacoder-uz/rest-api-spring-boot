package com.epam.esm.link;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.controller.OrderController;
import com.epam.esm.controller.TagController;
import com.epam.esm.controller.UserController;
import com.epam.esm.dto.response.GiftCertificateResponseDto;
import com.epam.esm.dto.response.OrderResponseDto;
import com.epam.esm.dto.response.TagResponseDto;
import com.epam.esm.dto.response.UserResponseDto;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class LinkProviderImpl implements LinkProvider{
    @Override
    public void addLinkToTagResponse(TagResponseDto responseDto) {
        responseDto.add(linkTo(methodOn(TagController.class).getList(0, 10, null)).withRel("get initial 10 tags"));
        responseDto.add(linkTo(methodOn(TagController.class).get(responseDto.getId())).withRel("get tag by id"));
    }

    @Override
    public void addLinkToGiftResponse(GiftCertificateResponseDto responseDto) {
        responseDto.add(linkTo(methodOn(GiftCertificateController.class).get(responseDto.getId())).withRel("certificate"));
        responseDto.add(linkTo(methodOn(GiftCertificateController.class).getPage(null, null,  0, 10)).withRel("certificate_list"));
        responseDto.add(linkTo(methodOn(GiftCertificateController.class).getPageByTagNames(null, null,  0, 10)).withRel("certificate_list_by_tags"));

    }

    @Override
    public void addLinkToUserResponse(UserResponseDto responseDto) {
        responseDto.add(linkTo(methodOn(UserController.class).get(responseDto.getId())).withRel("user"));
        responseDto.add(linkTo(methodOn(UserController.class).getList(0, 10, null)).withRel("user_list"));
    }

    @Override
    public void addLinkToOrderResponse(OrderResponseDto responseDto) {

        responseDto.add(linkTo(methodOn(OrderController.class).get(responseDto.getId())).withRel("order"));
        responseDto.add(linkTo(methodOn(OrderController.class).getPage(0, 10)).withRel("order_list"));
        responseDto.add(linkTo(methodOn(UserController.class)
                .getUserOrder(responseDto.getUserResponseDto().getId(), responseDto.getGiftCertificateResponseDto().getId())).withRel("user_order"));
        responseDto.add(linkTo(methodOn(UserController.class)
                .getUserOrders(responseDto.getUserResponseDto().getId(), 0, 10)).withRel("user_orders"));
    }
}
