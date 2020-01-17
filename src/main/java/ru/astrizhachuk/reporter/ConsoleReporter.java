package ru.astrizhachuk.reporter;

import java.util.Collection;

public class ConsoleReporter implements Reporter {

    @Override
    public void report(Collection<String> report) {
        for (String o : report) {
            System.out.println(String.format(CommandTemplates.TRUNCATE_COMMAND_STRING, o));
        }
    }
}
