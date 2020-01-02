package ru.astrizhachuk.cli;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class VersionCommandTest {

    @Test
    void testExecute() {

        Command command = new VersionCommand();
        int result = command.execute();

        assertThat(result).isEqualTo(0);
    }

}