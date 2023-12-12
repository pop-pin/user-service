package com.poppin.userservice.dto.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Getter
public class TokenResponseDto {

    private int code;

    private String message;

    private String accessToken;

    private String refreshToken;
}
