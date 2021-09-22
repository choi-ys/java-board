package io.example.board.controller;

import io.example.board.domain.dto.request.LoginRequest;
import io.example.board.domain.dto.response.LoginResponse;
import io.example.board.service.LoginService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author : choi-ys
 * @date : 2021/09/23 12:27 오전
 */
@RestController
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping(value = "login")
    public ResponseEntity login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = loginService.login(loginRequest);

        // TODO: 용석(2021/09/23) : [/logout, /member/{id}] link 정보 추가
        return ResponseEntity.ok(loginResponse);
    }
}
