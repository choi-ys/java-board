package io.example.board.service;

import io.example.board.advice.exception.ResourceNotFoundException;
import io.example.board.domain.dto.request.SignupRequest;
import io.example.board.domain.dto.response.MemberDetailResponse;
import io.example.board.domain.dto.response.MemberSimpleResponse;
import io.example.board.domain.rdb.member.Member;
import io.example.board.repository.rdb.member.MemberRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : choi-ys
 * @date : 2021/09/21 3:20 오전
 */
@Service
@Transactional(readOnly = true)
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepo memberRepo;

    public MemberService(PasswordEncoder passwordEncoder, MemberRepo memberRepo) {
        this.passwordEncoder = passwordEncoder;
        this.memberRepo = memberRepo;
    }

    @Transactional
    public MemberSimpleResponse signup(SignupRequest signupRequest) {
        if (memberRepo.existsByEmail(signupRequest.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일 입니다.");
        }
        Member save = memberRepo.save(signupRequest.toEntity(passwordEncoder));
        return MemberSimpleResponse.mapTo(save);
    }

    @Transactional
    public MemberDetailResponse getAnMember(long memberId) {
        return MemberDetailResponse.mapTo(memberRepo.findById(memberId).orElseThrow(ResourceNotFoundException::new));
    }
}
