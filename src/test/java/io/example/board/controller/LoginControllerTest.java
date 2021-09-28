package io.example.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.example.board.config.test.SpringBootTestConfig;
import io.example.board.domain.dto.request.LoginRequest;
import io.example.board.domain.dto.response.MemberSimpleResponse;
import io.example.board.domain.dto.response.error.ErrorCode;
import io.example.board.domain.rdb.member.Member;
import io.example.board.domain.vo.token.Token;
import io.example.board.service.MemberService;
import io.example.board.utils.generator.mock.MemberGenerator;
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

import static io.example.board.utils.generator.docs.LoginDocumentGenerator.generateLoginDocument;
import static io.example.board.utils.generator.docs.LoginDocumentGenerator.generateRefreshDocument;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author : choi-ys
 * @date : 2021/09/23 12:30 오전
 */
@SpringBootTestConfig
@Import({TokenGenerator.class, MemberGenerator.class})
@AutoConfigureRestDocs
@DisplayName("API:Login")
class LoginControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final MemberService memberService;
    private final TokenGenerator tokenGenerator;
    private final MemberGenerator memberGenerator;

    public LoginControllerTest(MockMvc mockMvc, ObjectMapper objectMapper,
                               MemberService memberService, TokenGenerator tokenGenerator,
                               MemberGenerator memberGenerator) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.memberService = memberService;
        this.tokenGenerator = tokenGenerator;
        this.memberGenerator = memberGenerator;
    }

    private final String LOGIN_URL = "/login";
    private final String REFRESH_URL = "/refresh";

    @Test
    @DisplayName("[200:POST]로그인")
    public void login() throws Exception {
        // Given
        MemberSimpleResponse memberSimpleResponse = memberService.signup(MemberGenerator.signupRequest());
        LoginRequest loginRequest = new LoginRequest(memberSimpleResponse.getEmail(), MemberGenerator.password);

        // When
        ResultActions resultActions = this.mockMvc.perform(post(LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON_VALUE)
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
                .andExpect(jsonPath("_links.refresh.href").exists())
                .andDo(generateLoginDocument())
        ;
    }

    @Test
    @DisplayName("[400:POST]로그인 실패(값이 없는 요청)")
    public void login_Fail_CauseNoArgument() throws Exception {
        // When
        ResultActions resultActions = this.mockMvc.perform(post(LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON_VALUE)
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
                .accept(MediaTypes.HAL_JSON_VALUE)
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
                .accept(MediaTypes.HAL_JSON_VALUE)
                .content(this.objectMapper.writeValueAsString(loginRequest))
        );

        // Then
        resultActions.andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(header().exists(HttpHeaders.CONTENT_TYPE))
                .andExpect(jsonPath("timestamp").exists())
                .andExpect(jsonPath("method").exists())
                .andExpect(jsonPath("path").exists())
                .andExpect(jsonPath("code").value(ErrorCode.AUTHENTICATION_CREDENTIALS_NOT_FOUND.name()))
                .andExpect(jsonPath("message").value(ErrorCode.AUTHENTICATION_CREDENTIALS_NOT_FOUND.message))
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
                .accept(MediaTypes.HAL_JSON_VALUE)
                .content(this.objectMapper.writeValueAsString(loginRequest))
        );

        // Then
        resultActions.andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(header().exists(HttpHeaders.CONTENT_TYPE))
                .andExpect(jsonPath("timestamp").exists())
                .andExpect(jsonPath("method").exists())
                .andExpect(jsonPath("path").exists())
                .andExpect(jsonPath("code").value(ErrorCode.AUTHENTICATION_CREDENTIALS_NOT_FOUND.name()))
                .andExpect(jsonPath("message").value(ErrorCode.AUTHENTICATION_CREDENTIALS_NOT_FOUND.message))
        ;
    }

    @Test
    @DisplayName("[200:POST]토큰 갱신")
    public void refresh() throws Exception {
        // Given
        Token token = tokenGenerator.generateToken();

        // When
        ResultActions resultActions = this.mockMvc.perform(RestDocumentationRequestBuilders.post(REFRESH_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON_VALUE)
                .header(AUTHORIZATION, TokenGenerator.getBearerToken(token.getRefreshToken()))
        );

        // Then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("accessToken").exists())
                .andExpect(jsonPath("refreshToken").exists())
                .andExpect(jsonPath("accessExpired").exists())
                .andExpect(jsonPath("refreshExpired").exists())
                .andDo(generateRefreshDocument())
        ;
    }

    @Test
    @DisplayName("[405:POST]토큰 갱신(허용하지 않는 Http Method 요청)")
    public void refresh_FailCauseNotAllowedMethod() throws Exception {
        // Given
        Token token = tokenGenerator.generateToken();

        // When
        ResultActions resultActions = this.mockMvc.perform(get(REFRESH_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON_VALUE)
                .header(AUTHORIZATION, TokenGenerator.getBearerToken(token.getRefreshToken()))
        );

        // Then
        resultActions.andDo(print())
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("timestamp").exists())
                .andExpect(jsonPath("method").exists())
                .andExpect(jsonPath("path").exists())
                .andExpect(jsonPath("code").value(ErrorCode.HTTP_REQUEST_METHOD_NOT_SUPPORTED.name()))
                .andExpect(jsonPath("message").value(ErrorCode.HTTP_REQUEST_METHOD_NOT_SUPPORTED.message))
        ;
    }

    @Test
    @DisplayName("[415:POST]토큰 갱신(지원하지 않는 Media Type)")
    public void refresh_FailCauseNotSupportedMediaType() throws Exception {
        // Given
        Token token = tokenGenerator.generateToken();

        // When
        ResultActions resultActions = this.mockMvc.perform(post(REFRESH_URL)
                .content(token.getRefreshToken())
                .accept(MediaTypes.HAL_JSON_VALUE)
                .header(AUTHORIZATION, TokenGenerator.getBearerToken(token.getRefreshToken()))
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

    @Test
    @DisplayName("[403:POST]토큰 갱신(값이 없는 요청)")
    public void refresh_FailCauseNoArgument() throws Exception {
        // When
        ResultActions resultActions = this.mockMvc.perform(post(REFRESH_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON_VALUE)
        );

        // Then
        resultActions.andDo(print())
                .andExpect(status().isForbidden())
        ;
    }

    @Test
    @DisplayName("[403:POST]토큰 갱신(잘못된 형식의 토큰 요청)")
    @Disabled("jwtFilter: JWTDecodeException 예외 발생 시 응답 처리")
    public void refresh_FailInValidFormat() throws Exception {
        // When
        String token = "aaa.bbb.ccc";
        ResultActions resultActions = this.mockMvc.perform(post(REFRESH_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON_VALUE)
                .header(AUTHORIZATION, TokenGenerator.getBearerToken(token))
        );

        // Then
        resultActions.andDo(print())
                .andExpect(status().isForbidden())
        ;
    }

    @Test
    @DisplayName("[403:POST]토큰 갱신(잘못된 서명의 토큰 요청)")
    @Disabled("jwtFilter: SignatureVerificationException 예외 발생 시 응답 처리")
    public void refresh_FailCauseInCorrectSignature() throws Exception {
        // When
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
        ResultActions resultActions = this.mockMvc.perform(post(REFRESH_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON_VALUE)
                .header(AUTHORIZATION, TokenGenerator.getBearerToken(token))
        );

        // Then
        resultActions.andDo(print())
                .andExpect(status().isForbidden())
        ;
    }

    @Test
    @DisplayName("[403:POST]토큰 갱신(유효기간이 만료된 토큰 요청)")
    @Disabled("jwtFilter: TokenExpiredException 예외 발생 시 응답 처리")
    public void refresh_FailCauseExpiredToken() throws Exception {
        // When
        memberGenerator.savedMember();
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJTVUJKRUNUIiwiYXVkIjoiQVVESUVOQ0UiLCJ1c2UiOiJBQ0NFU1MiLCJpc3MiOiJJU1NVRVIiLCJleHAiOjE2MzI0MTA0MzAsImlhdCI6MTYzMjQxMDQyOSwiYXV0aG9yaXRpZXMiOiJST0xFX01FTUJFUiIsInVzZXJuYW1lIjoicHJvamVjdC5sb2cuMDYyQGdtYWlsLmNvbSJ9.P7SWGwPAX3IF8_muQgqS6zUmpnC1t2aWGv3EoV1A9jU";
        ResultActions resultActions = this.mockMvc.perform(post(REFRESH_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON_VALUE)
                .header(AUTHORIZATION, TokenGenerator.getBearerToken(token))
        );

        // Then
        resultActions.andDo(print())
                .andExpect(status().isForbidden())
        ;
    }
}