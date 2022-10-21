package com.epam.esm.service.gift;

import com.epam.esm.dto.request.GiftCertificateRequestDto;
import com.epam.esm.dto.response.AppResponseDto;
import com.epam.esm.dto.response.GiftCertificateResponseDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DataPersistenceException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.repository.GiftCertificateRepo;
import com.epam.esm.repository.TagRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final ModelMapper modelMapper;
    private final TagRepo tagRepo;
    private final GiftCertificateRepo giftCertificateRepo;

    @Override
    public AppResponseDto<GiftCertificateResponseDto> create(GiftCertificateRequestDto requestDto) {
        try {
            //map request body dto to entity object
            GiftCertificate giftCertificate = modelMapper.map(requestDto, GiftCertificate.class);
            // save tags if they are not exists in db, get them and set entity tag list property
            giftCertificate.setTags(
                    giftCertificate.getTags().stream()
                            .map(tag -> tagRepo.saveTagByNameIfNotExists(tag.getName()))
                            .collect(Collectors.toList()));
            // save gift certificate
            GiftCertificate save = giftCertificateRepo.save(giftCertificate);

            return new AppResponseDto<>(
                    HttpStatus.CREATED.value(),
                    "successfully created",
                    modelMapper.map(save, GiftCertificateResponseDto.class)
            );
        } catch (Exception e) {
            throw new DataPersistenceException("Error when gift certificate creation");
        }
    }

    @Override
    public AppResponseDto<GiftCertificateResponseDto> get(Long id) {
        GiftCertificate certificate = giftCertificateRepo.findById(id)
                .orElseThrow(() -> {
                    throw new ResourceNotFoundException(id);
                });

        return new AppResponseDto<>(
                HttpStatus.OK.value(),
                "gift certificate",
                modelMapper.map(certificate, GiftCertificateResponseDto.class)
        );
    }

    @Override
    public AppResponseDto<GiftCertificateResponseDto> update(Long id, GiftCertificateRequestDto type) {
        GiftCertificate certificate = giftCertificateRepo.findById(id)
                .orElseThrow(() -> {
                    throw new ResourceNotFoundException(id);
                });

        modelMapper.map(type, certificate);
        return new AppResponseDto<>(
                HttpStatus.OK.value(), "successfully updated",
                modelMapper.map(giftCertificateRepo.save(certificate), GiftCertificateResponseDto.class));
    }

    @Override
    public AppResponseDto<Boolean> delete(Long id) {
        boolean exists = giftCertificateRepo.existsById(id);
        if(!exists){
            throw new ResourceNotFoundException(id);
        }
        giftCertificateRepo.deleteById(id);
        return new AppResponseDto<>(HttpStatus.NO_CONTENT.value(), "successfully deleted", true);
    }

    @Override
    public AppResponseDto<List<GiftCertificateResponseDto>> getList(
            String searchTerm, String sortTerm, int page, int size
    ) {
        Sort multiSort = getSortingParams(sortTerm);
        PageRequest pageRequest = multiSort != null ? PageRequest.of(page, size, multiSort) : PageRequest.of(page, size);
        Page<GiftCertificate> certificatePage = giftCertificateRepo
                .searchGiftCertificateByNameContainingOrDescriptionContaining(searchTerm, searchTerm, pageRequest);

        return new AppResponseDto<>(HttpStatus.OK.value(),
                "gift certificate list",
                certificatePage.map(giftCertificate -> modelMapper.map(giftCertificate, GiftCertificateResponseDto.class)).toList()
        );
    }

    @Override
    public AppResponseDto<List<GiftCertificateResponseDto>> getPageByTagList(List<String> tagNameList, String sortingParams, int page, int size) {
        Sort multiSort = getSortingParams(sortingParams);
        PageRequest pageRequest = multiSort != null ? PageRequest.of(page, size, multiSort) : PageRequest.of(page, size);
        Page<GiftCertificate> certificatePage = giftCertificateRepo.getPageBySearchingTermAndSort(tagNameList, tagNameList.size(), pageRequest);

        return new AppResponseDto<>(HttpStatus.OK.value(),
            "gift certificate list",
                certificatePage.map(giftCertificate -> modelMapper.map(giftCertificate, GiftCertificateResponseDto.class)).toList()
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
                case "-duration":
                    tempSort = Sort.by("duration").descending();
                    break;
                case "duration":
                    tempSort = Sort.by("duration").ascending();
                    break;
                case "-price":
                    tempSort = Sort.by("price").descending();
                    break;
                case "price":
                    tempSort = Sort.by("price").ascending();
                    break;
                case "-create_date":
                    tempSort = Sort.by("create_date").descending();
                    break;
                case "create_date":
                    tempSort = Sort.by("create_date").ascending();
                    break;
                case "-last_update_date":
                    tempSort = Sort.by("last_update_date").descending();
                    break;
                case "last_update_date":
                    tempSort = Sort.by("last_update_date").ascending();
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
