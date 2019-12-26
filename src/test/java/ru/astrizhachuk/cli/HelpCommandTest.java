package ru.astrizhachuk.cli;

import org.apache.commons.cli.Options;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HelpCommandTest {
    @Test
    void testExecute() {
        Options options = new Options();
        options.addOption("t", "test", false, "test...");

        Command command = new HelpCommand(options);
        int result = command.execute();

        assertThat(result).isEqualTo(0);
    }
}