package com.epam.esm.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppResponseDto<T> {
    @JsonProperty("http_status")
    private int httpStatus;
    @JsonProperty("message")
    private String message;
    @JsonProperty("data")
    private T data;
}
