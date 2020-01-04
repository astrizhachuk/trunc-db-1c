package ru.astrizhachuk.reporter;

import org.apache.commons.cli.CommandLine;

public class ReporterFactory {

    private ReporterFactory() {

    }

    public static Reporter create(CommandLine cmd) {
        String destOutputFile = cmd.getOptionValue("o", "");
        if (destOutputFile.isEmpty()) {
            return new ConsoleReporter();
        } else {
            return new FileReporter(destOutputFile);
        }
    }

}
