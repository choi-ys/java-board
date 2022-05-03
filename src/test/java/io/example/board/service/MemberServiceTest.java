package io.example.board.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import io.example.board.domain.dto.request.SignupRequest;
import io.example.board.domain.dto.response.MemberDetailResponse;
import io.example.board.domain.rdb.member.Member;
import io.example.board.domain.rdb.member.MemberStatus;
import io.example.board.repository.rdb.member.MemberRepo;
import io.example.board.utils.generator.mock.MemberGenerator;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

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
        SignupRequest signupRequest = MemberGenerator.signupRequest();
        given(memberRepo.save(any(Member.class))).will(AdditionalAnswers.returnsFirstArg());

        // When
        memberService.signup(signupRequest);

        // Then
        verify(memberRepo, times(1)).existsByEmail(any(String.class));
        verify(passwordEncoder, times(1)).encode(any(String.class));
        verify(memberRepo, times(1)).save(any(Member.class));
    }

    @Test
    @DisplayName("회원 번호로 특정 회원 조회")
    public void getAnMemberTest() {
        // Given
        given(memberRepo.findById(anyLong())).willReturn(Optional.of(MemberGenerator.member()));

        // When
        MemberDetailResponse actual = memberService.getAnMember(0L);

        // Then
        verify(memberRepo).findById(anyLong());
        assertAll(
            () -> assertThat(actual.getMemberStatus()).isEqualTo(MemberStatus.ACCEPTED_BUT_DISABLED)
        );
    }
}