package com.epam.esm.service.gift;

import com.epam.esm.dto.request.GiftCertificateRequestDto;
import com.epam.esm.dto.response.AppResponseDto;
import com.epam.esm.dto.response.GiftCertificateResponseDto;
import com.epam.esm.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GiftCertificateService extends BaseService<GiftCertificateRequestDto, GiftCertificateResponseDto> {
    AppResponseDto<List<GiftCertificateResponseDto>> getPageByTagList(List<String> tagNameList, String sortTerms, int page, int size);

    AppResponseDto<List<GiftCertificateResponseDto>> getList(
            String searchTerm, String sortTerm, int page, int size
    );
}
