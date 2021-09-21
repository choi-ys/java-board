package io.example.javaboard.service;

import io.example.javaboard.domain.dto.request.LoginRequest;
import io.example.javaboard.domain.dto.response.LoginResponse;
import io.example.javaboard.domain.member.Member;
import io.example.javaboard.repository.MemberRepo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author : choi-ys
 * @date : 2021/09/22 2:38 오전
 */
@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    private MemberRepo memberRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private LoginService loginService;

    @Test
    @DisplayName("로그인")
    public void login() {
        // Given
        String email = "project.log.062@gmail.com";
        String password = "password";
        String name = "choi-ys";
        String nickname = "whypie";
        Member member = new Member(email, password, name, nickname);

        given(memberRepo.findByEmail(anyString())).willReturn(Optional.of(member));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);

        LoginRequest loginRequest = new LoginRequest(email, password);

        // When
        LoginResponse loginResponse = loginService.login(loginRequest);

        // Then
        verify(memberRepo, times(1)).findByEmail(anyString());
        verify(passwordEncoder, times(1)).matches(anyString(), anyString());

        assertAll(
                () -> assertEquals(loginResponse.getEmail(), email),
                () -> assertEquals(loginResponse.getName(), name),
                () -> assertEquals(loginResponse.getNickname(), nickname),
                () -> assertEquals(loginResponse.getRoles(), member.getRoles())
        );
    }
}