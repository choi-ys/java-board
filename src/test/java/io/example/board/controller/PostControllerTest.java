package io.example.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.example.board.config.test.EnableMockMvc;
import io.example.board.domain.dto.request.PostCreateRequest;
import io.example.board.domain.dto.response.error.ErrorCode;
import io.example.board.domain.vo.token.Token;
import io.example.board.utils.generator.MemberGenerator;
import io.example.board.utils.generator.PostGenerator;
import io.example.board.utils.generator.TokenGenerator;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author : choi-ys
 * @date : 2021-09-27 오전 1:05
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableMockMvc
@Transactional
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Import({TokenGenerator.class, MemberGenerator.class, PostGenerator.class})
@ActiveProfiles("test")
@DisplayName("API:Post")
class PostControllerTest {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final TokenGenerator tokenGenerator;
    private final MemberGenerator memberGenerator;
    private final PostGenerator postGenerator;

    public PostControllerTest(MockMvc mockMvc, ObjectMapper objectMapper, TokenGenerator tokenGenerator,
                              MemberGenerator memberGenerator, PostGenerator postGenerator) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.tokenGenerator = tokenGenerator;
        this.memberGenerator = memberGenerator;
        this.postGenerator = postGenerator;
    }

    private final String POST_URL = "/post";

    @Test
    @DisplayName("[200:POST]게시글 생성")
    @Disabled
    public void create() throws Exception {
        // Given
        Token token = tokenGenerator.generateToken();
        PostCreateRequest postCreateRequest = PostGenerator.postRequest();

        // When
        ResultActions resultActions = this.mockMvc.perform(post(POST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
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
                .andExpect(jsonPath("writer").exists())
                .andExpect(jsonPath("_links.self").exists())
        ;
    }

    @Test
    @DisplayName("[400:POST]게시글 생성 실패 : 값이 없는 요청")
    @Disabled
    public void create_Fail_Cause_NoArgument() throws Exception {
        // Given
        Token token = tokenGenerator.generateToken();

        // When
        ResultActions resultActions = this.mockMvc.perform(post(POST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
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
    @DisplayName("[400:POST]게시글 생성 실패 : 값이 잘못된 요청")
    @Disabled
    public void create_Fail_Cause_InvalidArgument() throws Exception {
        // Given
        Token token = tokenGenerator.generateToken();
        PostCreateRequest postCreateRequest = new PostCreateRequest("", "");

        // When
        ResultActions resultActions = this.mockMvc.perform(post(POST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
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
                .andExpect(jsonPath("errorDetails").exists())
        ;
    }

    @Test
    @DisplayName("[403:POST]게시글 생성 실패 : 권한이 없는 요청")
    @Disabled
    public void create_Fail_Cause_NoCredentials() throws Exception {
        // Given
        PostCreateRequest postCreateRequest = PostGenerator.postRequest();

        // When
        ResultActions resultActions = this.mockMvc.perform(post(POST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(postCreateRequest))
        );

        // Then
        resultActions.andDo(print())
                .andExpect(status().isForbidden())
        ;
    }
}