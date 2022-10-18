package com.epam.esm.service.gift;

import com.epam.esm.dto.request.GiftCertificateRequestDto;
import com.epam.esm.dto.response.AppResponseDto;
import com.epam.esm.dto.response.GiftCertificateResponseDto;
import com.epam.esm.repository.GiftCertificateRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GiftCertificateImpl implements GiftCertificateService{

    private final GiftCertificateRepo giftCertificateRepo;

    @Override
    public AppResponseDto<GiftCertificateResponseDto> create(GiftCertificateRequestDto requestDto) {
        return null;
    }

    @Override
    public AppResponseDto<GiftCertificateResponseDto> get(Long id) {
        return null;
    }

    @Override
    public AppResponseDto<GiftCertificateResponseDto> update(Long id, GiftCertificateRequestDto type) {
        return null;
    }

    @Override
    public AppResponseDto<Boolean> delete(Long id) {
        return null;
    }
}
