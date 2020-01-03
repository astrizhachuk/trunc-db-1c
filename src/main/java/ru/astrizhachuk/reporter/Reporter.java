package ru.astrizhachuk.reporter;

import java.util.Collection;

public interface Reporter {

    String TRUNCATE_COMMAND_STRING = "TRUNCATE TABLE %s;";

    void report(Collection<String> report);

}
