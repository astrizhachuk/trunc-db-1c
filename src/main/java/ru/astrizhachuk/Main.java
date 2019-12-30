package ru.astrizhachuk;


import org.apache.commons.cli.*;
import ru.astrizhachuk.cli.*;

public class Main {
    public static final String APP_NAME = "Truncate DB tables for 1C";
    private static final Options options = createOptions();

    public static void main(String[] args) {

        CommandLineParser parser = new DefaultParser();
        Command command;
        try {

            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption("v")) {
                command = new VersionCommand();
            } else if (cmd.hasOption("h")) {
                command = new HelpCommand(options);
            } else {
                if (!cmd.hasOption("b") && !cmd.hasOption("f")) {
                    throw new ParseException("Specify the source of the metadata!");
                }
                command = new ExecuteCommand(cmd);
            }

        } catch (ParseException e) {
            command = new ParseExceptionCommand(options, e);
        }

        int result = command.execute();
        if (result >= 0) {
            System.exit(result);
        }
    }

    public static Options createOptions() {
        Options options = new Options();
        options.addOption(Option.builder("v")
                .longOpt("version")
                .desc("print the version of the application")
                .build());
        options.addOption(Option.builder("h")
                .longOpt("help")
                .desc("print this message")
                .build());
        options.addOption(Option.builder("s")
                .longOpt("server")
                .hasArg()
                .argName("SERVER")
                .desc("server with metadata service, like http://dmz-esb03.stdp.ru:8087")
                .build());
        options.addOption(Option.builder("b")
                .longOpt("base")
                .desc("base name of the 1c application")
                .hasArg()
                .argName("BASE")
                .build());
        options.addOption(Option.builder("f")
                .longOpt("file")
                .desc("json format file with metadata")
                .hasArg()
                .argName("FILE")
                .build());
        return options;
    }
}
