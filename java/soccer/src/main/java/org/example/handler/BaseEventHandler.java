package org.example.handler;

import org.example.util.MathUtil;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public class BaseEventHandler {

    public static final Map<String, Function<ButtonEvent, String>> mapping = new LinkedHashMap<>();

    static {
        mapping.put("Average", MathUtil::average);
        mapping.put("Online", e -> new OnlineFetchHandler().handle(e));
    }

    public String handle(ButtonEvent buttonEvent) {
        Function<ButtonEvent, String> consumer = mapping.get(buttonEvent.getCode());
        if (consumer != null) {
            try {
                return consumer.apply(buttonEvent);
            } catch (Exception e) {
                return "Error: " + e.getMessage();
            }
        } else {
            return null;
        }
    }
}
