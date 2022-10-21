package com.epam.esm.service.tag;

import com.epam.esm.dto.request.TagRequestDto;
import com.epam.esm.dto.response.AppResponseDto;
import com.epam.esm.dto.response.TagResponseDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ResourceAlreadyExistException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.repository.TagRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepo tagRepo;
    private final ModelMapper modelMapper;

    @Override
    public AppResponseDto<TagResponseDto> create(TagRequestDto type) {
        try {
            return new AppResponseDto<>(HttpStatus.CREATED.value(), "tag successfully created",
                    modelMapper.map(tagRepo.save(modelMapper.map(type, Tag.class)), TagResponseDto.class));
        } catch (Exception e) {
            throw new ResourceAlreadyExistException("already exist tag with the name: " + type.getName());
        }
    }

    @Override
    public AppResponseDto<TagResponseDto> get(Long id) {
        Tag tag = tagRepo.findById(id).orElseThrow(() -> {
            throw new ResourceNotFoundException(id);
        });
        return new AppResponseDto<>(HttpStatus.OK.value(), "tag", modelMapper.map(tag, TagResponseDto.class));
    }

    @Override
    public AppResponseDto<TagResponseDto> update(Long id, TagRequestDto type) {
        Tag tag = tagRepo.findById(id).orElseThrow(() -> {
            throw new ResourceNotFoundException(id);
        });
        modelMapper.map(type, tag);
        return new AppResponseDto<>(
                HttpStatus.OK.value(),
                "successfully updated",
                modelMapper.map(tag, TagResponseDto.class)
        );
    }

    @Override
    public AppResponseDto<Boolean> delete(Long id) {
        boolean exists = tagRepo.existsById(id);
        if (!exists) {
            throw new ResourceNotFoundException(id);
        }
        tagRepo.deleteById(id);
        return new AppResponseDto<>(HttpStatus.NO_CONTENT.value(), "successfully deleted", true);
    }

    @Override
    public AppResponseDto<List<TagResponseDto>> getList(int page, int size, String sortTerm) {
        Sort multiSort = getSortingParams(sortTerm);
        PageRequest pageRequest = multiSort == null ? PageRequest.of(page, size) : PageRequest.of(page, size, multiSort) ;
        return new AppResponseDto<>(HttpStatus.OK.value(), "tag list",
                tagRepo.findAll(pageRequest).map(tag -> modelMapper.map(tag, TagResponseDto.class)).toList()
        );
    }

    private Sort getSortingParams(String sortTerm) {
        if(sortTerm == null){
            return null;
        }
        String[] strings = sortTerm.split(",");
        Sort multiSort = null;
        for (String string : strings) {
            Sort tempSort = null;
            switch (string.trim()) {
                case "-name":
                    tempSort = Sort.by("name").descending();
                    break;
                case "name":
                    tempSort = Sort.by("name").ascending();
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
}
