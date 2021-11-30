package io.example.board.controller;

import io.example.board.domain.dto.request.LoginRequest;
import io.example.board.domain.dto.request.SignupRequest;
import io.example.board.domain.dto.response.MemberSimpleResponse;
import io.example.board.service.MemberService;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * @author : choi-ys
 * @date : 2021/09/21 4:00 오전
 */
@RestController
@RequestMapping(
        value = "member",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaTypes.HAL_JSON_VALUE
)
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity signup(@Valid @RequestBody final SignupRequest signupRequest) {
        MemberSimpleResponse memberSimpleResponse = memberService.signup(signupRequest);

        WebMvcLinkBuilder selfLinkBuilder = linkTo(methodOn(this.getClass())
                .signup(signupRequest))
                .slash(memberSimpleResponse.getId());

        URI createdUri = selfLinkBuilder.toUri();

        EntityModel<MemberSimpleResponse> entityModel = EntityModel.of(memberSimpleResponse);
        entityModel.add(selfLinkBuilder.withSelfRel());
        entityModel.add(linkTo(methodOn(LoginController.class).login(new LoginRequest(signupRequest.getEmail(), signupRequest.getPassword()))).withRel("login"));

        // TODO: 용석(2021/09/21): Add to api docs link info
        return ResponseEntity.created(createdUri).body(entityModel);
    }
}
