package com.epam.esm.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto extends RepresentationModel<OrderResponseDto> {
    private long id;
    @JsonProperty("user")
    private UserResponseDto userResponseDto;
    @JsonProperty("gift_certificate")
    private GiftCertificateResponseDto giftCertificateResponseDto;
    private BigDecimal price;
    @JsonProperty("purchase_date")
    private LocalDateTime createDate;
}
