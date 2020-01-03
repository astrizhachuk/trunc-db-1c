package ru.astrizhachuk.reporter;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FileReporterTest {

    private final String filename = "./output.sql";
    private final File file = new File(filename);

    @BeforeEach
    void setUp() {
        FileUtils.deleteQuietly(file);
    }

    @AfterEach
    void tearDown() {
        FileUtils.deleteQuietly(file);
    }

    @Test
    void reportWithoutName() throws IOException {

        // given
        ArrayList<String> test = new ArrayList<>();
        test.add("t1");
        test.add("t2");

        // when
        Reporter reporter = new FileReporter("");
        reporter.report(test);

        // then
        List<String> stringList = FileUtils.readLines(file, "UTF-8");
        assertThat(stringList).hasSize(2);
        assertThat(stringList.get(0))
                .containsIgnoringCase(String.format(Reporter.TRUNCATE_COMMAND_STRING, test.get(0)));
        assertThat(stringList.get(1))
                .containsIgnoringCase(String.format(Reporter.TRUNCATE_COMMAND_STRING, test.get(1)));

    }

    @Test
    void reportWithName() throws IOException {

        // given
        ArrayList<String> test = new ArrayList<>();
        test.add("t1");
        test.add("t2");

        // when
        Reporter reporter = new FileReporter(filename);
        reporter.report(test);

        // then
        List<String> stringList = FileUtils.readLines(file, "UTF-8");
        assertThat(stringList).hasSize(2);
        assertThat(stringList.get(0))
                .containsIgnoringCase(String.format(Reporter.TRUNCATE_COMMAND_STRING, test.get(0)));
        assertThat(stringList.get(1))
                .containsIgnoringCase(String.format(Reporter.TRUNCATE_COMMAND_STRING, test.get(1)));

    }

    @Test
    void reportWithFile() throws IOException {

        // given
        ArrayList<String> test = new ArrayList<>();
        test.add("t1");
        test.add("t2");

        // when
        Reporter reporter = new FileReporter(file);
        reporter.report(test);

        // then
        List<String> stringList = FileUtils.readLines(file, "UTF-8");
        assertThat(stringList).hasSize(2);
        assertThat(stringList.get(0))
                .containsIgnoringCase(String.format(Reporter.TRUNCATE_COMMAND_STRING, test.get(0)));
        assertThat(stringList.get(1))
                .containsIgnoringCase(String.format(Reporter.TRUNCATE_COMMAND_STRING, test.get(1)));

    }
}