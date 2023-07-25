package dev.crevan.l2j.c1;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class ConsoleLogFormatter extends Formatter {

    private static final String CRLF = "\r\n";

    @Override
    public String format(final LogRecord record) {
        StringBuffer output = new StringBuffer();

        output.append(record.getMessage());
        output.append(CRLF);

        return output.toString();
    }
}
