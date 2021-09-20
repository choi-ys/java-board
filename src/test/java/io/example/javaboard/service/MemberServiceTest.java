package io.example.javaboard.service;

import io.example.javaboard.domain.dto.request.SignupRequest;
import io.example.javaboard.domain.member.Member;
import io.example.javaboard.repository.MemberRepo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author : choi-ys
 * @date : 2021/09/21 3:32 오전
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Service:Member")
class MemberServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MemberRepo memberRepo;

    @InjectMocks
    private MemberService memberService;

    @Test
    @DisplayName("회원 가입")
    public void signup() {
        // Given
        String email = "project.log.062@gmail.com";
        String password = "password";
        String name = "choi-ys";
        String nickname = "whypie";

        SignupRequest signupRequest = new SignupRequest(email, password, name, nickname);

        given(memberRepo.save(any(Member.class))).will(AdditionalAnswers.returnsFirstArg());

        // When
        memberService.signup(signupRequest);

        // Then
        verify(memberRepo, times(1)).existsByEmail(any(String.class));
        verify(passwordEncoder, times(1)).encode(any(String.class));
        verify(memberRepo, times(1)).save(any(Member.class));
    }
}