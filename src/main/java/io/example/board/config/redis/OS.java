package io.example.board.config.redis;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : choi-ys
 * @date : 2021/09/24 1:32 오전
 */
public enum OS {
    WINDOWS("netstat -nao | find \"LISTEN\" | find \"%d\"", List.of("cmd.exe", "/y", "/c")),
    OTHERS("netstat -nat | grep LISTEN | grep %d", List.of("/bin/sh", "-c"));

    private String command;
    public List<String> shell;

    OS(String command, List<String> shell) {
        this.command = command;
        this.shell = shell;
    }

    public String[] mapTo(int port) {
        ArrayList<String> mutableShellCommandList = new ArrayList<>();
        mutableShellCommandList.addAll(shell);
        mutableShellCommandList.add(String.format(command, port));
        return mutableShellCommandList.toArray(new String[mutableShellCommandList.size()]);
    }
}
