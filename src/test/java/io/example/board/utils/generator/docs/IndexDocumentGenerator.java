package io.example.board.utils.generator.docs;

import io.example.board.utils.docs.DocsRelations;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static io.example.board.config.docs.ApiDocumentUtils.getDocumentRequest;
import static io.example.board.config.docs.ApiDocumentUtils.getDocumentResponse;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;

/**
 * @author : choi-ys
 * @date : 2021-09-28 오전 3:13
 */
public class IndexDocumentGenerator {

    public static RestDocumentationResultHandler generateIndexDocument() {
        return document("{class-name}/{method-name}",
                getDocumentRequest(),
                getDocumentResponse(),
                links(
                        linkWithRel(DocsRelations.INDEX.relation).description("목차 API link"),
                        linkWithRel(DocsRelations.INDEX.profileName()).description("목차 API 문서 link"),

                        linkWithRel(DocsRelations.CREATE_MEMBER.relation).description("회원가입 API link"),
                        linkWithRel(DocsRelations.CREATE_MEMBER.profileName()).description("회원가입 API 문서 link"),

                        linkWithRel(DocsRelations.LOGIN.relation).description("로그인 API link"),
                        linkWithRel(DocsRelations.LOGIN.profileName()).description("로그인 API 문서 link"),
                        linkWithRel(DocsRelations.REFRESH.relation).description("로그인 API link"),
                        linkWithRel(DocsRelations.REFRESH.profileName()).description("로그인 API link"),

                        linkWithRel(DocsRelations.CREATE_POST.relation).description("게시글 생성 API link"),
                        linkWithRel(DocsRelations.CREATE_POST.profileName()).description("게시글 생성 API 문서 link"),
                        linkWithRel(DocsRelations.GET_AN_POST.relation).description("게시글 조회 API link"),
                        linkWithRel(DocsRelations.GET_AN_POST.profileName()).description("게시글 조회 API 문서 link"),
                        linkWithRel(DocsRelations.UPDATE_AN_POST.relation).description("게시글 수정 API link"),
                        linkWithRel(DocsRelations.UPDATE_AN_POST.profileName()).description("게시글 API 문서 link"),
                        linkWithRel(DocsRelations.DELETE_AN_POST.relation).description("게시글 삭제 API link"),
                        linkWithRel(DocsRelations.DELETE_AN_POST.profileName()).description("게시글 삭제 API 문서 link")
                )
        );
    }
}
