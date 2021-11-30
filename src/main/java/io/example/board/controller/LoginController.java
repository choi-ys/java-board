package io.example.board.controller;

import io.example.board.config.security.jwt.verifier.TokenVerifier;
import io.example.board.domain.dto.request.LoginRequest;
import io.example.board.domain.dto.response.LoginResponse;
import io.example.board.domain.dto.response.MemberSimpleResponse;
import io.example.board.service.LoginService;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * @author : choi-ys
 * @date : 2021/09/23 12:27 오전
 */
@RestController
@RequestMapping(
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaTypes.HAL_JSON_VALUE
)
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("login")
    public ResponseEntity login(@Valid @RequestBody final LoginRequest loginRequest) {
        LoginResponse loginResponse = loginService.login(loginRequest);

        EntityModel<MemberSimpleResponse> entityModel = EntityModel.of(loginResponse);
        entityModel.add(linkTo(methodOn(this.getClass()).refresh(null)).withRel("refresh"));

        // TODO: 용석(2021/09/23) : [/logout, /member/{id}, /refresh] link 정보 추가
        return ResponseEntity.ok(entityModel);
    }

    @PostMapping("refresh")
    public ResponseEntity refresh(HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(loginService.refresh(TokenVerifier.resolve(httpServletRequest)));
    }
}
