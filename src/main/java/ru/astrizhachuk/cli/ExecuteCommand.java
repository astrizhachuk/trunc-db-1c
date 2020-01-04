package ru.astrizhachuk.cli;

import org.apache.commons.cli.CommandLine;
import ru.astrizhachuk.configuration.Configuration;
import ru.astrizhachuk.http.HttpClient;
import ru.astrizhachuk.metadata.MetadataParser;
import ru.astrizhachuk.reporter.Reporter;
import ru.astrizhachuk.reporter.ReporterFactory;

import java.io.File;
import java.io.IOException;

public class ExecuteCommand implements Command {
    private final CommandLine cmd;

    public ExecuteCommand(CommandLine cmd) {
        this.cmd = cmd;
    }

    @Override
    public int execute() {

        String destConfigFile = cmd.getOptionValue("c", "");
        Configuration configuration = Configuration.create(new File(destConfigFile));

        String destMetaFile = cmd.getOptionValue("f", "");

        MetadataParser metadataParser = null;
        if (!destMetaFile.isEmpty()) {
            metadataParser = MetadataParser.create(new File(destMetaFile));
        } else {
            HttpClient httpClient = HttpClient.create(cmd);
            try {
                metadataParser = MetadataParser.create(httpClient.getResponseByteStream());
            } catch (IOException e) {
                e.printStackTrace();
                return 1;
            }
        }
// TODO реализовать прогон configuration по metadataParser

        Reporter reporter = ReporterFactory.create(cmd);
        reporter.report(metadataParser.getTables().values());

        return 0;
    }

}
