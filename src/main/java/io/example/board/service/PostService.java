package io.example.board.service;

import io.example.board.advice.exception.ResourceNotFoundException;
import io.example.board.domain.dto.request.PostCreateRequest;
import io.example.board.domain.dto.request.PostUpdateRequest;
import io.example.board.domain.dto.response.PostResponse;
import io.example.board.domain.dto.response.error.ErrorCode;
import io.example.board.domain.rdb.member.Member;
import io.example.board.domain.rdb.post.Post;
import io.example.board.domain.vo.login.LoginUser;
import io.example.board.repository.rdb.member.MemberRepo;
import io.example.board.repository.rdb.post.PostRepo;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : choi-ys
 * @date : 2021-09-26 오후 2:13
 */
@Service
@Transactional(readOnly = true)
public class PostService {

    private final MemberRepo memberRepo;
    private final PostRepo postRepo;

    public PostService(MemberRepo memberRepo, PostRepo postRepo) {
        this.memberRepo = memberRepo;
        this.postRepo = postRepo;
    }

    @Transactional
    public PostResponse create(PostCreateRequest postCreateRequest, LoginUser loginUser) {
        Member member = memberRepo.findByEmail(loginUser.getEmail()).orElseThrow(
                () -> new BadCredentialsException(ErrorCode.BAD_CREDENTIALS.message)
        );
        Post post = postRepo.save(postCreateRequest.toEntity(member));
        return PostResponse.mapTo(post);
    }

    public PostResponse findByIdAndDisplayTrue(long postId) {
        return PostResponse.mapTo(postRepo.findByIdAndDisplayTrue(postId).orElseThrow(
                () -> new ResourceNotFoundException()
        ));
    }

    @Transactional
    public PostResponse update(PostUpdateRequest postUpdateRequest, LoginUser loginUser) {
        Post post = postRepo.findByIdAndMemberEmail(postUpdateRequest.getId(), loginUser.getEmail()).orElseThrow(
                () -> new BadCredentialsException(ErrorCode.BAD_CREDENTIALS.message)
        );
        post.update(postUpdateRequest);
        return PostResponse.mapTo(post);
    }

    @Transactional
    public void delete(long postId, LoginUser loginUser) {
        Post post = postRepo.findByIdAndMemberEmail(postId, loginUser.getEmail()).orElseThrow(
                () -> new BadCredentialsException(ErrorCode.BAD_CREDENTIALS.message)
        );
        post.delete();
    }
}
