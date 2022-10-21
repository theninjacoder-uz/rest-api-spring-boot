package com.epam.esm.link;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.controller.TagController;
import com.epam.esm.controller.UserController;
import com.epam.esm.dto.response.AppResponseDto;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class LinkProviderImpl implements LinkProvider{
    @Override
    public void addLinkToTagResponse(AppResponseDto<?> responseDto) {
        responseDto.add(linkTo(methodOn(TagController.class).getList(0, 10, null)).withRel("get initial 10 tags"));
        responseDto.add(linkTo(methodOn(TagController.class).get(1L)).withRel("get tag by id"));
    }

    @Override
    public void addLinkToGiftResponse(AppResponseDto<?> responseDto) {
        responseDto.add(linkTo(methodOn(GiftCertificateController.class).getPage(null, null,  0, 10)).withRel("get initial 10 certificates"));
        responseDto.add(linkTo(methodOn(GiftCertificateController.class).getPageByTagNames(null, null,  0, 10)).withRel("get initial 10 certificates"));
    }

    @Override
    public void addLinkToUserResponse(AppResponseDto<?> responseDto) {
        responseDto.add(linkTo(methodOn(UserController.class).getList(0, 10, null)).withRel("get initial 10 users"));
    }

    @Override
    public void addLinkToOrderResponse(AppResponseDto<?> responseDto) {
        responseDto.add(linkTo(methodOn(TagController.class).getList(0, 10, null)).withRel("get initial 10 tags"));
    }
}
