package com.molean.isletopia.shared.model;

import com.molean.isletopia.shared.utils.UUIDManager;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Mail {
    private int id;
    @Nullable
    private UUID source;
    private UUID target;
    private String message;
    private LocalDateTime localDateTime;
    private byte[] data;
    private boolean claimed;

    @Override
    public String toString() {
        String source = "系统";
        if (this.source != null) {
            source = UUIDManager.get(this.source);
        }
        return """
                来自 %s 的包裹
                %s
                                
                发件日期: %s
                """.formatted(source, message, localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public @Nullable UUID getSource() {
        return source;
    }

    public void setSource(@Nullable UUID source) {
        this.source = source;
    }

    public UUID getTarget() {
        return target;
    }

    public void setTarget(UUID target) {
        this.target = target;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public boolean isClaimed() {
        return claimed;
    }

    public void setClaimed(boolean claimed) {
        this.claimed = claimed;
    }
}
