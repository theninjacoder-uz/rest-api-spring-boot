package com.epam.esm.service.gift;


import com.epam.esm.dto.request.GiftCertificateRequestDto;
import com.epam.esm.dto.request.TagRequestDto;
import com.epam.esm.dto.response.AppResponseDto;
import com.epam.esm.dto.response.GiftCertificateResponseDto;
import com.epam.esm.dto.response.TagResponseDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DataPersistenceException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.repository.GiftCertificateRepo;
import com.epam.esm.repository.TagRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GiftCertificateServiceImplTest {

    @Mock
    private GiftCertificateRepo giftCertificateRepo;

    @Mock
    private TagRepo tagRepo;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private GiftCertificateServiceImpl giftCertificateService;

    private GiftCertificateRequestDto requestDto;
    private GiftCertificateResponseDto responseDto;
    private GiftCertificate certificate;
    private Tag tag;

    @BeforeEach
    void setUp(){
        tag = new Tag("testTag");
        tag.setId(1L);
        List<Tag> tagList = List.of(tag);

        certificate = new GiftCertificate("testCertificate", "testing purpose", BigDecimal.ONE, 10, tagList);
        certificate.setId(1L);

        requestDto = new GiftCertificateRequestDto("testCertificate", "testing purpose", BigDecimal.ONE, 10, List.of(new TagRequestDto("testTag")));
        responseDto = new GiftCertificateResponseDto(1L, "testCertificate", "testing purpose", BigDecimal.ONE, 10, List.of(new TagResponseDto(1L, "testTag")), LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    void createWithSuccess() {
        when(modelMapper.map(requestDto, GiftCertificate.class)).thenReturn(certificate);
        when(tagRepo.saveTagByNameIfNotExists("testTag")).thenReturn(tag);
        when(giftCertificateRepo.save(certificate)).thenReturn(certificate);
        when(modelMapper.map(certificate, GiftCertificateResponseDto.class)).thenReturn(responseDto);

        AppResponseDto<GiftCertificateResponseDto> appResponseDto = giftCertificateService.create(requestDto);
        assertNotNull(appResponseDto);
        assertEquals(appResponseDto.getHttpStatus(), HttpStatus.CREATED.value());
        assertEquals(appResponseDto.getMessage(), "successfully created");
    }

    @Test
    void createWithError() {
        when(modelMapper.map(requestDto, GiftCertificate.class)).thenReturn(certificate);
        when(tagRepo.saveTagByNameIfNotExists("testTag")).thenReturn(tag);
        when(giftCertificateRepo.save(certificate)).thenThrow(DataPersistenceException.class);

        DataPersistenceException exception = assertThrows(DataPersistenceException.class, () ->
                giftCertificateService.create(requestDto));
        assertEquals(exception.getMessage(), "Error when gift certificate creation");
    }

    @Test
    void getWithSuccess() {
        when(giftCertificateRepo.findById(anyLong())).thenReturn(Optional.ofNullable(certificate));
        when(modelMapper.map(certificate, GiftCertificateResponseDto.class)).thenReturn(responseDto);
        AppResponseDto<GiftCertificateResponseDto> appResponseDto = giftCertificateService.get(1L);

        assertNotNull(appResponseDto);
        assertEquals(appResponseDto.getMessage(), "gift certificate");
        assertEquals(appResponseDto.getData(), responseDto);
    }

    @Test
    void getWithError() {
//        when(giftCertificateRepo.findById(anyLong())).thenThrow(ResourceNotFoundException.class);
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> giftCertificateService.get(1L));

        assertEquals(exception.getMessage(), "Requested resource not found with the id: 1");
    }

    @Test
    void updateWithSuccess() {
        when(giftCertificateRepo.findById(anyLong())).thenReturn(Optional.ofNullable(certificate));
        when(tagRepo.saveTagByNameIfNotExists("testTag")).thenReturn(tag);
        when(giftCertificateRepo.save(certificate)).thenReturn(certificate);
        when(modelMapper.map(certificate, GiftCertificateResponseDto.class)).thenReturn(responseDto);
        doNothing().when(modelMapper).map(requestDto, certificate);

        AppResponseDto<GiftCertificateResponseDto> appResponseDto = giftCertificateService.update(1L, requestDto);
        assertNotNull(appResponseDto);
        assertEquals(appResponseDto.getMessage(), "successfully updated");
        assertEquals(appResponseDto.getData(), responseDto);
    }

    @Test
    void updateWithError() {
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> giftCertificateService.get(1L));
        assertEquals(exception.getMessage(), "Requested resource not found with the id: 1");
    }

    @Test
    void deleteWithSuccess() {
        when(giftCertificateRepo.existsById(anyLong())).thenReturn(true);
        doNothing().when(giftCertificateRepo).deleteById(anyLong());
        giftCertificateService.delete(1L);
        verify(giftCertificateRepo, times(1)).deleteById(1L);
    }

    @Test
    void deleteWithError() {
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> giftCertificateService.get(1L));
        assertEquals(exception.getMessage(), "Requested resource not found with the id: 1");
    }

    @Test
    void getListWithSuccess() {
        when(giftCertificateRepo.searchGiftCertificateByNameContainingOrDescriptionContaining(anyString(), anyString(), any()))
                .thenReturn(new PageImpl<>(List.of(certificate)));
        when(modelMapper.map(certificate, GiftCertificateResponseDto.class)).thenReturn(responseDto);
        AppResponseDto<List<GiftCertificateResponseDto>> appResponseDto = giftCertificateService.getList("amazing", "name", 0, 10);
        assertNotNull(appResponseDto);
        assertEquals(appResponseDto.getMessage(), "gift certificate list");
    }


    @Test
    void getPageByTagList() {
        when(giftCertificateRepo.getPageBySearchingTermAndSort(anyList(), anyLong(), any()))
                .thenReturn(new PageImpl<>(List.of(certificate)));
        AppResponseDto<List<GiftCertificateResponseDto>> appResponseDto = giftCertificateService.getPageByTagList(List.of("test", "tag"), "name", 0, 10);
        assertNotNull(appResponseDto);
        assertEquals(appResponseDto.getMessage(), "gift certificate list");

    }
}