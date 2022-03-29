package io.example.board.domain.rdb.member;

import io.example.board.domain.rdb.base.EnumType;
import lombok.Getter;

/**
 * @author : choi-ys
 * @date : 2022/03/29 10:47 오전
 */
@Getter
public enum MemberStatus implements EnumType {
    INVITED("가입 완료"),
    VERIFICATION_IN_PROGRESS("인증 진행 중"),
    VERIFICATION_FAILED("인증 실패"),
    ENABLED("사용 가능"),
    ACCEPTED_BUT_DISABLED("비활성화");

    private final String description;

    MemberStatus(String description) {
        this.description = description;
    }

    @Override
    public String getName() {
        return this.name();
    }
}
