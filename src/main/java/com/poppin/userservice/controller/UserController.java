package com.poppin.userservice.controller;

import com.poppin.userservice.config.JwtTokenProvider;
import com.poppin.userservice.dto.request.UpdateUserRequestDto;
import com.poppin.userservice.dto.response.KakaoLoginResponseDto;
import com.poppin.userservice.dto.response.ResponseDto;
import com.poppin.userservice.dto.response.TokenResponseDto;
import com.poppin.userservice.entity.User;
import com.poppin.userservice.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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

    //카카오 로그인
    @PostMapping(value = "/kakao-login")
    public KakaoLoginResponseDto kakaoLogin(HttpServletRequest request)  {

        String accessToken = jwtTokenProvider.resolveToken(request);
        KakaoLoginResponseDto kakaoLoginResponseDto = userService.kakaoLogin(accessToken);
        LOGGER.info("카카오 로그인 완료");

        return kakaoLoginResponseDto;
    }

    //로그아웃
    @DeleteMapping("/logout")
    public ResponseDto logout(HttpServletRequest request)  {
        String userId = jwtTokenProvider.getUserId(request);
        ResponseDto logoutResponseDto = userService.logout(Long.valueOf(userId));
        LOGGER.info("로그아웃 완료");

        return logoutResponseDto;
    }

    //액세스 토큰 재발급
    @GetMapping(value = "/reissue-token")
    public TokenResponseDto reissueToken(HttpServletRequest request)  {

        String userId = jwtTokenProvider.getUserId(request);
        String refreshToken = jwtTokenProvider.resolveToken(request);
        TokenResponseDto reissueTokenResponseDto = userService.reissueToken(refreshToken, Long.valueOf(userId));
        LOGGER.info("토큰 재발급 완료");

        return reissueTokenResponseDto;
    }

    //유저 정보 변경
    @PatchMapping("")
    public ResponseEntity<Long> updateUser(@RequestParam("user_id")Long userId, @RequestBody UpdateUserRequestDto updateUserRequestDto) {
        Optional<User> existingUser = userService.findUserById(userId);
        if (existingUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Long updatedId = userService.updateUser(existingUser.get(),updateUserRequestDto);
        return ResponseEntity.ok(updatedId);
    }

}
