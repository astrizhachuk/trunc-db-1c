package ru.astrizhachuk.cli;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.astrizhachuk.Main.createOptions;

class HelpCommandTest {
    @Test
    void testExecute() {

        Command command = new HelpCommand(createOptions());
        int result = command.execute();

        assertThat(result).isEqualTo(0);
    }
}