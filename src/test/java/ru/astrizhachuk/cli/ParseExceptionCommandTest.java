package ru.astrizhachuk.cli;

import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.astrizhachuk.Main.createOptions;

class ParseExceptionCommandTest {

    @Test
    void testExecute() {
        Command command = new ParseExceptionCommand(createOptions(), new ParseException(""));
        int result = command.execute();
        assertThat(result).isEqualTo(1);
    }

}