package io.example.board.config.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.util.StringUtils;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author : choi-ys
 * @date : 2021/09/24 12:53 오전
 */
@Configuration
@Profile(value = {"test", "local"})
@Slf4j
public class EmbeddedRedisConfig {

    @Value("${spring.redis.port}")
    private int redisPort;

    private RedisServer redisServer;
    private OS os = OS.WINDOWS;

    @PostConstruct
    public void redisServer() throws IOException {
        int port = isRedisRunning() ? findAvailablePort() : redisPort;
        redisServer = new RedisServer(port);

        try {
            redisServer.start();
        } catch (BeanCreationException e) {
            port = findAvailablePort();
            redisServer = new RedisServer(port);
        }
        log.debug("[Embedded Redis ::] Start on [{}] by [{}] port", System.getProperty("os.name"), port);
    }

    @PreDestroy
    public void stopRedis() {
        if (redisServer != null) {
            redisServer.stop();
        }
    }

    /**
     * Embedded Redis가 현재 실행중인지 확인
     */
    private boolean isRedisRunning() throws IOException {
        return isRunning(executeGrepProcessCommand(redisPort));
    }

    /**
     * 현재 PC/서버에서 사용가능한 포트 조회
     */
    public int findAvailablePort() throws IOException {

        for (int port = 10000; port <= 65535; port++) {
            Process process = executeGrepProcessCommand(port);
            if (!isRunning(process)) {
                log.debug("[Embedded Redis ::] Running by [{}] port... find not using [{}] port", redisPort, port);
                return port;
            }
        }

        throw new IllegalArgumentException("[Embedded Redis ::] Not Found Available port: 10000 ~ 65535");
    }

    /**
     * 해당 port를 사용중인 프로세스 확인하는 sh 실행
     */
    private Process executeGrepProcessCommand(int port) throws IOException {
        os = System.getProperty("os.name").toLowerCase().contains("win") ? OS.WINDOWS : OS.OTHERS;
        String command;
        String[] shell;

        switch (os) {
            case WINDOWS:
                command = String.format("netstat -nao | find \"LISTEN\" | find \"%d\"", port);
                shell = new String[]{"cmd.exe", "/y", "/c", command};
                break;
            case OTHERS:
                command = String.format("netstat -nat | grep LISTEN | grep %d", port);
                shell = new String[]{"/bin/sh", "-c", command};
                break;
            default:
                throw new IllegalStateException("[Embedded Redis ::] Unexpected value: " + os);
        }

        return Runtime.getRuntime().exec(shell);
    }

    /**
     * 해당 Process가 현재 실행중인지 확인
     */
    private boolean isRunning(Process process) {
        String line = "";
        StringBuilder pidInfo = new StringBuilder();

        try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {

            while ((line = input.readLine()) != null) {
                pidInfo.append(line);
            }

        } catch (Exception e) {
        }
        log.debug("[Embedded Redis ::] Pid info [{}] by execute command [{}], check isRunning -> {}",
                pidInfo, line, !StringUtils.hasText(pidInfo.toString())
        );
        return StringUtils.hasText(pidInfo.toString());
    }
}
