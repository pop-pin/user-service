package com.poppin.userservice.service;

import com.poppin.userservice.config.JwtTokenProvider;
import com.poppin.userservice.dto.request.UpdateUserRequestDto;
import com.poppin.userservice.dto.response.KakaoLoginResponseDto;
import com.poppin.userservice.dto.response.KakaoUserResponseDto;
import com.poppin.userservice.dto.response.ResponseDto;
import com.poppin.userservice.dto.response.TokenResponseDto;
import com.poppin.userservice.entity.User;
import com.poppin.userservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Slf4j
@Service
public class UserService{

    public JwtTokenProvider jwtTokenProvider;
    public RedisTemplate<String, String> redisTemplate;

    public UserRepository userRepository;

    private final WebClient webClient;

    @Autowired
    public UserService(WebClient webClient, UserRepository userRepository, JwtTokenProvider jwtTokenProvider, RedisTemplate<String, String> redisTemplate) {
        this.webClient = webClient;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisTemplate = redisTemplate;
    }

    public KakaoLoginResponseDto kakaoLogin(String accessToken) {

        User user;

        //카카오 리소스 서버에서 정보 가져오기
        Mono<KakaoUserResponseDto> userInfoMono = getUserInfo(accessToken);
        KakaoUserResponseDto userInfo = userInfoMono.block();

        Optional<User> userData = userRepository.findByUsernumber(String.valueOf(userInfo.getId()));

        if(userData.isEmpty()){
            user = User.builder()
                    .usernumber(String.valueOf(userInfo.getId()))
                    .role("USER")
                    .email(userInfo.getEmail())
                    .nickname(userInfo.getNickname())
                    .build();

            userRepository.save(user);
        }

        Optional<User> userLoginData = userRepository.findByUsernumber(String.valueOf(userInfo.getId()));
        String refreshToken = "Bearer " +jwtTokenProvider.createRereshToken(userLoginData.get().getId());
        KakaoLoginResponseDto tokenResponseDto = KakaoLoginResponseDto.builder()
                .message("OK")
                .code(200)
                .accessToken("Bearer " +jwtTokenProvider.createAccessToken(
                        userLoginData.get().getId(),
                        String.valueOf(userLoginData.get().getRole())))
                .refreshToken(refreshToken)
                .userId(userLoginData.get().getId())
                .email(userLoginData.get().getEmail())
                .nickname(userLoginData.get().getNickname())
                .build();

        redisTemplate.opsForValue().set(String.valueOf(userLoginData.get().getId()),refreshToken);

        return tokenResponseDto;
    }

    public ResponseDto logout(Long userId) {
        deleteValueByKey(String.valueOf(userId));

        return ResponseDto.builder()
                .message("OK")
                .code(200)
                .build();
    }

    public TokenResponseDto reissueToken(String refreshToken, Long userId) {
        TokenResponseDto reissueTokenResponse;

        if(!jwtTokenProvider.validateRefreshToken(refreshToken)){

            reissueTokenResponse = TokenResponseDto.builder()
                    .code(417)
                    .message("재로그인하시오")
                    .build();

            return reissueTokenResponse;
        }

        String redisRefreshToken = redisTemplate.opsForValue().get(userId);

        if(redisRefreshToken.equals(refreshToken)){

            String userRole = String.valueOf(userRepository.findUserRole(userId));

            reissueTokenResponse= TokenResponseDto
                    .builder()
                    .code(200)
                    .message("OK")
                    .accessToken(jwtTokenProvider.createAccessToken(userId,userRole))
                    .refreshToken(refreshToken)
                    .build();

            return reissueTokenResponse;

        }

        reissueTokenResponse = TokenResponseDto.builder()
                .code(403)
                .message("접근이 올바르지 않습니다.")
                .build();

        return reissueTokenResponse;

    }

    public void deleteValueByKey(String key) {
        redisTemplate.delete(key);
    }

    public Mono<KakaoUserResponseDto> getUserInfo(String accessToken) {
        return webClient
                .get()
                .uri("https://kapi.kakao.com/v2/user/me") // 카카오 사용자 정보 엔드포인트
                .headers(headers -> headers.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(KakaoUserResponseDto.class);
    }

    public Long updateUser(User existedUser, UpdateUserRequestDto updateUserRequestDto) {
        existedUser.updateUser(updateUserRequestDto.getNickname(), updateUserRequestDto.getEmail());
        userRepository.save(existedUser);
        return existedUser.getId();
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }
}
