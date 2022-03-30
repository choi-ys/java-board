package io.example.board.utils.generator.docs.common;

import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

import static io.example.board.config.docs.ApiDocumentUtils.createDocument;
import static io.example.board.utils.generator.docs.common.CommonFieldDescriptor.commonPaginationFieldWithPath;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

/**
 * @author : choi-ys
 * @date : 2022/03/30 8:50 오전
 */
public class PaginationDocumentGenerator {
    public static RestDocumentationResultHandler generateCommonPaginationDocument() {
        return createDocument(
                requestHeaders(
                        headerWithName(HttpHeaders.ACCEPT).description("accept type header"),
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                ),
                responseHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Response content type")
                ),
                responseFields(
                        commonPaginationFieldWithPath()
                )
        );
    }
}
