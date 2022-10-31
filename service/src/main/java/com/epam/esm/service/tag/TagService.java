package com.epam.esm.service.tag;

import com.epam.esm.dto.request.TagRequestDto;
import com.epam.esm.dto.response.AppResponseDto;
import com.epam.esm.dto.response.TagResponseDto;
import com.epam.esm.service.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TagService extends BaseService<TagRequestDto, TagResponseDto> {
    AppResponseDto<List<TagResponseDto>> getList(int page, int size, String sortTerm);
    AppResponseDto<TagResponseDto> getMostUsedTagOfUser();
}
