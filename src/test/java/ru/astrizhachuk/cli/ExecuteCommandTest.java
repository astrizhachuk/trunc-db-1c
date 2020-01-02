package ru.astrizhachuk.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.astrizhachuk.Main.createOptions;

class ExecuteCommandTest {

    @Test
    void testExecuteFile() throws ParseException {

        Options options = createOptions();
        DefaultParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-f", "./src/test/resources/short-response.json"});
        Command command = new ExecuteCommand(cmd);
        int result = command.execute();

        assertThat(result).isEqualTo(0);
    }
}