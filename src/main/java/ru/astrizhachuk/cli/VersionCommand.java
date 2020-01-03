package ru.astrizhachuk.cli;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

@Slf4j
public class VersionCommand implements Command {

    @Override
    public int execute() {

        final InputStream mfStream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("META-INF/MANIFEST.MF");

        Manifest manifest = new Manifest();
        try {
            manifest.read(mfStream);
        } catch (IOException e) {
            LOGGER.error("Can't read manifest", e);
        }

        System.out.print(String.format(
                "version: %s%n",
                manifest.getMainAttributes().getValue(Attributes.Name.IMPLEMENTATION_VERSION)
        ));
        return 0;
    }
}
