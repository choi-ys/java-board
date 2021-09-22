package io.example.board.utils.generator;

import io.example.board.domain.dto.request.SignupRequest;
import io.example.board.domain.dto.response.MemberSimpleResponse;
import io.example.board.domain.member.Member;
import io.example.board.repository.MemberRepo;
import io.example.board.service.MemberService;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.test.context.TestConstructor;

/**
 * @author : choi-ys
 * @date : 2021/09/23 12:58 오전
 */
@TestComponent
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class MemberGenerator {

    private final MemberRepo memberRepo;

    public MemberGenerator(MemberRepo memberRepo) {
        this.memberRepo = memberRepo;
    }

    public Member savedMember() {
        return memberRepo.saveAndFlush(member());
    }

    public static String email = "project.log.062@gmail.com";
    public static final String password = "password";
    public static final String name = "choi-ys";
    public static final String nickname = "whypie";

    public static Member member() {
        return new Member(email, password, name, nickname);
    }

    public static SignupRequest signupRequest() {
        return new SignupRequest(email, password, name, nickname);
    }
}
