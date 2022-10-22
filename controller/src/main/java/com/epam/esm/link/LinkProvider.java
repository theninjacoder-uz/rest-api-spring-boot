package com.epam.esm.link;

import com.epam.esm.dto.response.*;

public interface LinkProvider {
    void addLinkToTagResponse(TagResponseDto tagResponseDto);
    void addLinkToGiftResponse(GiftCertificateResponseDto giftCertificateResponseDto);
    void addLinkToUserResponse(UserResponseDto userResponseDto);
    void addLinkToOrderResponse(OrderResponseDto orderResponseDto);
}
