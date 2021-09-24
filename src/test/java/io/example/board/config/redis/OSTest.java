package io.example.board.config.redis;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author : choi-ys
 * @date : 2021-09-25 오전 2:04
 */
@DisplayName("config:EmbeddedRedis")
class OSTest {
    @Test
    @DisplayName("OS.WINDOWS: shell command 반환")
    public void windowsExecuteShellCommand() {
        // Given
        int port = 6379;
        Set<String> windowsShellCommandSet = Set.of("cmd.exe", "/y", "/c", String.format("netstat -nao | find \"LISTEN\" | find \"%d\"", port));

        // When
        String[] expected = OS.WINDOWS.mapTo(port);

        // Then
        assertTrue(Arrays.stream(expected).collect(Collectors.toSet()).containsAll(windowsShellCommandSet), "Windows에서 실행되어야 하는 shell command 반환 여부 확인");
        System.out.println(Arrays.toString(expected));
    }

    @Test
    @DisplayName("OS.OTHERS : shell command 반환")
    public void othersExecuteShellCommand() {
        // Given
        int port = 6379;
        Set<String> othersShellCommandSet = Set.of("/bin/sh", "-c", String.format("netstat -nat | grep LISTEN | grep %s", port));

        // When
        String[] expected = OS.OTHERS.mapTo(port);

        // Then
        assertTrue(Arrays.stream(expected).collect(Collectors.toSet()).containsAll(othersShellCommandSet), "Mac, Linux의 환경에서 실행되어야 하는 shell command 반환 여부 확인");
        System.out.println(Arrays.toString(expected));
    }
}