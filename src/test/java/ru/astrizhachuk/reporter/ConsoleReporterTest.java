package ru.astrizhachuk.reporter;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

class ConsoleReporterTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    void report() {

        // given
        ArrayList<String> test = new ArrayList<>();
        test.add("t1");
        test.add("t2");

        // when
        Reporter reporter = new ConsoleReporter();
        reporter.report(test);

        // then
        assertThat(outContent.toString())
                .containsIgnoringCase(String.format(Reporter.TRUNCATE_COMMAND_STRING, test.get(0)));
        assertThat(outContent.toString())
                .containsIgnoringCase(String.format(Reporter.TRUNCATE_COMMAND_STRING, test.get(1)));

    }
}