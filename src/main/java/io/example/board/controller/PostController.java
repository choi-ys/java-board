package io.example.board.controller;

import io.example.board.domain.dto.request.PostCreateRequest;
import io.example.board.domain.dto.response.PostResponse;
import io.example.board.domain.vo.login.CurrentUser;
import io.example.board.domain.vo.login.LoginUser;
import io.example.board.service.PostService;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * @author : choi-ys
 * @date : 2021-09-27 오전 12:54
 */
@RestController
@RequestMapping(
        value = "post",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity create(@Valid @RequestBody PostCreateRequest postCreateRequest, @CurrentUser LoginUser loginUser) {
        PostResponse postResponse = postService.create(postCreateRequest, loginUser);

        WebMvcLinkBuilder selfLinkBuilder = linkTo(methodOn(this.getClass())
                .create(postCreateRequest, loginUser))
                .slash(postResponse.getId());

        EntityModel<PostResponse> entityModel = EntityModel.of(postResponse);
        entityModel.add(selfLinkBuilder.withSelfRel());

        return ResponseEntity.created(selfLinkBuilder.toUri()).body(entityModel);
    }
}
