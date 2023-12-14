package com.poppin.userservice.dto.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Getter
public class KakaoLoginResponseDto {

    private int code;
    private String message;
    private String accessToken;
    private String refreshToken;
    private Long userId;
    private String email;
    private String nickname;
}
