package com.epam.esm.controller;

import com.epam.esm.dto.request.TagRequestDto;
import com.epam.esm.dto.response.AppResponseDto;
import com.epam.esm.dto.response.TagResponseDto;
import com.epam.esm.link.LinkProvider;
import com.epam.esm.service.tag.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tags")
public class TagController {
    private final TagService tagService;
    private final LinkProvider linkProvider;
    private final static String PAGE = "0";
    private final static String SIZE = "10";

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(
            @RequestBody @Valid TagRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tagService.create(requestDto));
    }

    @GetMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> get(@PathVariable Long id) {
        return ResponseEntity.ok(tagService.get(id));
    }

    @DeleteMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return ResponseEntity.ok(tagService.delete(id));
    }

    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getList(
            @RequestParam(value = "page", required = false, defaultValue = PAGE) int page,
            @RequestParam(value = "size", required = false, defaultValue = SIZE) int size,
            @RequestParam(value = "sort", required = false) String sortTerm
            ) {
        AppResponseDto<List<TagResponseDto>> list = tagService.getList(page, size, sortTerm);
        linkProvider.addLinkToTagResponse(list);
        return ResponseEntity.ok(list);
    }
}
