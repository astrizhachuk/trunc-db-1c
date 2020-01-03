package ru.astrizhachuk.reporter;

import java.util.Collection;

public class ConsoleReporter implements Reporter {
    @Override
    public void report(Collection<String> report) {
        for (String o : report) {
            System.out.print(String.format("TRUNCATE TABLE %s;%n", o));
        }
    }
}
