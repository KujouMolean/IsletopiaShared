package com.molean.isletopia.shared.utils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class I18n {
    private static final Map<Locale, ResourceBundle> resourceBundleMap = new HashMap<>();

    @SafeVarargs
    public static String getMessage(Locale locale, String key, Pair<String, String>... pairs) {
        ResourceBundle resourceBundle;
        if (resourceBundleMap.containsKey(locale)) {
            resourceBundle = resourceBundleMap.get(locale);
        } else {
            resourceBundle = ResourceBundle.getBundle("message", locale);
            resourceBundleMap.put(locale, resourceBundle);
        }
        if (!resourceBundle.containsKey(key)) {
            return key;
        }
        String raw = resourceBundle.getString(key);
        for (Pair<String, String> pair : pairs) {
            raw = raw.replaceAll("<" + pair.getKey() + ">", pair.getValue());
        }
        return raw;
    }
}
