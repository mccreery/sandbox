package adventure.glot;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.StreamSupport;

public final class Glot {
    private final ResourceBundle resourceBundle;
    private final FunctionCache<String, MessageFormat> formatCache = new FunctionCache<>(MessageFormat::new);

    public Glot(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    public Locale getLocale() {
        return resourceBundle.getLocale();
    }

    public String localize(String key, Object... arguments) {
        return formatCache.get(resourceBundle.getString(key)).format(arguments);
    }

    public String localize(String key, Iterable<?> arguments) {
        Object[] argumentsArray = StreamSupport.stream(arguments.spliterator(), false).toArray(Object[]::new);
        return localize(key, argumentsArray);
    }
}
