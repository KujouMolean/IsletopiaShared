package com.molean.isletopia.shared.parameter;

import com.molean.isletopia.shared.annotations.AutoInject;
import com.molean.isletopia.shared.annotations.Bean;
import com.molean.isletopia.shared.service.UniversalParameter;

import java.util.UUID;

@Bean
public class PlayerParameter {

    @AutoInject
    private UniversalParameter universalParameter;

    public void set(UUID uuid, String key, String value) {
        universalParameter.setParameter(uuid, key, value);
    }

    public String get(UUID uuid, String key) {
        return universalParameter.getParameter(uuid, key);
    }

    public void unset(UUID uuid, String key) {
        universalParameter.unsetParameter(uuid, key);
    }

}
