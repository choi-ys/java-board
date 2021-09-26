package io.example.board.service;

import io.example.board.advice.exception.ResourceNotFoundException;
import io.example.board.domain.dto.request.PostRequest;
import io.example.board.domain.dto.request.PostUpdateRequest;
import io.example.board.domain.dto.response.PostResponse;
import io.example.board.domain.rdb.member.Member;
import io.example.board.domain.rdb.post.Post;
import io.example.board.domain.vo.login.LoginUser;
import io.example.board.repository.rdb.member.MemberRepo;
import io.example.board.repository.rdb.post.PostRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : choi-ys
 * @date : 2021-09-26 오후 2:13
 */
@Service
@Transactional
public class PostService {

    private final MemberRepo memberRepo;
    private final PostRepo postRepo;

    public PostService(MemberRepo memberRepo, PostRepo postRepo) {
        this.memberRepo = memberRepo;
        this.postRepo = postRepo;
    }

    public PostResponse create(PostRequest postRequest, LoginUser loginUser) {
        Member member = memberRepo.findByEmail(loginUser.getEmail()).orElseThrow();
        Post post = postRepo.save(postRequest.toEntity(member));
        return PostResponse.mapTo(post);
    }

    public PostResponse findByIdAndDisplayTrue(long postId) {
        return PostResponse.mapTo(postRepo.findByIdAndDisplayTrue(postId).orElseThrow(
                () -> new ResourceNotFoundException()
        ));
    }

    public PostResponse update(PostUpdateRequest postUpdateRequest) {
        Post post = postRepo.findById(postUpdateRequest.getId()).orElseThrow(
                () -> new ResourceNotFoundException()
        );
        post.update(postUpdateRequest);
        return PostResponse.mapTo(post);
    }

    public void delete(long postId) {
        postRepo.delete(postRepo.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException()
        ));
    }
}
