package ru.astrizhachuk.cli;

import lombok.SneakyThrows;
import org.apache.commons.cli.CommandLine;
import ru.astrizhachuk.metadata.Metadata;

import java.io.File;
import java.nio.file.Path;

public class ExecuteCommand implements Command {
    private final CommandLine cmd;

    public ExecuteCommand(CommandLine cmd) {
        this.cmd = cmd;
    }

    @Override
    public int execute() {

        String src = cmd.getOptionValue("f", "");
        Path filePath = path(src);
        Metadata metadata = Metadata.create(new File(src));

        return 0;
    }

    @SneakyThrows
    public Path path(String file) {
        return new File(file).getCanonicalFile().toPath().toAbsolutePath();
    }

}
