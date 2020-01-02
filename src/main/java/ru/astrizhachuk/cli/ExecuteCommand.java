package ru.astrizhachuk.cli;

import org.apache.commons.cli.CommandLine;
import ru.astrizhachuk.configuration.Configuration;

import java.io.File;

public class ExecuteCommand implements Command {
    private final CommandLine cmd;

    public ExecuteCommand(CommandLine cmd) {
        this.cmd = cmd;
    }

    @Override
    public int execute() {

        String destConfiguration = cmd.getOptionValue("c", "");
        Configuration configuration = Configuration.create(new File(destConfiguration));


        //TODO ex
        //String source = cmd.getOptionValue("f", "");
        //Path filePath = path(source);
        //MetadataParser metadataParser = MetadataParser.create(new File(source));
        //String report = cmd.getOptionValue("r", "");

        return 0;
    }

}
