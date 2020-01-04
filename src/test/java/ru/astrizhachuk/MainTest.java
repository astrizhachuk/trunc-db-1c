package ru.astrizhachuk;

import com.ginsberg.junit.exit.ExpectSystemExitWithStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.astrizhachuk.Main.APP_NAME;

class MainTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    @ExpectSystemExitWithStatus(0)
    void testHelp() {
        // given
        String[] args = new String[]{"--help"};

        // when
        try {
            Main.main(args);
        } catch (RuntimeException ignored) {
            // catch prevented system.exit call
        }

        // then
        assertThat(outContent.toString()).containsIgnoringCase(APP_NAME);
    }

    @Test
    @ExpectSystemExitWithStatus(1)
    void testParseError() {
        // given
        String[] args = new String[]{"--error"};

        // when
        try {
            Main.main(args);
        } catch (RuntimeException ignored) {
            // catch prevented system.exit call
        }

        // then
        assertThat(errContent.toString()).containsIgnoringCase("Unrecognized option: --error");
    }

    @Test
    @ExpectSystemExitWithStatus(0)
    void testVersion() {
        // given
        String[] args = new String[]{"--version"};

        // when
        try {
            Main.main(args);
        } catch (RuntimeException ignored) {
            // catch prevented system.exit call
        }

        // then
        assertThat(outContent.toString()).containsIgnoringCase("version");
    }

    @Test
    @ExpectSystemExitWithStatus(1)
    void testServerWithoutArg() {
        // given
        String[] args = new String[]{"-s"};

        // when
        try {
            Main.main(args);
        } catch (RuntimeException ignored) {
            // catch prevented system.exit call
        }

        // then
        assertThat(errContent.toString()).containsIgnoringCase("missing argument for option");
    }

    @Test
    @ExpectSystemExitWithStatus(1)
    void testServerWithoutBase() {
        // given
        String[] args = new String[]{"-s", "server"};

        // when
        try {
            Main.main(args);
        } catch (RuntimeException ignored) {
            // catch prevented system.exit call
        }

        // then
        assertThat(errContent.toString()).containsIgnoringCase("specify the source of the metadata");
    }

    @Test
    @ExpectSystemExitWithStatus(1)
    void testServerWithEmptyBase() {
        // given
        String[] args = new String[]{"-s", "server", "-b"};

        // when
        try {
            Main.main(args);
        } catch (RuntimeException ignored) {
            // catch prevented system.exit call
        }

        // then
        assertThat(errContent.toString()).containsIgnoringCase("missing argument for option");
    }

    @Test
    @ExpectSystemExitWithStatus(1)
    void testBaseWithoutServer() {
        // given
        String[] args = new String[]{"-b", "base"};

        // when
        try {
            Main.main(args);
        } catch (RuntimeException ignored) {
            // catch prevented system.exit call
        }

        // then
        assertThat(errContent.toString()).containsIgnoringCase("specify the source of the metadata");
    }

    @Test
    @ExpectSystemExitWithStatus(0)
    void testFileWithoutServer() {
        // given
        String[] args = new String[]{"-f", "file"};

        // when
        try {
            Main.main(args);
        } catch (RuntimeException ignored) {
            // catch prevented system.exit call
        }

        // then
        assertThat(outContent.toString()).isEmpty();
    }

    @Test
    @ExpectSystemExitWithStatus(1)
    void testFileWithoutArg() {
        // given
        String[] args = new String[]{"-f"};

        // when
        try {
            Main.main(args);
        } catch (RuntimeException ignored) {
            // catch prevented system.exit call
        }

        // then
        assertThat(errContent.toString()).containsIgnoringCase("missing argument for option");
    }

    @Test
    @ExpectSystemExitWithStatus(0)
    void testFileWithConfig() {
        // given
        String[] args = new String[]{"-f", "file", "-c", "config"};

        // when
        try {
            Main.main(args);
        } catch (RuntimeException ignored) {
            // catch prevented system.exit call
        }

        // then
        assertThat(outContent.toString()).isEmpty();
    }

    @Test
    @ExpectSystemExitWithStatus(1)
    void testWithoutParameters() {
        // given
        String[] args = new String[]{};

        // when
        try {
            Main.main(args);
        } catch (RuntimeException ignored) {
            // catch prevented system.exit call
        }

        // then
        assertThat(errContent.toString()).containsIgnoringCase("specify the source of the metadata");
    }
}