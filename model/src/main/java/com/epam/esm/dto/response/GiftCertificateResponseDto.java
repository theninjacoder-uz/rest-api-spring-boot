package com.epam.esm.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GiftCertificateResponseDto {

    private long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer duration;
    private List<TagResponseDto> tags;
    @JsonProperty("create_date")
    private LocalDateTime createDate;
    @JsonProperty("last_update_date")
    private LocalDateTime lastUpdateDate;
}
