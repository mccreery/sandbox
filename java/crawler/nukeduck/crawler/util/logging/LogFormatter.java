package nukeduck.crawler.util.logging;

import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class LogFormatter extends Formatter {
	public static final SimpleDateFormat TIMESTAMP = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public String format(LogRecord record) {
		StringBuilder builder = new StringBuilder();

		builder.append('[');
		builder.append(TIMESTAMP.format(record.getMillis()));
		builder.append("] [");
		builder.append(record.getSourceClassName());
		builder.append('#');
		builder.append(record.getSourceMethodName());
		builder.append("] ");
		builder.append(record.getLevel());
		builder.append(": ");
		builder.append(record.getMessage());

		return builder.toString();
	}
}
