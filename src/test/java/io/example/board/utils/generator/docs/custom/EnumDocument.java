package io.example.board.utils.generator.docs.custom;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author : choi-ys
 * @date : 2022/03/29 11:42 오전
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EnumDocument {

    private Map<String, String> memberStatus;

    public EnumDocument(Map<String, String> memberStatus) {
        this.memberStatus = memberStatus;
    }

    public static EnumDocument of(Map<String, String> memberStatus) {
        return new EnumDocument(memberStatus);
    }
}
