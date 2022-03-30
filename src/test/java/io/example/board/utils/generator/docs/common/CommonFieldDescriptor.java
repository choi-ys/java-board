package io.example.board.utils.generator.docs.common;

import org.springframework.restdocs.payload.FieldDescriptor;

import java.util.Arrays;
import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

/**
 * @author : choi-ys
 * @date : 2022/03/28 4:31 오후
 */
public class CommonFieldDescriptor {

    public static List<FieldDescriptor> commonErrorFieldWithPath() {
        return Arrays.asList(
                fieldWithPath("timestamp").description("에러 일시"),
                fieldWithPath("code").description("에러 뷴류 코드"),
                fieldWithPath("message").description("에러 메세지"),
                fieldWithPath("method").description("요청 HTTP Method"),
                fieldWithPath("path").description("요청 URL")
        );
    }

    public static List<FieldDescriptor> invalidErrorFieldWithPath() {
        return Arrays.asList(
                fieldWithPath("errorDetails[*]").description("에러 상세 정보 배열"),
                fieldWithPath("errorDetails[*].object").description("에러 객체명"),
                fieldWithPath("errorDetails[*].field").description("에러 필드"),
                fieldWithPath("errorDetails[*].code").description("에러 상세 코드"),
                fieldWithPath("errorDetails[*].rejectMessage").description("에러 사유"),
                fieldWithPath("errorDetails[*].rejectedValue").description("에러 발생값")
        );
    }

    public static List<FieldDescriptor> commonPaginationFieldWithPath() {
        return Arrays.asList(
                fieldWithPath("totalPages").description("전페 페이지 수"),
                fieldWithPath("totalElementCount").description("전체 요소 수"),
                fieldWithPath("currentPage").description("현제 페이지 번호"),
                fieldWithPath("currentElementCount").description("현재 페이지의 요수 수"),
                fieldWithPath("perPageNumber").description("페이지당 요소 수"),
                fieldWithPath("firstPage").description("첫 페이지 여부"),
                fieldWithPath("lastPage").description("마지막 페이지 여부"),
                fieldWithPath("hasNextPage").description("다음 페이지 존재 여부"),
                fieldWithPath("hasPrevious").description("이전 페이지 존재 여부"),
                fieldWithPath("embedded[*]").description("응답 본문 배열")
        );
    }
}
