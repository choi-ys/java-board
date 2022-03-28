package io.example.board.config.security.endpoint;

import org.springframework.http.HttpMethod;

import java.util.Arrays;
import java.util.List;

/**
 * @author : choi-ys
 * @date : 2021-09-27 오전 8:19
 */
public enum AuthenticationEndpointByRoles {

    NONE(Arrays.asList(
            new AuthenticationRequest(HttpMethod.GET, Arrays.asList(
                    "/post/**", "/index/**"
            )),
            new AuthenticationRequest(HttpMethod.POST, Arrays.asList(
                    "/member",
                    "/login"
            ))
    )),
    MEMBER(Arrays.asList(
            new AuthenticationRequest(HttpMethod.POST, Arrays.asList(
                    "/refresh", "/post"
            )),
            new AuthenticationRequest(HttpMethod.PATCH, Arrays.asList(
                    "/post"
            )),
            new AuthenticationRequest(HttpMethod.DELETE, Arrays.asList(
                    "/post/**"
            ))
    ));

    private List<AuthenticationRequest> matchers;

    AuthenticationEndpointByRoles(List<AuthenticationRequest> matchers) {
        this.matchers = matchers;
    }

    public String[] patterns(HttpMethod httpMethod) {
        return matchers.stream()
                .filter(it -> it.getHttpMethod().equals(httpMethod))
                .map(it -> it.getScope())
                .flatMap(arr -> Arrays.stream(arr.toArray(String[]::new)))
                .toArray(String[]::new)
                ;
    }
}
