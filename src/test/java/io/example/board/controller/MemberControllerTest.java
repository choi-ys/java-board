package io.example.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.example.board.config.test.SpringBootTestConfig;
import io.example.board.domain.dto.request.SignupRequest;
import io.example.board.domain.dto.response.error.ErrorCode;
import io.example.board.utils.generator.mock.MemberGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author : choi-ys
 * @date : 2021/09/21 4:04 오전
 */
@SpringBootTestConfig
@DisplayName("API:Member")
class MemberControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    public MemberControllerTest(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    private final String MEMBER_URL = "/member";

    @Test
    @DisplayName("[200:POST]회원가입")
    public void signup() throws Exception {
        // Given
        SignupRequest signupRequest = MemberGenerator.signupRequest();

        // When
        ResultActions resultActions = this.mockMvc.perform(post(MEMBER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest))
        );

        // Then
        resultActions.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().exists(HttpHeaders.CONTENT_TYPE))
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("email").value(signupRequest.getEmail()))
                .andExpect(jsonPath("name").value(signupRequest.getName()))
                .andExpect(jsonPath("nickname").value(signupRequest.getNickname()))
                .andExpect(jsonPath("_links.self").exists())
        ;
    }

    @Test
    @DisplayName("[400:POST]회원 가입 실패(값이 없는 요청)")
    public void signup_Fail_CauseNoArgument() throws Exception {
        // When
        ResultActions resultActions = this.mockMvc.perform(post(MEMBER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
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
    @DisplayName("[400:POST]회원 가입 실패(값이 잘못된 요청)")
    public void signup_Fail_CauseInvalidArgument() throws Exception {
        // Given
        SignupRequest signupRequest = new SignupRequest("", "", "", "");

        // When
        ResultActions resultActions = this.mockMvc.perform(post(MEMBER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest))
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
    @DisplayName("[415:POST]회원 가입 실패(잘못된 Media Type 요청)")
    public void signup_Fail_CauseInValidMimeType() throws Exception {
        // Given
        SignupRequest signupRequest = new SignupRequest("", "", "", "");

        // When
        ResultActions resultActions = this.mockMvc.perform(post(MEMBER_URL)
                .contentType(objectMapper.writeValueAsString(signupRequest))
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest))
        );

        // Then
        resultActions.andDo(print())
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(jsonPath("timestamp").exists())
                .andExpect(jsonPath("method").exists())
                .andExpect(jsonPath("path").exists())
                .andExpect(jsonPath("code").value(ErrorCode.HTTP_MEDIA_TYPE_NOT_SUPPORTED.name()))
                .andExpect(jsonPath("message").value(ErrorCode.HTTP_MEDIA_TYPE_NOT_SUPPORTED.message))
        ;
    }
}