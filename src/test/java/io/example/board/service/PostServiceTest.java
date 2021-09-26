package io.example.board.service;

import io.example.board.advice.exception.ResourceNotFoundException;
import io.example.board.domain.dto.request.PostCreateRequest;
import io.example.board.domain.dto.request.PostUpdateRequest;
import io.example.board.domain.dto.response.PostResponse;
import io.example.board.domain.dto.response.error.ErrorCode;
import io.example.board.domain.rdb.member.Member;
import io.example.board.domain.rdb.post.Post;
import io.example.board.domain.vo.login.LoginUserAdapter;
import io.example.board.repository.rdb.member.MemberRepo;
import io.example.board.repository.rdb.post.PostRepo;
import io.example.board.utils.generator.MemberGenerator;
import io.example.board.utils.generator.PostGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

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

        // When
        postService.create(postCreateRequest, loginUserAdapter.getLoginUser());

        // Then
        verify(memberRepo, times(1)).findByEmail(anyString());
        verify(postRepo, times(1)).save(any(Post.class));
    }

    @Test
    @DisplayName("게시글 조회 실패 : 존재 하지 않는 자원")
    public void findByIdAndDisplay() {
        // When
        Exception expected = assertThrows(
                ResourceNotFoundException.class,
                () -> postService.findByIdAndDisplayTrue(0L)
        );

        // Then
        assertAll(
                () -> assertTrue(expected instanceof IllegalArgumentException),
                () -> assertEquals(expected.getMessage(), ErrorCode.RESOURCE_NOT_FOUND_EXCEPTION.message)
        );
    }

    @Test
    @DisplayName("게시글 수정")
    public void update() {
        // Given
        Post postMock = PostGenerator.postMock();
        PostUpdateRequest postUpdateRequest = PostGenerator.postUpdateRequest();

        given(postRepo.findById(postUpdateRequest.getId())).willReturn(Optional.of(postMock));

        // When
        PostResponse expected = postService.update(postUpdateRequest);

        // Then
        assertAll(
                () -> assertEquals(expected.getTitle(), postUpdateRequest.getTitle()),
                () -> assertEquals(expected.getContent(), postUpdateRequest.getContent()),
                () -> assertEquals(expected.isDisplay(), postUpdateRequest.isDisplay())
        );

        verify(postRepo, times(1)).findById(postUpdateRequest.getId());
    }

    @Test
    @DisplayName("게시글 삭제")
    public void delete() {
        // Given
        Post postMock = PostGenerator.postMock();
        given(postRepo.findById(anyLong())).willReturn(Optional.of(postMock));

        // When
        postService.delete(0L);

        // Then
        verify(postRepo, times(1)).delete(postMock);
    }
}