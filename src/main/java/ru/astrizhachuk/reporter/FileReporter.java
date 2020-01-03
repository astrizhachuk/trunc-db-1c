package ru.astrizhachuk.reporter;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class FileReporter implements Reporter {

    private File file;

    public FileReporter(String name) {
        this(new File((name.isEmpty() ? "./output.sql" : name)));
    }

    @SneakyThrows
    @Override
    public void report(Collection<String> report) {

        List<String> lines = report.stream()
                .map(v -> String.format(Reporter.TRUNCATE_COMMAND_STRING, v))
                .collect(Collectors.toList());
        FileUtils.writeLines(file, "UTF-8", lines);

    }
}
