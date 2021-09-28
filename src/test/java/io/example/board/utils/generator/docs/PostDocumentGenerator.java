package io.example.board.utils.generator.docs;

import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

import static io.example.board.config.docs.ApiDocumentUtils.getDocumentRequest;
import static io.example.board.config.docs.ApiDocumentUtils.getDocumentResponse;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

/**
 * @author : choi-ys
 * @date : 2021-09-28 오전 5:40
 */
public class PostDocumentGenerator {
    public static RestDocumentationResultHandler generateCreatePostDocument() {
        return document("{class-name}/{method-name}",
                getDocumentRequest(),
                getDocumentResponse(),
                links(
                        linkWithRel("self").description("생성된 리소스의 link 정보"),
                        linkWithRel("profile").description("API 목차 link 정보")
                ),
                requestHeaders(
                        headerWithName(HttpHeaders.ACCEPT).description("accept type header"),
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header"),
                        headerWithName(AUTHORIZATION).description("access token header")
                ),
                requestFields(
                        fieldWithPath("title").description("게시글 제목"),
                        fieldWithPath("content").description("게시글 본문")
                ),
                responseHeaders(
                        headerWithName(HttpHeaders.LOCATION).description("Location header"),
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Response content type")
                ),
                responseFields(
                        fieldWithPath("id").description("게시글 ID"),
                        fieldWithPath("title").description("게시글 제목"),
                        fieldWithPath("content").description("게시글 본문"),
                        fieldWithPath("viewCount").description("게시글 조회수"),
                        fieldWithPath("display").description("게시글의 전시 여부"),
                        fieldWithPath("writer.id").description("작성자 ID"),
                        fieldWithPath("writer.email").description("작성자 이메일"),
                        fieldWithPath("writer.name").description("작성자 이름"),
                        fieldWithPath("writer.nickname").description("작성자 닉네임"),
                        fieldWithPath("_links.self.href").description("생성된 리소스의 link 정보"),
                        fieldWithPath("_links.profile.href").description("API 목차 link 정보")
                )
        );
    }

    public static RestDocumentationResultHandler generateGetAnPostDocument(){
        return document("{class-name}/{method-name}",
                getDocumentRequest(),
                getDocumentResponse(),
                requestHeaders(
                        headerWithName(HttpHeaders.ACCEPT).description("accept type header"),
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                ),
                pathParameters(
                        parameterWithName("id").description("게시글 ID")
                ),
                responseHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Response content type")
                ),
                responseFields(
                        fieldWithPath("id").description("게시글 ID"),
                        fieldWithPath("title").description("게시글 제목"),
                        fieldWithPath("content").description("게시글 본문"),
                        fieldWithPath("viewCount").description("게시글 조회수"),
                        fieldWithPath("display").description("게시글의 전시 여부"),
                        fieldWithPath("writer.id").description("작성자 ID"),
                        fieldWithPath("writer.email").description("작성자 이메일"),
                        fieldWithPath("writer.name").description("작성자 이름"),
                        fieldWithPath("writer.nickname").description("작성자 닉네임")
                )
        );
    }

    public static RestDocumentationResultHandler generateUpdateAnPostDocument(){
        return document("{class-name}/{method-name}",
                getDocumentRequest(),
                getDocumentResponse(),
                requestHeaders(
                        headerWithName(HttpHeaders.ACCEPT).description("accept type header"),
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header"),
                        headerWithName(AUTHORIZATION).description("access token header")
                ),
                pathParameters(
                        parameterWithName("id").description("게시글 ID")
                ),
                requestFields(
                        fieldWithPath("id").description("게시글 ID"),
                        fieldWithPath("title").description("게시글 제목"),
                        fieldWithPath("content").description("게시글 본문"),
                        fieldWithPath("display").description("전시 여부")
                ),
                responseHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Response content type")
                ),
                responseFields(
                        fieldWithPath("id").description("게시글 ID"),
                        fieldWithPath("title").description("게시글 제목"),
                        fieldWithPath("content").description("게시글 본문"),
                        fieldWithPath("viewCount").description("게시글 조회수"),
                        fieldWithPath("display").description("게시글의 전시 여부"),
                        fieldWithPath("writer.id").description("작성자 ID"),
                        fieldWithPath("writer.email").description("작성자 이메일"),
                        fieldWithPath("writer.name").description("작성자 이름"),
                        fieldWithPath("writer.nickname").description("작성자 닉네임")
                )
        );
    }

    public static RestDocumentationResultHandler generateDeleteAnPostDocument(){
        return document("{class-name}/{method-name}",
                getDocumentRequest(),
                getDocumentResponse(),
                requestHeaders(
                        headerWithName(HttpHeaders.ACCEPT).description("accept type header"),
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header"),
                        headerWithName(AUTHORIZATION).description("access token header")
                ),
                pathParameters(
                        parameterWithName("id").description("게시글 ID")
                )
        );
    }
}
