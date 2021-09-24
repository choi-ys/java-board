package io.example.board.config.redis;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author : choi-ys
 * @date : 2021/09/24 1:32 오전
 */
public enum OS {
    WINDOWS("netstat -nao | find \"LISTEN\" | find \"%d\"", Set.of("cmd.exe", "/y", "/c")),
    OTHERS("netstat -nat | grep LISTEN | grep %d", Set.of("/bin/sh", "-c"));

    private String command;
    public Set<String> shell;

    OS(String command, Set<String> shell) {
        this.command = command;
        this.shell = shell;
    }

    public String[] mapTo(int port) {
        LinkedHashSet<String> mutableShellCommandSet = new LinkedHashSet<>();
        mutableShellCommandSet.addAll(shell);
        mutableShellCommandSet.add(String.format(command, port));
        return mutableShellCommandSet.toArray(new String[mutableShellCommandSet.size()]);
    }
}
