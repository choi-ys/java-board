package io.example.board.service;

import io.example.board.advice.exception.ResourceNotFoundException;
import io.example.board.domain.dto.request.PostCreateRequest;
import io.example.board.domain.dto.request.PostUpdateRequest;
import io.example.board.domain.dto.request.SearchPostRequest;
import io.example.board.domain.dto.response.PostResponse;
import io.example.board.domain.dto.response.SearchPostResponse;
import io.example.board.domain.dto.response.error.ErrorCode;
import io.example.board.domain.rdb.member.Member;
import io.example.board.domain.rdb.post.Post;
import io.example.board.domain.vo.login.LoginUser;
import io.example.board.domain.vo.login.LoginUserAdapter;
import io.example.board.repository.rdb.common.PageResponse;
import io.example.board.repository.rdb.member.MemberRepo;
import io.example.board.repository.rdb.post.PostRepo;
import io.example.board.utils.generator.mock.MemberGenerator;
import io.example.board.utils.generator.mock.PostGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author : choi-ys
 * @date : 2021-09-26 오후 5:51
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Service:Post")
class PostServiceTest {

    @Mock
    private MemberRepo memberRepo;

    @Mock
    private PostRepo postRepo;

    @InjectMocks
    PostService postService;

    @Test
    @DisplayName("게시글 생성")
    public void create() {
        // Given
        Member member = MemberGenerator.member();
        PostCreateRequest postCreateRequest = PostGenerator.postRequest();
        LoginUserAdapter loginUserAdapter = MemberGenerator.loginUserAdapter();

        given(memberRepo.findByEmail(anyString())).willReturn(Optional.of(member));
        given(postRepo.save(any(Post.class))).will(AdditionalAnswers.returnsFirstArg());
        ArgumentCaptor<Post> postArgumentCaptor = ArgumentCaptor.forClass(Post.class);

        // When
        postService.create(postCreateRequest, loginUserAdapter.getLoginUser());

        // Then
        verify(memberRepo, times(1)).findByEmail(anyString());
        verify(postRepo, times(1)).save(postArgumentCaptor.capture());

        Post expected = postArgumentCaptor.getValue();
        assertAll(
                () -> assertEquals(expected.getId(), null),
                () -> assertEquals(expected.getTitle(), postCreateRequest.getTitle()),
                () -> assertEquals(expected.getContent(), postCreateRequest.getContent()),
                () -> assertEquals(expected.getMember(), member),
                () -> assertEquals(expected.isDisplay(), true),
                () -> assertEquals(expected.getViewCount(), 0L),
                () -> assertEquals(expected.isDeleted(), false)
        );
    }

    @Test
    @DisplayName("게시글 조회 실패(존재 하지 않는 자원)")
    public void findByIdAndDisplay() {
        // When
        IllegalArgumentException expected = assertThrows(
                ResourceNotFoundException.class,
                () -> postService.findByIdAndDisplayTrue(0L)
        );

        // Then
        assertAll(
                () -> assertTrue(expected instanceof RuntimeException),
                () -> assertEquals(expected.getMessage(), ErrorCode.RESOURCE_NOT_FOUND.message)
        );
    }

    @Test
    @DisplayName("게시글 수정")
    public void update() {
        // Given
        LoginUser loginUser = MemberGenerator.loginUserAdapter().getLoginUser();
        Post postMock = PostGenerator.postMock();
        PostUpdateRequest postUpdateRequest = PostGenerator.postUpdateRequestMock();

        given(postRepo.findByIdAndMemberEmail(postUpdateRequest.getId(), loginUser.getEmail())).willReturn(Optional.of(postMock));

        // When
        PostResponse expected = postService.update(postUpdateRequest, loginUser);

        // Then
        assertAll(
                () -> assertEquals(expected.getTitle(), postUpdateRequest.getTitle()),
                () -> assertEquals(expected.getContent(), postUpdateRequest.getContent()),
                () -> assertEquals(expected.isDisplay(), postUpdateRequest.isDisplay())
        );

        verify(postRepo, times(1)).findByIdAndMemberEmail(postUpdateRequest.getId(), loginUser.getEmail());
    }

