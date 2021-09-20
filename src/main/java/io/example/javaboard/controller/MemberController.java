package io.example.javaboard.controller;

import io.example.javaboard.domain.dto.request.SignupRequest;
import io.example.javaboard.service.MemberService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : choi-ys
 * @date : 2021/09/21 4:00 오전
 */
@RestController
@RequestMapping(
        value = "member",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity signup(@RequestBody SignupRequest signupRequest) {
        return ResponseEntity.ok(memberService.signup(signupRequest));
    }
}
