package io.example.board.utils.generator.docs.common;

/**
 * @author : choi-ys
 * @date : 2022/05/03 12:00 오후
 */
public class EnumDocumentLinkGenerator {

    public static String enumLinkGenerator(TargetEnum targetEnum) {
        return String
            .format("link:enums/%s.html[%s 코드, role=\"popup\"]", targetEnum.htmlFileName, targetEnum.linkName);
    }

    public enum TargetEnum {
        MEMBER_STATUS("member-status", "회원 상태");

        private final String htmlFileName;
        private final String linkName;

        TargetEnum(String htmlFileName, String linkName) {
            this.htmlFileName = htmlFileName;
            this.linkName = linkName;
        }
    }

}
