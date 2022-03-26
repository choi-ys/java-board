package io.example.board.utils.generator.docs;

import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static io.example.board.config.docs.ApiDocumentUtils.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

/**
 * @author : choi-ys
 * @date : 2021-09-29 오전 12:52
 */
public class LoginDocumentGenerator {

    public static RestDocumentationResultHandler generateLoginDocument() {
        return createDocument(
                links(
                        linkWithRel("refresh").description("토큰 갱신 리소스의 link 정보")
                ),
                requestHeaders(
                        headerWithName(HttpHeaders.ACCEPT).description("accept type header"),
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                ),
                requestFields(
                        fieldWithPath("email").description("사용자 이메일"),
                        fieldWithPath("password").description("사용자 비밀번호")
                ),
                responseHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Response content type")
                ),
                responseFields(
                        fieldWithPath("id").description("사용자 ID"),
                        fieldWithPath("email").description("사용자 이메일"),
                        fieldWithPath("name").description("사용자 이름"),
                        fieldWithPath("nickname").description("사용자 닉네임"),
                        fieldWithPath("token.accessToken").description("접근 토큰 정보"),
                        fieldWithPath("token.accessExpired").description("접근 토큰 만료 일시"),
                        fieldWithPath("token.refreshToken").description("갱신 토큰 정보"),
                        fieldWithPath("token.refreshExpired").description("갱신 토큰 만료 일시"),
                        fieldWithPath("_links.refresh.href").description("생성된 리소스의 link 정보")
                )
        );
    }

    public static RestDocumentationResultHandler generateRefreshDocument() {
        return createDocument(
                requestHeaders(
                        headerWithName(HttpHeaders.ACCEPT).description("accept type header"),
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header"),
                        headerWithName(AUTHORIZATION).description("access token header")
                ),
                responseHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Response content type")
                ),
                responseFields(
                        fieldWithPath("accessToken").description("접근 토큰 정보"),
                        fieldWithPath("accessExpired").description("접근 토큰 만료 일시"),
                        fieldWithPath("refreshToken").description("갱신 토큰 정보"),
                        fieldWithPath("refreshExpired").description("갱신 토큰 만료 일시")
                )
        );
    }
}
