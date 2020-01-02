package ru.astrizhachuk.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.astrizhachuk.Main.createOptions;

class ExecuteCommandTest {

    public static final String PATH_TO_SHORT_RESPONSE_JSON = "./src/test/resources/short-response.json";
    public static final String PATH_TO_FULL_CONFIGURATION_FILE = "./src/test/resources/configuration/configuration.json";

    @Test
    void testExecuteFromFileWithoutConfig() throws ParseException {

        Options options = createOptions();
        DefaultParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-f", PATH_TO_SHORT_RESPONSE_JSON});
        Command command = new ExecuteCommand(cmd);
        int result = command.execute();

        assertThat(result).isEqualTo(0);
    }

    @Test
    void testExecuteFromFileWithConfig() throws ParseException {

        Options options = createOptions();
        DefaultParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options,
                new String[]{"-f", PATH_TO_SHORT_RESPONSE_JSON, "-c", PATH_TO_FULL_CONFIGURATION_FILE});
        Command command = new ExecuteCommand(cmd);
        int result = command.execute();

        assertThat(result).isEqualTo(0);
    }
}