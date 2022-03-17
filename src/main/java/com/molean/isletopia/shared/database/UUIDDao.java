package com.molean.isletopia.shared.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class UUIDDao {

    public static Map<UUID, String> snapshot() {
        HashMap<UUID, String> uuidStringHashMap = new HashMap<>();
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = "select uuid,name from minecraft.uuid";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String uuidString = resultSet.getString(1);
                if (uuidString == null) {
                    continue;
                }
                UUID uuid = UUID.fromString(resultSet.getString(1));
                String name = resultSet.getString(2);
                if (name == null) {
                    continue;
                }
                uuidStringHashMap.put(uuid, name);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return uuidStringHashMap;
    }

    public static void delete(UUID uuid) {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = "delete from minecraft.uuid where uuid=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void delete(String name) {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = "delete from minecraft.uuid where name=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public static void update(UUID uuid, String name) {
        if (!Objects.equals(query(uuid), name)) {
            if (query(name) != null) {
                delete(name);
            }
            delete(uuid);
            insert(uuid, name);
        }
    }

    public static UUID query(String username) {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = "select uuid from minecraft.uuid where name=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return UUID.fromString(resultSet.getString(1));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public static String query(UUID uuid) {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = "select * from minecraft.uuid where uuid=?";
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

    public static void insert(UUID uuid, String username) {

        UUID query = query(username);
        if (query != null) {
            delete(query);

        }

        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = "insert into minecraft.uuid(name, uuid) values(?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, uuid.toString());
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
