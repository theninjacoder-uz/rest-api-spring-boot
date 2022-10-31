package com.epam.esm.service.tag;

import com.epam.esm.dto.request.TagRequestDto;
import com.epam.esm.dto.response.AppResponseDto;
import com.epam.esm.dto.response.TagResponseDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ResourceAlreadyExistException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.repository.TagRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {

    @Mock
    private TagRepo tagRepo;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TagServiceImpl tagService;

    private Tag savedTag;
    private Tag rawTag;
    private TagRequestDto requestDto;
    private TagResponseDto responseDto;



    @BeforeEach
    void setUp(){
        requestDto = new TagRequestDto("test");
        responseDto = new TagResponseDto(1L, "test");

        rawTag = new Tag("test");

        savedTag = new Tag("test");
        savedTag.setId(1L);
        savedTag.setCreateDate(LocalDateTime.now());
        savedTag.setLastUpdateDate(LocalDateTime.now());
    }

    @Test
    void createWithSuccess() {
        when(modelMapper.map(requestDto, Tag.class)).thenReturn(rawTag);
        when(tagRepo.save(rawTag)).thenReturn(savedTag);
        when(modelMapper.map(savedTag, TagResponseDto.class)).thenReturn(responseDto);
        AppResponseDto<TagResponseDto> appResponseDto = tagService.create(requestDto);
        assertNotNull(appResponseDto);
        assertEquals(appResponseDto.getHttpStatus(), HttpStatus.CREATED.value());
        assertEquals(appResponseDto.getData().getName(), savedTag.getName());
    }

    @Test
    void createWithError() {
        when(modelMapper.map(requestDto, Tag.class)).thenReturn(rawTag);
        when(tagRepo.save(rawTag)).thenThrow(ResourceAlreadyExistException.class);
        ResourceAlreadyExistException exception =assertThrows(ResourceAlreadyExistException.class,
                () -> tagService.create(requestDto));
        assertEquals(exception.getMessage(), "already exist tag with the name: " + requestDto.getName());
    }

    @Test
    void getWithSuccess() {
        when(tagRepo.findById(1L)).thenReturn(Optional.ofNullable(savedTag));
        when(modelMapper.map(savedTag, TagResponseDto.class)).thenReturn(responseDto);
        AppResponseDto<TagResponseDto> appResponseDto = tagService.get(1L);

        assertNotNull(appResponseDto);
        assertEquals(appResponseDto.getHttpStatus(), HttpStatus.OK.value());
        assertEquals(appResponseDto.getData().getName(), responseDto.getName());
    }

    @Test
    void getWithError() {
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> tagService.get(2L));
        assertEquals(exception.getMessage(), "Requested resource not found with the id: 2");
    }

    @Test
    void updateWithSuccess() {
        when(tagRepo.findById(anyLong())).thenReturn(Optional.ofNullable(savedTag));
        when(tagRepo.save(savedTag)).thenReturn(savedTag);

        AppResponseDto<TagResponseDto> appResponseDto = tagService.update(1L, requestDto);
        assertNotNull(appResponseDto);
        assertEquals(appResponseDto.getHttpStatus(), HttpStatus.OK.value());
        assertEquals(appResponseDto.getMessage(), "successfully updated");
    }

    @Test
    void updateWithError() {
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> tagService.update(2L, requestDto));
        assertEquals(exception.getMessage(), "Requested resource not found with the id: 2");
    }

    @Test
    void deleteWithSuccess() {
        when(tagRepo.existsById(anyLong())).thenReturn(true);
        doNothing().when(tagRepo).deleteById(anyLong());
        tagService.delete(1L);
        verify(tagRepo, times(1)).deleteById(1L);
    }

    @Test
    void deleteWithError() {
//        when(tagRepo.existsById(anyLong())).thenReturn(false);
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> tagService.get(2L));
        assertEquals(exception.getMessage(), "Requested resource not found with the id: 2");
    }

    @Test
    void getListWithSuccess() {
        PageRequest pageable = PageRequest.of(0, 10, Sort.by("name").ascending());
        when(tagRepo.findAll(pageable)).thenReturn(new PageImpl<>(List.of(savedTag)));
        when(modelMapper.map(savedTag, TagResponseDto.class)).thenReturn(responseDto);

        AppResponseDto<List<TagResponseDto>> appResponseDto = tagService.getList(0,10, "name");
        assertNotNull(appResponseDto);
        assertEquals(1, appResponseDto.getData().size());
    }

    @Test
    void getMostUsedTagOfUserWithSuccess() {
        when(tagRepo.findMostUsedTag()).thenReturn(Optional.ofNullable(savedTag));
        when(modelMapper.map(savedTag, TagResponseDto.class)).thenReturn(responseDto);
        AppResponseDto<TagResponseDto> mostUsedTagOfUser = tagService.getMostUsedTagOfUser();

        assertNotNull(mostUsedTagOfUser);
        assertEquals(mostUsedTagOfUser.getHttpStatus(), HttpStatus.OK.value());
        assertEquals(mostUsedTagOfUser.getMessage(), "most used tag");
    }
}