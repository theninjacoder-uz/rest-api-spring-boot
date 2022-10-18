package com.epam.esm.service;

import com.epam.esm.dto.response.AppResponseDto;

public interface BaseService <T, R>{

    AppResponseDto<R> create(T type);

    AppResponseDto<R> get(Long id);

    AppResponseDto<R> update(Long id, T type);

    AppResponseDto<Boolean> delete(Long id);
}
