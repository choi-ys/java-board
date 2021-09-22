package io.example.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.example.board.config.test.EnableMockMvc;
import io.example.board.domain.dto.request.LoginRequest;
import io.example.board.domain.dto.response.MemberSimpleResponse;
import io.example.board.domain.dto.response.error.ErrorCode;
import io.example.board.domain.member.Member;
import io.example.board.service.MemberService;
import io.example.board.utils.generator.MemberGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author : choi-ys
 * @date : 2021/09/23 12:30 오전
 */
@SpringBootTest
@EnableMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@ActiveProfiles("test")
@DisplayName("API:Login")
@Transactional
class LoginControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final MemberService memberService;

    public LoginControllerTest(MockMvc mockMvc, ObjectMapper objectMapper, MemberService memberService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.memberService = memberService;
    }

    private final String LOGIN_URL = "/login";

    @Test
    @DisplayName("[200:POST]로그인")
    public void login() throws Exception {
        // Given
        MemberSimpleResponse memberSimpleResponse = memberService.signup(MemberGenerator.signupRequest());
        LoginRequest loginRequest = new LoginRequest(memberSimpleResponse.getEmail(), MemberGenerator.password);

        // When
        ResultActions resultActions = this.mockMvc.perform(post(LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(loginRequest))
        );

        // Then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.CONTENT_TYPE))
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("email").exists())
                .andExpect(jsonPath("name").exists())
                .andExpect(jsonPath("nickname").exists())
                .andExpect(jsonPath("token.accessToken").exists())
                .andExpect(jsonPath("token.refreshToken").exists())
                .andExpect(jsonPath("token.accessExpired").exists())
                .andExpect(jsonPath("token.refreshExpired").exists())
        ;
    }

    @Test
    @DisplayName("[400:POST]로그인 실패(값이 없는 요청)")
    public void login_Fail_CauseNoArgument() throws Exception {
        // When
        ResultActions resultActions = this.mockMvc.perform(post(LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(header().exists(HttpHeaders.CONTENT_TYPE))
                .andExpect(jsonPath("timestamp").exists())
                .andExpect(jsonPath("method").exists())
                .andExpect(jsonPath("path").exists())
                .andExpect(jsonPath("code").value(ErrorCode.HTTP_MESSAGE_NOT_READABLE.name()))
                .andExpect(jsonPath("message").value(ErrorCode.HTTP_MESSAGE_NOT_READABLE.message))
        ;
    }

    @Test
    @DisplayName("[400:POST]로그인 실패(값이 잘못된 요청)")
    public void login_Fail_CauseInvalidArgument() throws Exception {
        // Given
        LoginRequest loginRequest = new LoginRequest("", "");

        // When
        ResultActions resultActions = this.mockMvc.perform(post(LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(loginRequest))
        );

        // Then
        resultActions.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(header().exists(HttpHeaders.CONTENT_TYPE))
                .andExpect(jsonPath("timestamp").exists())
                .andExpect(jsonPath("method").exists())
                .andExpect(jsonPath("path").exists())
                .andExpect(jsonPath("code").value(ErrorCode.METHOD_ARGUMENT_NOT_VALID.name()))
                .andExpect(jsonPath("message").value(ErrorCode.METHOD_ARGUMENT_NOT_VALID.message))
        ;
    }

    @Test
    @DisplayName("[401:POST]로그인 실패(존재하지 않는 회원)")
    public void login_Fail_CauseNoExistUser() throws Exception {
        // Given
        Member member = MemberGenerator.member();
        LoginRequest loginRequest = new LoginRequest(member.getEmail(), member.getPassword());

        // When
        ResultActions resultActions = this.mockMvc.perform(post(LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(loginRequest))
        );

        // Then
        resultActions.andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(header().exists(HttpHeaders.CONTENT_TYPE))
                .andExpect(jsonPath("timestamp").exists())
                .andExpect(jsonPath("method").exists())
                .andExpect(jsonPath("path").exists())
                .andExpect(jsonPath("code").value(ErrorCode.BAD_CREDENTIALS.name()))
                .andExpect(jsonPath("message").value(ErrorCode.BAD_CREDENTIALS.message))
        ;
    }

    @Test
    @DisplayName("[401:POST]로그인 실패(비밀번호 불일치)")
    public void login_Fail_CauseNotMatchedPassword() throws Exception {
        // Given
        MemberSimpleResponse memberSimpleResponse = memberService.signup(MemberGenerator.signupRequest());
        LoginRequest loginRequest = new LoginRequest(memberSimpleResponse.getEmail(), "Given invalid password");

        // When
        ResultActions resultActions = this.mockMvc.perform(post(LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(loginRequest))
        );

        // Then
        resultActions.andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(header().exists(HttpHeaders.CONTENT_TYPE))
                .andExpect(jsonPath("timestamp").exists())
                .andExpect(jsonPath("method").exists())
                .andExpect(jsonPath("path").exists())
                .andExpect(jsonPath("code").value(ErrorCode.BAD_CREDENTIALS.name()))
                .andExpect(jsonPath("message").value(ErrorCode.BAD_CREDENTIALS.message))
        ;
    }
}