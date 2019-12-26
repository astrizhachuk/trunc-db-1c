package ru.astrizhachuk.cli;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import static ru.astrizhachuk.Main.APP_NAME;

@Slf4j
public class ParseExceptionCommand implements Command {

    private final Options options;
    private final ParseException e;

    public ParseExceptionCommand(Options options, ParseException e) {
        this.options = options;
        this.e = e;
    }

    @Override
    public int execute() {

        LOGGER.error(e.getMessage());

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(APP_NAME, options, true);

        return 1;
    }
}
