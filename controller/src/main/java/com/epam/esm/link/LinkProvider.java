package com.epam.esm.link;

import com.epam.esm.dto.response.AppResponseDto;
import com.epam.esm.dto.response.TagResponseDto;

public interface LinkProvider {
    void addLinkToTagResponse(AppResponseDto<?> responseDto);
    void addLinkToGiftResponse(AppResponseDto<?> responseDto);
    void addLinkToUserResponse(AppResponseDto<?> responseDto);
    void addLinkToOrderResponse(AppResponseDto<?> responseDto);
}
