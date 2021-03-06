package io.example.board.controller;

import io.example.board.domain.dto.request.PostCreateRequest;
import io.example.board.domain.dto.request.PostUpdateRequest;
import io.example.board.domain.dto.request.SearchPostRequest;
import io.example.board.domain.dto.response.PostResponse;
import io.example.board.domain.vo.login.CurrentUser;
import io.example.board.domain.vo.login.LoginUser;
import io.example.board.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;

import static io.example.board.controller.IndexController.profileUrl;
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
        produces = MediaTypes.HAL_JSON_VALUE
)
@Slf4j
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity create(
            @Valid @RequestBody final PostCreateRequest postCreateRequest,
            @CurrentUser final LoginUser loginUser
    ) {
        PostResponse postResponse = postService.create(postCreateRequest, loginUser);
        WebMvcLinkBuilder selfLinkBuilder = linkTo(methodOn(this.getClass())
                .create(postCreateRequest, loginUser))
                .slash(postResponse.getId());

        EntityModel<PostResponse> entityModel = EntityModel.of(postResponse);
        entityModel.add(selfLinkBuilder.withSelfRel());
        entityModel.add(profileUrl());

        // TODO: 용석(2021-09-27) : [GET, PATCH, DELETE /post, GET /search] link 정보 추가
        return ResponseEntity.created(selfLinkBuilder.toUri()).body(entityModel);
    }

    @GetMapping("{id}")
    public ResponseEntity findById(@PathVariable("id") final Long id) {
        // TODO: 용석(2021-09-27) : [PATCH, DELETE /post, GET /search] link 정보 추가
        return ResponseEntity.ok(postService.findByIdAndDisplayTrue(id));
    }

    @PatchMapping("{id}")
    public ResponseEntity update(
            @PathVariable("id") final Long id,
            @Valid @RequestBody final PostUpdateRequest postUpdateRequest,
            @CurrentUser final LoginUser loginUser
    ) {
        // TODO: 용석(2021-09-27) : [GET, DELETE /post, GET /search] link 정보 추가
        return ResponseEntity.ok(postService.update(postUpdateRequest, loginUser));
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(
            @PathVariable("id") final Long id,
            @CurrentUser final LoginUser loginUser
    ) {
        // TODO: 용석(2021-09-27) : [GET, DELETE /post, GET /search] link 정보 추가
        postService.delete(id, loginUser);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity search(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) String writerName,
            @RequestParam(required = false) LocalDateTime createdAt,
            @RequestParam(required = false) LocalDateTime updatedAt,
            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = {"createdAt"},
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return ResponseEntity.ok(
                postService.searchPostWithComments(
                        SearchPostRequest.of(
                                title,
                                content,
                                writerName,
                                createdAt,
                                updatedAt,
                                pageable
                        )
                )
        );
    }
}
