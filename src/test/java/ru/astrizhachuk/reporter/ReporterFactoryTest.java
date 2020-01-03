package ru.astrizhachuk.reporter;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.astrizhachuk.Main.createOptions;

class ReporterFactoryTest {

    @Test
    void testReportFactoryWithoutOption() throws ParseException {
        // given
        Options options = createOptions();
        DefaultParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options,
                new String[]{""});

        // when
        Reporter reporter = ReporterFactory.create(cmd);

        // then
        assertThat(reporter).isInstanceOf(ConsoleReporter.class);
    }

    @Test
    void testReportFactoryWithEmptyOption() throws ParseException {

        // given
        Options options = createOptions();
        DefaultParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options,
                new String[]{"-o", ""});

        // when
        Reporter reporter = ReporterFactory.create(cmd);

        // then
        assertThat(reporter).isInstanceOf(ConsoleReporter.class);
    }

    @Test
    void testReportFactoryWithFile() throws ParseException {

        // given
        Options options = createOptions();
        DefaultParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options,
                new String[]{"-o", "./any"});

        // when
        Reporter reporter = ReporterFactory.create(cmd);

        // then
        assertThat(reporter).isInstanceOf(FileReporter.class);
    }
}