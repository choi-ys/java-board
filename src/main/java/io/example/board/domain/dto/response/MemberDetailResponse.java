package io.example.board.domain.dto.response;

import io.example.board.domain.rdb.member.Member;
import io.example.board.domain.rdb.member.MemberStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author : choi-ys
 * @date : 2021/09/21 3:22 오전
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDetailResponse extends MemberSimpleResponse {

    private MemberStatus memberStatus;

    // * --------------------------------------------------------------
    // * Header : 도메인 생성
    // * @author : choi-ys
    // * @date : 2021/09/21 3:28 오전
    // * --------------------------------------------------------------
    private MemberDetailResponse(
        Long id,
        String email,
        String name,
        String nickname,
        MemberStatus memberStatus
    ) {
        super(id, email, name, nickname);
        this.memberStatus = memberStatus;
    }

    // * --------------------------------------------------------------
    // * Header : 객체 값 매핑
    // * @author : choi-ys
    // * @date : 2021/09/21 3:28 오전
    // * --------------------------------------------------------------
    public static MemberDetailResponse mapTo(Member member) {
        return new MemberDetailResponse(
            member.getId(),
            member.getEmail(),
            member.getName(),
            member.getNickname(),
            member.getMemberStatus()
        );
    }
}
