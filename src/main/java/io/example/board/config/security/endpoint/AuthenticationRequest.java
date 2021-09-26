package io.example.board.config.security.endpoint;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;

import java.util.List;

/**
 * @author : choi-ys
 * @date : 2021-09-27 오전 8:02
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthenticationRequest {
    private HttpMethod httpMethod;
    private List<String> scope;

    public AuthenticationRequest(HttpMethod httpMethod, List<String> scope) {
        this.httpMethod = httpMethod;
        this.scope = scope;
    }
}