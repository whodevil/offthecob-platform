package info.offthecob.jvm.platform.logging;

import ch.qos.logback.classic.pattern.ThrowableProxyConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.core.LayoutBase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.time.ZoneOffset.UTC;
import static java.time.ZonedDateTime.now;
import static java.time.format.DateTimeFormatter.ISO_INSTANT;

public class StructuredLoggingLayout extends LayoutBase<ILoggingEvent> {
    public static final Type JSON_MAP_TYPE = new TypeToken<Map<String, Object>>() {}.getType();
    public static final String JSON = "JSON";
    private final Gson gson = new Gson();
    private final ThrowableProxyConverter throwableProxyConverter = new ThrowableProxyConverter();

    @Override
    public void start() {
        throwableProxyConverter.start();
        super.start();
    }

    @Override
    public String doLayout(ILoggingEvent event) {
        var message = new HashMap<String, Object>();
        message.put("timestamp", now(UTC).format(ISO_INSTANT));
        message.put("level", event.getLevel().toString());
        message.put("message", event.getFormattedMessage());
        message.put("service", event.getLoggerContextVO().getName());
        message.put("logger", event.getLoggerName());
        message.put("thread", event.getThreadName());
        message.putAll(json(event));
        message.putAll(stacktrace(event));
        return gson.toJson(message);
    }

    private Map<String, Object> stacktrace(ILoggingEvent event) {
        IThrowableProxy throwableProxy = event.getThrowableProxy();
        var map = new HashMap<String, Object>();
        if(throwableProxy != null) {
            String stack = throwableProxyConverter.convert(event);
            List<String> stackList = Arrays.stream(stack.split("\n"))
                    .map((s) -> s.replace("\t", ""))
                    .collect(Collectors.toList());
            map.put("stacktrace", stackList);
        }
        return map;
    }

    private Map<String, Object> json(ILoggingEvent event) {
        var map = new HashMap<String, Object>();
        if(event.getMDCPropertyMap().containsKey(JSON)) {
            map.putAll(gson.fromJson(event.getMDCPropertyMap().get(JSON), JSON_MAP_TYPE));
        }
        return map;
    }
}
