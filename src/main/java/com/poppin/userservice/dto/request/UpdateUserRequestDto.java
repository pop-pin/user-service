package com.poppin.userservice.dto.request;

import lombok.Getter;

@Getter
public class UpdateUserRequestDto {
    private String email;
    private String nickname;
}
