package ru.astrizhachuk.cli;

import org.apache.commons.cli.CommandLine;

public class ExecuteCommand implements Command {
    private final CommandLine cmd;

    public ExecuteCommand(CommandLine cmd) {
        this.cmd = cmd;
    }

    @Override
    public int execute() {
        return 0;
    }
}
