package com.molean.isletopia.shared.parameter;

import com.molean.isletopia.shared.database.ParameterDao;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;

public class ParameterObject<O> {
    private final Map<String, String> map;

    public ParameterObject(String namespace, O obj) {
        this.map = ParameterDao.map(namespace, obj.toString());
    }

    @Nullable
    public String get(String key) {
        return map.get(key);
    }

    @Nullable
    public String getAsString(String key) {
        String s = get(key);
        return Objects.requireNonNullElse(s, "");
    }

    public boolean getAsBoolean(String key) {
        return "true".equalsIgnoreCase(get(key));
    }

    public int getAsInteger(String key) {
        try {
            String s = get((key));
            if (s == null) {
                return 0;
            } else {
                return Integer.parseInt(s);
            }
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public LocalDateTime getAsLocalDateTime(String key) {
        String s = get(key);
        if (s == null) {
            return null;
        }
        return LocalDateTime.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public LocalDateTime getAsStringList(String key) {
        String s = get(key);
        if (s == null) {
            return null;
        }
        return LocalDateTime.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

}