    @Test
    @DisplayName("게시글 수정 실패(게시자가 아닌 사용자의 요청)")
    public void update_Fail_Cause_BadCredentials() {
        // Given
        LoginUser loginUser = MemberGenerator.loginUserAdapter().getLoginUser();
        PostUpdateRequest postUpdateRequest = PostGenerator.postUpdateRequestMock();

        given(postRepo.findByIdAndMemberEmail(anyLong(), anyString())).willThrow(BadCredentialsException.class);

        // When
        AuthenticationException expected = assertThrows(
                BadCredentialsException.class,
                () -> postService.update(postUpdateRequest, loginUser)
        );

        assertTrue(expected instanceof RuntimeException);

        verify(postRepo, times(1)).findByIdAndMemberEmail(anyLong(), anyString());
    }

    @Test
    @DisplayName("게시글 삭제")
    public void delete() {
        // Given
        Post postMock = PostGenerator.postMock();
        LoginUser loginUser = MemberGenerator.loginUserAdapter().getLoginUser();
        given(postRepo.findByIdAndMemberEmail(anyLong(), anyString())).willReturn(Optional.of(postMock));

        // When
        postService.delete(0L, loginUser);

        // Then
        verify(postRepo, times(1)).findByIdAndMemberEmail(anyLong(), anyString());
        assertTrue(postMock.isDeleted());
    }

    @Test
    @DisplayName("게시글 삭제 실패(게시자가 아닌 사용자의 요청)")
    public void delete_Cause_BadCredentials() {
        // Given
        Post postMock = PostGenerator.postMock();
        LoginUserAdapter loginUserAdapter = MemberGenerator.loginUserAdapter();
        given(postRepo.findByIdAndMemberEmail(anyLong(), anyString())).willThrow(BadCredentialsException.class);

        // When
        AuthenticationException expected = assertThrows(
                BadCredentialsException.class,
                () -> postService.delete(0L, loginUserAdapter.getLoginUser())
        );

        // Then
        verify(postRepo, times(1)).findByIdAndMemberEmail(anyLong(), anyString());
        assertTrue(expected instanceof RuntimeException);
    }

    @Test
    @DisplayName("게시글 검색: 검색조건과 페이지 요청이 포함된 경우")
    public void givenSearchParamsAndPageRequest_whenFindingPostPage_thenReturnSearchedPostPageResponse() {
        // Given
        SearchPostRequest searchPostRequest = PostGenerator.searchPostRequest();
        SearchPostResponse searchPostResponseMock = PostGenerator.searchPostResponse();

        given(postRepo.findPostPageBySearchParams(searchPostRequest))
                .willReturn(new PageImpl(Arrays.asList(searchPostResponseMock)));

        // When
        PageResponse<SearchPostResponse> expected = postService.searchPost(searchPostRequest);

        // Then
        assertThat(expected.getEmbedded())
                .allSatisfy(postSearchResponse -> {
                    assertTrue(postSearchResponse.getTitle().contains(searchPostRequest.getTitle()));
                    assertTrue(postSearchResponse.getContent().contains(searchPostRequest.getContent()));
                    assertEquals(searchPostRequest.getWriterName(), postSearchResponse.getWriter().getName());
                    assertThat(postSearchResponse.getCreatedAt()).isAfterOrEqualTo(searchPostRequest.getCreatedAt());
                    assertThat(postSearchResponse.getUpdatedAt()).isBeforeOrEqualTo(searchPostRequest.getUpdatedAt());
                });
        verify(postRepo, times(1)).findPostPageBySearchParams(searchPostRequest);
    }

    @Test
    @DisplayName("게시글 검색: 검색조건만 있고, 페이지 요청이 없는 경우")
    public void givenSearchParamsWithoutPageRequest_whenFindPostPage_thenResultSearchedFirstPostPageResponse() {
        // Given
        LocalDateTime now = LocalDateTime.now();

        SearchPostRequest searchPostRequest = new SearchPostRequest(
                "제목",
                "본문",
                "choi-ys",
                now.minusDays(2L),
                now,
                null
        );

        given(postRepo.findPostPageBySearchParams(searchPostRequest))
                .willThrow(InvalidDataAccessApiUsageException.class);

        // When
        Exception exception = assertThrows(Exception.class, () -> postService.searchPost(searchPostRequest));

        // Then
        assertThat(exception)
                .isInstanceOf(RuntimeException.class);
        verify(postRepo, times(1)).findPostPageBySearchParams(searchPostRequest);
    }
}