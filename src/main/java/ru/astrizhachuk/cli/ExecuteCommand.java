package ru.astrizhachuk.cli;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import ru.astrizhachuk.configuration.Configuration;
import ru.astrizhachuk.http.HttpClient;
import ru.astrizhachuk.metadata.Metadata;
import ru.astrizhachuk.reporter.Reporter;
import ru.astrizhachuk.reporter.ReporterFactory;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

@Slf4j
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

        Metadata metadata;
        if (!destMetaFile.isEmpty()) {
            metadata = Metadata.create(new File(destMetaFile));
        } else {
            HttpClient httpClient = HttpClient.create(cmd);
            try {
                metadata = Metadata.create(httpClient.getResponseByteStream());
            } catch (IOException e) {
                LOGGER.error("Can't get response stream from service", e);
                return 1;
            }
        }

        Collection<String> tables = metadata.collectByConfig(configuration);

        Reporter reporter = ReporterFactory.create(cmd);
        reporter.report(tables);

        return 0;
    }
}
