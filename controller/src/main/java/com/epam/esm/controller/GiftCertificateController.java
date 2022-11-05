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
    private static final String PAGE = "0"; //default page
    private static final String SIZE = "10"; // default size

    //Create Gift certificate with tags if new tag received save it tags table
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponseDto<GiftCertificateResponseDto>> create(
            @RequestBody @Valid GiftCertificateRequestDto requestDto){
        AppResponseDto<GiftCertificateResponseDto> appResponseDto = giftCertificateService.create(requestDto);
        //link Hateoas
        linkProvider.addLinkToGiftResponse(appResponseDto.getData());
        return ResponseEntity.ok(appResponseDto);
    }


    //Get certificate with ID
    @GetMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponseDto<GiftCertificateResponseDto>> get(
            @PathVariable Long id){
        AppResponseDto<GiftCertificateResponseDto> appResponseDto = giftCertificateService.get(id);
        //link Hateoas
        linkProvider.addLinkToGiftResponse(appResponseDto.getData());
        return ResponseEntity.ok(appResponseDto);
    }
    //update certificate with id
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponseDto<GiftCertificateResponseDto>> update(
            @PathVariable Long id,
            @RequestBody @Valid GiftCertificateRequestDto requestDto
    ){
        AppResponseDto<GiftCertificateResponseDto> appResponseDto = giftCertificateService.update(id, requestDto);
        linkProvider.addLinkToGiftResponse(appResponseDto.getData());
        return ResponseEntity.ok(appResponseDto);
    }

    //Delete certificate by id
    @DeleteMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppResponseDto<Boolean>> delete(@PathVariable Long id){
        return ResponseEntity.ok(giftCertificateService.delete(id));
    }

    //get certificate page. Search by name + description and sort by fields
    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPage(
            @RequestParam(required = false, name = "search") String searchTerm,
            @RequestParam(required = false, name = "sort") String sortTerm,
            @RequestParam(required = false, name = "page", defaultValue = PAGE) int page,
            @RequestParam(required = false, name = "size", defaultValue = SIZE) int size

    ){
        AppResponseDto<List<GiftCertificateResponseDto>> appResponseDto = giftCertificateService.getList(searchTerm, sortTerm, page, size);
        //link Hateoas
        linkProvider.addLinkToGiftResponse(appResponseDto.getData().get(0));
        return ResponseEntity.ok(appResponseDto);
    }

    //get certificate page by the list of tag names with capability of sorting based on fields
    @GetMapping(value = "/tags", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPageByTagNames(
            @RequestParam(required = false, name = "name") List<String> tagNameList,
            @RequestParam(required = false, name = "sort") String sortTerm,
            @RequestParam(required = false, name = "page", defaultValue = PAGE) int page,
            @RequestParam(required = false, name = "size", defaultValue = SIZE) int size
    ){
        AppResponseDto<List<GiftCertificateResponseDto>> appResponseDto = giftCertificateService.getPageByTagList(tagNameList, sortTerm, page, size);
        //link Hateoas
        linkProvider.addLinkToGiftResponse(appResponseDto.getData().get(0));
        return ResponseEntity.ok(appResponseDto);
    }




}
