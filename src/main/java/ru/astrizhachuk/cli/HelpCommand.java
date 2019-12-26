package ru.astrizhachuk.cli;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import static ru.astrizhachuk.Main.APP_NAME;

public class HelpCommand implements Command {

    private final Options options;

    public HelpCommand(Options options) {
        this.options = options;
    }

    @Override
    public int execute() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(APP_NAME, options, true);
        return 0;
    }
}
