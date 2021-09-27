package io.example.board.utils.generator.mock;

import io.example.board.domain.dto.request.SignupRequest;
import io.example.board.domain.rdb.member.Member;
import io.example.board.domain.vo.login.LoginUserAdapter;
import io.example.board.repository.rdb.member.MemberRepo;
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

    public static String secondMemberEmail = "ys.choi@naver.com";
    public static final String secondMemberPassword = "password";
    public static final String secondMemberName = "choi-ys";
    public static final String secondMemberNickname = "whypie";

    public static Member member() {
        return new Member(email, password, name, nickname);
    }

    public static Member secondMember() {
        return new Member(secondMemberEmail, secondMemberPassword, secondMemberName, secondMemberNickname);
    }

    public static SignupRequest signupRequest() {
        return new SignupRequest(email, password, name, nickname);
    }

    public static LoginUserAdapter loginUserAdapter() {
        Member member = member();
        return new LoginUserAdapter(member.getEmail(), member.mapToSimpleGrantedAuthority());
    }
}
