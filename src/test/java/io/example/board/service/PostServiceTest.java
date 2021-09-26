package io.example.board.service;

import io.example.board.advice.exception.ResourceNotFoundException;
import io.example.board.domain.dto.request.PostRequest;
import io.example.board.domain.dto.response.error.ErrorCode;
import io.example.board.domain.rdb.member.Member;
import io.example.board.domain.rdb.post.Post;
import io.example.board.domain.vo.login.LoginUserAdapter;
import io.example.board.repository.rdb.member.MemberRepo;
import io.example.board.repository.rdb.post.PostRepo;
import io.example.board.utils.generator.MemberGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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

    private Post generatePostMock() {
        Member member = MemberGenerator.member();
        String title = "게시글 제목";
        String content = "게시글 본문";
        return new Post(title, content, member);
    }

    @Test
    @DisplayName("게시글 생성")
    public void create() {
        // Given
        Member member = MemberGenerator.member();
        String title = "게시글 제목";
        String content = "게시글 본문";
        PostRequest postRequest = new PostRequest(title, content);
        LoginUserAdapter loginUserAdapter = MemberGenerator.loginUserAdapter();

        given(memberRepo.findByEmail(anyString())).willReturn(Optional.of(member));
        given(postRepo.save(any(Post.class))).will(AdditionalAnswers.returnsFirstArg());

        // When
        postService.create(postRequest, loginUserAdapter.getLoginUser());

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
                () -> postService.findByIdAndDisplay(0L)
        );

        // Then
        assertAll(
                () -> assertTrue(expected instanceof IllegalArgumentException),
                () -> assertEquals(expected.getMessage(), ErrorCode.RESOURCE_NOT_FOUND_EXCEPTION.message)
        );
    }
}