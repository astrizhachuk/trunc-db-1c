package ru.astrizhachuk.reporter;

public final class CommandTemplates {

    public static final String TRUNCATE_COMMAND_STRING = "TRUNCATE TABLE %s;";

    private CommandTemplates() {
        throw new IllegalStateException("Utility class");
    }
}
