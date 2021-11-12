package com.molean.isletopia.shared.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.molean.isletopia.shared.database.DataSourceUtils;
import com.molean.isletopia.shared.platform.PlatformRelatedUtils;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.function.Consumer;

public class UUIDUtils {

    @Nullable
    public static String get(UUID uuid) {
        //查正版
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = "select name from minecraft.uuid where uuid=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, uuid.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }

    public static UUID getOffline(String player) {
        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + player).getBytes(StandardCharsets.UTF_8));
    }

    @Nullable
    public static UUID get(String player) {
        if (player.startsWith("#")) {
            return UUID.nameUUIDFromBytes(("OfflinePlayer:" + player).getBytes(StandardCharsets.UTF_8));
        } else {
            try (Connection connection = DataSourceUtils.getConnection()) {
                String sql = "select uuid from minecraft.uuid where name=?";

                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, player);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return UUID.fromString(resultSet.getString(1));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return null;
    }

    public static UUID getOnlineSync(String player) {
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + player);
            InputStream inputStream = url.openStream();
            byte[] bytes = inputStream.readAllBytes();
            JsonElement parse = JsonParser.parseString(new String(bytes));
            JsonObject asJsonObject = parse.getAsJsonObject();
            JsonElement id = asJsonObject.get("id");
            String asString = id.getAsString();

            return UUID.fromString(asString.replaceFirst(
                    "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)",
                    "$1-$2-$3-$4-$5"));
        } catch (Exception ignore) {
            return null;
        }
    }


    public static void getOnline(String player, Consumer<UUID> consumer) {
        PlatformRelatedUtils.getInstance().runAsync(() -> {
            try {
                URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + player);
                InputStream inputStream = url.openStream();
                byte[] bytes = inputStream.readAllBytes();
                JsonElement parse = JsonParser.parseString(new String(bytes));
                JsonObject asJsonObject = parse.getAsJsonObject();
                JsonElement id = asJsonObject.get("id");
                String asString = id.getAsString();
                UUID uuid = UUID.fromString(asString.replaceFirst(
                        "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)",
                        "$1-$2-$3-$4-$5"));

                consumer.accept(uuid);
            } catch (Exception ignore) {
                consumer.accept(null);
            }
        });
    }
}
