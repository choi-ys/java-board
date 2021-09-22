package io.example.javaboard.domain.vo.login;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : choi-ys
 * @date : 2021/09/22 4:33 오후
 * API 요청 시, JwtFilter에서 Request Header의 JWT로부터 추출한 claims정보의 principal과 authorities를 이용하여
 * SpringSecurityContextHolder에 저장한 User(SpringSecurity.UserDetails)객체를 Application에서 정의한 사용자 정보로 변경하기 위하여
 * LoginUserAdapter를 이용하여 SpringSercuirt의 User객체를 Application에서 정의한 loginUser 객체로 변경 후
 * 해당 Annotation을 이용하여 Spring Security Context Holder에 저장된 인증된 사용자 정보인 LoginUserAdapter로 부터 현재 요청 사용자 정보에 접근
 * 1. 요청 JWT로부터 claims에 담긴 principal과 authorities를 추출
 * 2. 추출된 정보를 이용하여 Spring Security의 User객체를 상속받은 LoginUserAdapter객체로 변환
 * 3. SpringSecurityContextHolder에 저장된 LoginUserAdapter의 Getter와 @AuthenticationPrincipal를 이용하여 LoginUser 객체에 저장된 현재 요청 사용자 정보에 접근
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@AuthenticationPrincipal(expression = "#this =='anonymousUser' ? null : loginUser")
public @interface CurrentUser {
}
