package io.example.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.example.board.config.test.SpringBootTestConfig;
import io.example.board.domain.dto.request.PostCreateRequest;
import io.example.board.domain.dto.request.PostUpdateRequest;
import io.example.board.domain.dto.response.error.ErrorCode;
import io.example.board.domain.rdb.member.Member;
import io.example.board.domain.rdb.post.Post;
import io.example.board.domain.vo.token.Token;
import io.example.board.utils.generator.mock.MemberGenerator;
import io.example.board.utils.generator.mock.PostGenerator;
import io.example.board.utils.generator.mock.TokenGenerator;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static io.example.board.utils.generator.docs.PostDocumentGenerator.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author : choi-ys
 * @date : 2021-09-27 오전 1:05
 */
@SpringBootTestConfig
@Import({TokenGenerator.class, MemberGenerator.class, PostGenerator.class})
@AutoConfigureRestDocs
@DisplayName("API:Post")
class PostControllerTest {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final MemberGenerator memberGenerator;
    private final TokenGenerator tokenGenerator;
    private final PostGenerator postGenerator;

    public PostControllerTest(MockMvc mockMvc, ObjectMapper objectMapper, MemberGenerator memberGenerator,
                              TokenGenerator tokenGenerator, PostGenerator postGenerator) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.memberGenerator = memberGenerator;
        this.tokenGenerator = tokenGenerator;
        this.postGenerator = postGenerator;
    }

    private final String POST_URL = "/post";

    @Test
    @DisplayName("[201:POST]게시글 생성")
    public void create() throws Exception {
        // Given
        Token token = tokenGenerator.generateToken();
        PostCreateRequest postCreateRequest = PostGenerator.postRequest();

        // When
        ResultActions resultActions = this.mockMvc.perform(post(POST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(this.objectMapper.writeValueAsString(postCreateRequest))
                .header(AUTHORIZATION, TokenGenerator.getBearerToken(token.getAccessToken()))
        );

        // Then
        resultActions.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().exists(HttpHeaders.CONTENT_TYPE))
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("title").value(postCreateRequest.getTitle()))
                .andExpect(jsonPath("content").value(postCreateRequest.getContent()))
                .andExpect(jsonPath("viewCount").value(0L))
                .andExpect(jsonPath("display").value(true))
                .andExpect(jsonPath("writer").isNotEmpty())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(generateCreatePostDocument())
        ;
    }

    @Test
    @DisplayName("[400:POST]게시글 생성 실패(값이 없는 요청)")
    public void create_Fail_Cause_NoArgument() throws Exception {
        // Given
        Token token = tokenGenerator.generateToken();

        // When
        ResultActions resultActions = this.mockMvc.perform(post(POST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .header(AUTHORIZATION, TokenGenerator.getBearerToken(token.getAccessToken()))
        );

        // Then
        resultActions.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("timestamp").exists())
                .andExpect(jsonPath("method").exists())
                .andExpect(jsonPath("path").exists())
                .andExpect(jsonPath("code").value(ErrorCode.HTTP_MESSAGE_NOT_READABLE.name()))
                .andExpect(jsonPath("message").value(ErrorCode.HTTP_MESSAGE_NOT_READABLE.message))
        ;
    }

    @Test
    @DisplayName("[400:POST]게시글 생성 실패(값이 잘못된 요청)")
    @Disabled
    public void create_Fail_Cause_InvalidArgument() throws Exception {
        // Given
        Token token = tokenGenerator.generateToken();
        PostCreateRequest postCreateRequest = new PostCreateRequest("", "");

        // When
        ResultActions resultActions = this.mockMvc.perform(post(POST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(this.objectMapper.writeValueAsString(postCreateRequest))
                .header(AUTHORIZATION, TokenGenerator.getBearerToken(token.getAccessToken()))
        );

        // Then
        resultActions.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("timestamp").exists())
                .andExpect(jsonPath("method").exists())
                .andExpect(jsonPath("path").exists())
                .andExpect(jsonPath("code").value(ErrorCode.METHOD_ARGUMENT_NOT_VALID.name()))
                .andExpect(jsonPath("message").value(ErrorCode.METHOD_ARGUMENT_NOT_VALID.message))
                .andExpect(jsonPath("errorDetails").isNotEmpty())
        ;
    }

    @Test
    @DisplayName("[403:POST]게시글 생성 실패(권한이 없는 요청)")
    public void create_Fail_Cause_NoCredentials() throws Exception {
        // Given
        PostCreateRequest postCreateRequest = PostGenerator.postRequest();

        // When
        ResultActions resultActions = this.mockMvc.perform(post(POST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(this.objectMapper.writeValueAsString(postCreateRequest))
        );

        // Then
        resultActions.andDo(print())
                .andExpect(status().isForbidden())
        ;
    }

    @Test
    @DisplayName("[200:GET]전시 상태의 게시글 조회")
    public void findByIdAndDisplayTrue() throws Exception {
        // Given
        Post savedPost = postGenerator.savedPost();
        String urlTemplate = POST_URL + "/{id}";

        // When
        ResultActions resultActions = this.mockMvc.perform(RestDocumentationRequestBuilders.get(urlTemplate, savedPost.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
        );

        // Then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(savedPost.getId()))
                .andExpect(jsonPath("title").value(savedPost.getTitle()))
                .andExpect(jsonPath("content").value(savedPost.getContent()))
                .andExpect(jsonPath("viewCount").value(savedPost.getViewCount()))
                .andExpect(jsonPath("display").value(savedPost.isDisplay()))
                .andExpect(jsonPath("writer").isNotEmpty())
                .andDo(generateGetAnPostDocument())
        ;
    }

    @Test
    @DisplayName("[400:GET]전시 상태의 게시글 조회(요청 값의 자료형이 잘못된 경우)")
    public void findByIdAndDisplayTrue_Fail_CauseArgumentTypeMisMatch() throws Exception {
        // Given
        String urlTemplate = POST_URL + "/a";

        // When
        ResultActions resultActions = this.mockMvc.perform(get(urlTemplate)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
        );

        // Then
        resultActions.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("timestamp").exists())
                .andExpect(jsonPath("method").exists())
                .andExpect(jsonPath("path").value(urlTemplate))
                .andExpect(jsonPath("code").value(ErrorCode.METHOD_ARGUMENT_TYPE_MISMATCH.name()))
                .andExpect(jsonPath("message").value(ErrorCode.METHOD_ARGUMENT_TYPE_MISMATCH.message))
        ;
    }

    @Test
    @DisplayName("[404:GET]전시 상태의 게시글 조회(존재하지 않는 자원의 조회 요청)")
    public void findByIdAndDisplayTrue_Fail_Cause_ResourceNotFound() throws Exception {
        // Given
        String urlTemplate = POST_URL + "/0";

        // When
        ResultActions resultActions = this.mockMvc.perform(get(urlTemplate)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
        );

        // Then
        resultActions.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("timestamp").exists())
                .andExpect(jsonPath("method").exists())
                .andExpect(jsonPath("path").value(urlTemplate))
                .andExpect(jsonPath("code").value(ErrorCode.RESOURCE_NOT_FOUND.name()))
                .andExpect(jsonPath("message").value(ErrorCode.RESOURCE_NOT_FOUND.message))
        ;
    }

    @Test
    @DisplayName("[200:PATCH]게시글 수정")
    public void update() throws Exception {
        // Given
        Member savedMember = memberGenerator.savedMember();
        Post savedPost = postGenerator.savedPost(savedMember);
        PostUpdateRequest postUpdateRequest = postGenerator.postUpdateRequest(savedPost);
        Token token = tokenGenerator.generateToken(savedMember);
        String urlTemplate = POST_URL + "/{id}";

        // When
        ResultActions resultActions = this.mockMvc.perform(RestDocumentationRequestBuilders.patch(urlTemplate, savedPost.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(this.objectMapper.writeValueAsString(postUpdateRequest))
                .header(AUTHORIZATION, TokenGenerator.getBearerToken(token.getAccessToken()))
        );

        // Then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(postUpdateRequest.getId()))
                .andExpect(jsonPath("title").value(postUpdateRequest.getTitle()))
                .andExpect(jsonPath("content").value(postUpdateRequest.getContent()))
                .andExpect(jsonPath("viewCount").exists())
                .andExpect(jsonPath("display").exists())
                .andExpect(jsonPath("writer").isNotEmpty())
                .andDo(generateUpdateAnPostDocument())
        ;
    }

    @Test
    @DisplayName("[401:PATCH]게시글 수정(게시자가 아닌 사용자의 요청)")
    public void update_Fail_Cause_BadCredentials() throws Exception {
        // Given
        Token token = tokenGenerator.generateToken(new Member("choi.ys@naver.com", "password", "용석", "noel"));
        Post savedPost = postGenerator.savedPost();
        PostUpdateRequest postUpdateRequest = postGenerator.postUpdateRequest(savedPost);
        String urlTemplate = POST_URL + "/{id}";

        // When
        ResultActions resultActions = this.mockMvc.perform(patch(urlTemplate, savedPost.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(this.objectMapper.writeValueAsString(postUpdateRequest))
                .header(AUTHORIZATION, TokenGenerator.getBearerToken(token.getAccessToken()))
        );

        // Then
        resultActions.andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("timestamp").exists())
                .andExpect(jsonPath("method").exists())
                .andExpect(jsonPath("path").value(POST_URL + "/" + savedPost.getId()))
                .andExpect(jsonPath("code").value(ErrorCode.BAD_CREDENTIALS.name()))
                .andExpect(jsonPath("message").value(ErrorCode.BAD_CREDENTIALS.message))
        ;
    }

    @Test
    @DisplayName("[204:DELETE]게시글 삭제")
    public void deletePost() throws Exception {
        // Given
        Member savedMember = memberGenerator.savedMember();
        Post savedPost = postGenerator.savedPost(savedMember);
        Token token = tokenGenerator.generateToken(savedMember);
        String urlTemplate = POST_URL + "/{id}";

        // When
        ResultActions resultActions = this.mockMvc.perform(RestDocumentationRequestBuilders.delete(urlTemplate, savedPost.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .header(AUTHORIZATION, TokenGenerator.getBearerToken(token.getAccessToken()))
        );

        // Then
        resultActions.andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$").doesNotExist())
                .andDo(generateDeleteAnPostDocument())
        ;
    }

    @Test
    @DisplayName("[401:DELETE]게시글 삭제(게시자가 아닌 사용자의 요청)")
    public void deletePost_Fail_Cause_BadCredentials() throws Exception {
        // Given
        Member savedMember = memberGenerator.savedMember();
        Post savedPost = postGenerator.savedPost(savedMember);
        Token token = tokenGenerator.generateToken(MemberGenerator.secondMember());
        String urlTemplate = POST_URL + "/" + savedPost.getId();

        // When
        ResultActions resultActions = this.mockMvc.perform(delete(urlTemplate)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .header(AUTHORIZATION, TokenGenerator.getBearerToken(token.getAccessToken()))
        );

        // Then
        resultActions.andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("timestamp").exists())
                .andExpect(jsonPath("method").exists())
                .andExpect(jsonPath("path").value(urlTemplate))
                .andExpect(jsonPath("code").value(ErrorCode.BAD_CREDENTIALS.name()))
                .andExpect(jsonPath("message").value(ErrorCode.BAD_CREDENTIALS.message))
        ;
    }
}