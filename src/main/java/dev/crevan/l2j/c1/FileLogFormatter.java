package dev.crevan.l2j.c1;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class FileLogFormatter extends Formatter {

    private static final String CRLF = "\r\n";
    private static final String TAB = "\t";


    @Override
    public String format(final LogRecord record) {
        StringBuffer output = new StringBuffer();
        output.append(record.getMillis()).append(TAB);
        output.append(record.getLevel().getName()).append(TAB);
        output.append(record.getThreadID()).append(TAB);
        output.append(record.getLoggerName()).append(TAB);
        output.append(record.getMessage()).append(TAB);
        output.append(CRLF);
        return output.toString();
    }
}
