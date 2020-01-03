package ru.astrizhachuk.reporter;

import lombok.AllArgsConstructor;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

@AllArgsConstructor
public class FileReporter implements Reporter {

    private File file;

    public FileReporter(String name) {
        this(new File((name.isEmpty() ? "./output.sql" : name)));
    }

    @Override
    public void report(Collection<String> report) {
        try {
            FileUtils.writeLines(file, "UTF-8", report);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
