package io.example.board.config.security.jwt.common;

/**
 * @author : choi-ys
 * @date : 2021/09/21 11:31 오후
 */
public enum ClaimKey {
    ISS("iss"),
    SUB("sub"),
    AUD("aud"),
    IAT("iat"),
    EXP("exp"),
    USE("use"),
    USERNAME("username"),
    AUTHORITIES("authorities");

    public String value;

    ClaimKey(String value) {
        this.value = value;
    }
}
