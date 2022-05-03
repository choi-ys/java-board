package io.example.board.controller;

import static io.example.board.utils.generator.docs.MemberDocumentGenerator.generateGetAnMemberDocument;
import static io.example.board.utils.generator.docs.MemberDocumentGenerator.generateSignupMemberDocument;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.example.board.config.test.SpringBootTestConfig;
import io.example.board.domain.dto.request.SignupRequest;
import io.example.board.domain.dto.response.error.ErrorCode;
import io.example.board.domain.rdb.member.Member;
import io.example.board.domain.vo.token.Token;
import io.example.board.utils.generator.mock.MemberGenerator;
import io.example.board.utils.generator.mock.TokenGenerator;
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

/**
 * @author : choi-ys
 * @date : 2021/09/21 4:04 오전
 */
@SpringBootTestConfig
@AutoConfigureRestDocs
@DisplayName("API:Member")
@Import({MemberGenerator.class, TokenGenerator.class})
class MemberControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final MemberGenerator memberGenerator;
    private final TokenGenerator tokenGenerator;
    private final String MEMBER_URL = "/member";

    public MemberControllerTest(
        MockMvc mockMvc,
        ObjectMapper objectMapper,
        MemberGenerator memberGenerator,
        TokenGenerator tokenGenerator
    ) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.memberGenerator = memberGenerator;
        this.tokenGenerator = tokenGenerator;
    }

    @Test
    @DisplayName("[200:POST]회원가입")
    public void signup() throws Exception {
        // Given
        SignupRequest signupRequest = MemberGenerator.signupRequest();

        // When
        ResultActions resultActions = this.mockMvc.perform(RestDocumentationRequestBuilders.post(MEMBER_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaTypes.HAL_JSON)
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
            .andDo(generateSignupMemberDocument())
        ;
    }

    @Test
    @DisplayName("[400:POST]회원 가입 실패(값이 없는 요청)")
    public void signup_Fail_CauseNoArgument() throws Exception {
        // When
        ResultActions resultActions = this.mockMvc.perform(post(MEMBER_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaTypes.HAL_JSON)
        );

        // Then
        resultActions.andDo(print())
            .andExpect(status().isBadRequest())
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
            .accept(MediaTypes.HAL_JSON)
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
    @DisplayName("[400:POST]회원 가입 실패(중복된 이메일 요청)")
    public void givenDuplicatedEmail_whenSignupRequest_thenReturnDuplicatedErrorResponse() throws Exception {
        // Given
        Member savedMember = memberGenerator.savedMember();
        SignupRequest signupRequest = new SignupRequest(
            savedMember.getEmail(),
            savedMember.getPassword(),
            savedMember.getName(),
            savedMember.getNickname()
        );

        // When
        ResultActions resultActions = this.mockMvc.perform(RestDocumentationRequestBuilders.post(MEMBER_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaTypes.HAL_JSON)
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
            .accept(MediaTypes.HAL_JSON)
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

    @Test
    @DisplayName("[200:GET]특정 회원 조회")
    public void getAnMemberTest() throws Exception {
        // Given
        Member member = memberGenerator.savedMember();
        Token token = tokenGenerator.generateToken(member);
        final String urlTemplate = MEMBER_URL.concat("/{id}");
//        final String urlTemplate = MEMBER_URL + "/{id}";

        // When
        ResultActions resultActions = this.mockMvc
            .perform(RestDocumentationRequestBuilders.get(urlTemplate, member.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .header(AUTHORIZATION, TokenGenerator.getBearerToken(token.getAccessToken()))
            );

        // Then
        resultActions.andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(member.getId()))
            .andExpect(jsonPath("$.email").value(member.getEmail()))
            .andExpect(jsonPath("$.name").value(member.getName()))
            .andExpect(jsonPath("$.nickname").value(member.getNickname()))
            .andExpect(jsonPath("$.memberStatus").value(member.getMemberStatus().getName()))
            .andDo(generateGetAnMemberDocument())
        ;
    }

    @Test
    @DisplayName("[404:GET]특정 회원 조회 실패 : 존재 하지 않는 회원 조회 요청")
    public void getAnMemberTest_Fail_CauseNotExist() throws Exception {
        // Given
        Token token = tokenGenerator.generateToken();
        final String urlTemplate = MEMBER_URL.concat("/{memberId}");

        // When
        ResultActions resultActions = this.mockMvc.perform(get(urlTemplate, 0L)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaTypes.HAL_JSON)
            .header(AUTHORIZATION, TokenGenerator.getBearerToken(token.getAccessToken()))
        );

        // Then
        resultActions.andDo(print())
            .andExpect(status().isNotFound())
        ;
    }
}