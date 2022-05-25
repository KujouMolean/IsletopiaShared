package com.molean.isletopia.shared;

import com.molean.isletopia.shared.annotations.Bean;
import com.molean.isletopia.shared.utils.UUIDManager;

public class Configure {

    @Bean
    public UUIDManager uuidManager() {
        return UUIDManager.INSTANCE;
    }
}
