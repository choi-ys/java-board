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
import static org.springframework.restdocs.request.RequestDocumentation.*;

/**
 * @author : choi-ys
 * @date : 2021-09-28 오전 5:40
 */
public class PostDocumentGenerator {
    public static RestDocumentationResultHandler generateCreatePostDocument() {
        return createDocument(
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
                        fieldWithPath("title").description("게시글 제목").attributes(format("1~10자의 문자열")),
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

    public static RestDocumentationResultHandler generateGetAnPostDocument() {
        return createDocument(
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

    public static RestDocumentationResultHandler generateUpdateAnPostDocument() {
        return createDocument(
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

    public static RestDocumentationResultHandler generateDeleteAnPostDocument() {
        return createDocument(
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

    public static RestDocumentationResultHandler generateSearchPostDocument() {
        return createDocument(
                requestHeaders(
                        headerWithName(HttpHeaders.ACCEPT).description("accept type header"),
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                ),
                requestParameters(
                        parameterWithName("title").description("게시글 제목"),
                        parameterWithName("content").description("게시글 내용"),
                        parameterWithName("writerName").description("게시글 작성자"),
                        parameterWithName("page").description("요청 페이지 번호"),
                        parameterWithName("size").description("페이지당 항목 수"),
                        parameterWithName("sort").description("정렬 기준 : 정렬 항목 명"),
                        parameterWithName("direction").description("정렬 기준 : 순서(ASC, DESC)")
                ),
                responseHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Response content type")
                ),
                responseFields(
                        fieldWithPath("totalPages").description("전페 페이지 수"),
                        fieldWithPath("totalElementCount").description("전체 요소 수"),
                        fieldWithPath("currentPage").description("현제 페이지 번호"),
                        fieldWithPath("currentElementCount").description("현재 페이지의 요수 수"),
                        fieldWithPath("perPageNumber").description("페이지당 요소 수"),
                        fieldWithPath("firstPage").description("첫 페이지 여부"),
                        fieldWithPath("lastPage").description("마지막 페이지 여부"),
                        fieldWithPath("hasNextPage").description("다음 페이지 존재 여부"),
                        fieldWithPath("hasPrevious").description("이전 페이지 존재 여부"),
                        fieldWithPath("embedded").description("응답 본문 배열"),
                        fieldWithPath("embedded[0].id").description("게시글 ID"),
                        fieldWithPath("embedded[0].title").description("게시글 제목"),
                        fieldWithPath("embedded[0].content").description("게시글 내용"),
                        fieldWithPath("embedded[0].viewCount").description("게시글 조회 수"),
                        fieldWithPath("embedded[0].createdAt").description("게시글 생성일"),
                        fieldWithPath("embedded[0].updatedAt").description("게시글 수정일"),
                        fieldWithPath("embedded[0].writer").description("게시글 작성자 정보"),
                        fieldWithPath("embedded[0].writer.id").description("게시글 작성자 ID"),
                        fieldWithPath("embedded[0].writer.email").description("게시글 작성자 이메일"),
                        fieldWithPath("embedded[0].writer.name").description("게시글 작성자 이름"),
                        fieldWithPath("embedded[0].writer.nickname").description("게시글 작성자 닉네임"),
                        fieldWithPath("embedded[0].comments").description("게시글 댓글 배열"),
                        fieldWithPath("embedded[0].comments[0].id").description("댓글 ID"),
                        fieldWithPath("embedded[0].comments[0].postId").description("댓글이 작성된 게시글 ID"),
                        fieldWithPath("embedded[0].comments[0].content").description("댓글 내용"),
                        fieldWithPath("embedded[0].comments[0].writer").description("댓글 작성자 정보"),
                        fieldWithPath("embedded[0].comments[0].writer.id").description("댓글 작성자 ID"),
                        fieldWithPath("embedded[0].comments[0].writer.email").description("댓글 작성자 이메일"),
                        fieldWithPath("embedded[0].comments[0].writer.name").description("댓글 작성자 이름"),
                        fieldWithPath("embedded[0].comments[0].writer.nickname").description("댓글 작성자 이름"),
                        fieldWithPath("embedded[0].comments[0].createdAt").description("댓글 생성일"),
                        fieldWithPath("embedded[0].comments[0].updatedAt").description("댓글 수정일")
                )
        );
    }
}
