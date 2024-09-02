package com.study.SpringSecurity.dto.response;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class RespJwtDto {
    private String accessToken;
    private String refreshToken;
}
