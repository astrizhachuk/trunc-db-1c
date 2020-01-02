package ru.astrizhachuk.cli;

import lombok.SneakyThrows;
import org.apache.commons.cli.CommandLine;
import ru.astrizhachuk.metadata.MetadataParser;

import java.io.File;
import java.nio.file.Path;

public class ExecuteCommand implements Command {
    private final CommandLine cmd;

    public ExecuteCommand(CommandLine cmd) {
        this.cmd = cmd;
    }

    @Override
    public int execute() {

        //TODO ex
        String source = cmd.getOptionValue("f", "");
        Path filePath = path(source);
        MetadataParser metadataParser = MetadataParser.create(new File(source));
        String report = cmd.getOptionValue("r", "");

        return 0;
    }

    @SneakyThrows
    public Path path(String file) {
        return new File(file).getCanonicalFile().toPath().toAbsolutePath();
    }

}
