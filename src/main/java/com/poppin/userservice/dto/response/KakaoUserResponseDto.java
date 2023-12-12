package com.poppin.userservice.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class KakaoUserResponseDto {

    private long id;
    private String email;
    private String nickname;

    @JsonCreator
    public KakaoUserResponseDto(@JsonProperty("id") long id,
                                @JsonProperty("account_email") String email,
                                @JsonProperty("profile_nickname") String nickname) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
    }
}
