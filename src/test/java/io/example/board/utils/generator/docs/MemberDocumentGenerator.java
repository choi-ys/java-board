package io.example.board.utils.generator.docs;

import static io.example.board.config.docs.ApiDocumentUtils.createDocument;
import static io.example.board.utils.generator.docs.common.EnumDocumentLinkGenerator.TargetEnum.MEMBER_STATUS;
import static io.example.board.utils.generator.docs.common.EnumDocumentLinkGenerator.enumLinkGenerator;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

/**
 * @author : choi-ys
 * @date : 2021-09-29 오전 12:52
 */
public class MemberDocumentGenerator {

    public static RestDocumentationResultHandler generateSignupMemberDocument() {
        return createDocument(
            links(
                linkWithRel("self").description("생성된 리소스의 link 정보"),
                linkWithRel("login").description("로그인 리소스의 link 정보")
            ),
            requestHeaders(
                headerWithName(HttpHeaders.ACCEPT).description("accept type header"),
                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
            ),
            requestFields(
                fieldWithPath("email").description("사용자 이메일 : 각 회원을 구분하는 식별자"),
                fieldWithPath("password").description("사용자 비밀번호"),
                fieldWithPath("name").description("사용자 이름"),
                fieldWithPath("nickname").description("사용자 닉네임")
            ),
            responseHeaders(
                headerWithName(HttpHeaders.LOCATION).description("Location header"),
                headerWithName(HttpHeaders.CONTENT_TYPE).description("Response content type")
            ),
            responseFields(
                fieldWithPath("id").description("사용자 ID"),
                fieldWithPath("email").description("사용자 이메일"),
                fieldWithPath("name").description("사용자 이름"),
                fieldWithPath("nickname").description("사용자 닉네임"),
                fieldWithPath("_links.self.href").description("생성된 리소스의 link 정보"),
                fieldWithPath("_links.login.href").description("로그인 리소스의 link 정보")
            )
        );
    }

    public static RestDocumentationResultHandler generateGetAnMemberDocument() {
        return createDocument(
            requestHeaders(
                headerWithName(HttpHeaders.ACCEPT).description("accept type header"),
                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header"),
                headerWithName(AUTHORIZATION).description("access token header")
            ),
            pathParameters(
                parameterWithName("id").description("사용자 ID")
            ),
            responseHeaders(
                headerWithName(HttpHeaders.CONTENT_TYPE).description("Response content type")
            ),
            responseFields(
                fieldWithPath("id").description("사용자 ID"),
                fieldWithPath("email").description("사용자 이메일"),
                fieldWithPath("name").description("사용자 이름"),
                fieldWithPath("nickname").description("사용자 닉네임"),
                fieldWithPath("memberStatus").description(enumLinkGenerator(MEMBER_STATUS))
            )
        );
    }
}
