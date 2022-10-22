package com.epam.esm.controller;

import com.epam.esm.dto.request.GiftCertificateRequestDto;
import com.epam.esm.dto.response.AppResponseDto;
import com.epam.esm.dto.response.GiftCertificateResponseDto;
import com.epam.esm.link.LinkProvider;
import com.epam.esm.service.gift.GiftCertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/certificates")
public class GiftCertificateController {
    private final GiftCertificateService giftCertificateService;
    private final LinkProvider linkProvider;
    private static final String PAGE = "0";
    private static final String SIZE = "10";

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponseDto<GiftCertificateResponseDto>> create(
            @RequestBody @Valid GiftCertificateRequestDto requestDto){
        AppResponseDto<GiftCertificateResponseDto> appResponseDto = giftCertificateService.create(requestDto);
        linkProvider.addLinkToGiftResponse(appResponseDto.getData());
        return ResponseEntity.ok(appResponseDto);
    }


    @GetMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponseDto<GiftCertificateResponseDto>> get(
            @PathVariable Long id){
        AppResponseDto<GiftCertificateResponseDto> appResponseDto = giftCertificateService.get(id);
        linkProvider.addLinkToGiftResponse(appResponseDto.getData());
        return ResponseEntity.ok(appResponseDto);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponseDto<GiftCertificateResponseDto>> update(
            @PathVariable Long id,
            @RequestBody @Valid GiftCertificateRequestDto requestDto
    ){
        AppResponseDto<GiftCertificateResponseDto> appResponseDto = giftCertificateService.update(id, requestDto);
        linkProvider.addLinkToGiftResponse(appResponseDto.getData());
        return ResponseEntity.ok(appResponseDto);
    }

    @DeleteMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponseDto<Boolean>> delete(@PathVariable Long id){
        return ResponseEntity.ok(giftCertificateService.delete(id));
    }

    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPage(
            @RequestParam(required = false, name = "search") String searchTerm,
            @RequestParam(required = false, name = "sort") String sortTerm,
            @RequestParam(required = false, name = "page", defaultValue = PAGE) int page,
            @RequestParam(required = false, name = "size", defaultValue = SIZE) int size

    ){
        AppResponseDto<List<GiftCertificateResponseDto>> appResponseDto = giftCertificateService.getList(searchTerm, sortTerm, page, size);
        linkProvider.addLinkToGiftResponse(appResponseDto.getData().get(0));
        return ResponseEntity.ok(appResponseDto);
    }

    @GetMapping(value = "/tags", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPageByTagNames(
            @RequestParam(required = false, name = "name") List<String> tagNameList,
            @RequestParam(required = false, name = "sort") String sortTerm,
            @RequestParam(required = false, name = "page", defaultValue = PAGE) int page,
            @RequestParam(required = false, name = "size", defaultValue = SIZE) int size
    ){
        AppResponseDto<List<GiftCertificateResponseDto>> appResponseDto = giftCertificateService.getPageByTagList(tagNameList, sortTerm, page, size);
        linkProvider.addLinkToGiftResponse(appResponseDto.getData().get(0));
        return ResponseEntity.ok(appResponseDto);
    }




}
