package com.poppin.userservice.controller;

import com.poppin.userservice.config.JwtTokenProvider;
import com.poppin.userservice.dto.response.ResponseDto;
import com.poppin.userservice.dto.response.TokenResponseDto;
import com.poppin.userservice.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserController(UserService userService, JwtTokenProvider jwtTokenProvider){
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping(value = "/kakao-login")
    public TokenResponseDto kakaoLogin(HttpServletRequest request)  {

        String accessToken = jwtTokenProvider.resolveToken(request);
        TokenResponseDto reissueTokenResponseDto = userService.kakaoLogin(accessToken);
        LOGGER.info("카카오 로그인 완료");

        return reissueTokenResponseDto;
    }

    @DeleteMapping("/logout")
    public ResponseDto logout(HttpServletRequest request)  {
        String userId = jwtTokenProvider.getUserId(request);
        ResponseDto logoutResponseDto = userService.logout(Long.valueOf(userId));
        LOGGER.info("로그아웃 완료");

        return logoutResponseDto;
    }

    @GetMapping(value = "/reissue-token")
    public TokenResponseDto reissueToken(HttpServletRequest request)  {

        String userId = jwtTokenProvider.getUserId(request);
        String refreshToken = jwtTokenProvider.resolveToken(request);
        TokenResponseDto reissueTokenResponseDto = userService.reissueToken(refreshToken, Long.valueOf(userId));
        LOGGER.info("토큰 재발급 완료");

        return reissueTokenResponseDto;
    }

}
