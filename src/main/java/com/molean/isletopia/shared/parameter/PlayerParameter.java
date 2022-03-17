package com.molean.isletopia.shared.parameter;

import com.molean.isletopia.shared.service.UniversalParameter;

import java.util.UUID;

public class PlayerParameter {
    public static void set(UUID uuid, String key, String value) {
        UniversalParameter.setParameter(uuid, key, value);
    }

    public static String get(UUID uuid, String key) {
        return UniversalParameter.getParameter(uuid, key);
    }

    public static void unset(UUID uuid, String key) {
        UniversalParameter.unsetParameter(uuid, key);
    }

}
