package com.molean.isletopia.shared.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class PlayerParameterDao {

    public static List<UUID> players() {
        ArrayList<UUID> playerNames = new ArrayList<>();
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = "select distinct uuid from minecraft.isletopia_parameters ";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                try {
                    playerNames.add(UUID.fromString(resultSet.getString(1)));
                } catch (Exception ignored) {
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return playerNames;
    }
    public static String get(UUID uuid, String key) {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = "select p_value from minecraft.isletopia_parameters where uuid=? and p_key=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setString(2, key);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public static ArrayList<String> keys(UUID uuid) {
        ArrayList<String> keyList = new ArrayList<>();
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = "select p_key from minecraft.isletopia_parameters where uuid=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, uuid.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                keyList.add(resultSet.getString(1));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
        return keyList;
    }

    public static Map<String, String> properties(UUID uuid) {
        HashMap<String, String> map = new HashMap<>();
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = "select p_key,p_value from minecraft.isletopia_parameters where uuid=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, uuid.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String key = resultSet.getString(1);
                String value = resultSet.getString(2);
                if (key != null && value != null) {
                    map.put(key, value);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
        return map;
    }

    public static void set(UUID uuid, String key, String value) {
        if (value == null || value.isEmpty()) {
            unset(uuid, key);
            return;
        }
        if (!exist(uuid, key)) {
            insert(uuid, key, value);
        } else {
            try (Connection connection = DataSourceUtils.getConnection()) {
                String sql = "update minecraft.isletopia_parameters set p_value=? where uuid=? and p_key=?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, value);
                preparedStatement.setString(2, uuid.toString());
                preparedStatement.setString(3, key);
                preparedStatement.execute();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public static void unset(UUID uuid, String key) {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = "delete from minecraft.isletopia_parameters where uuid=? and p_key=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setString(2, key);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void insert(UUID uuid, String key, String value) {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = "insert into minecraft.isletopia_parameters(uuid,p_key,p_value) values(?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setString(2, key);
            preparedStatement.setString(3, value);
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static boolean exist(UUID uuid, String key) {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = "select * from minecraft.isletopia_parameters where uuid=? and p_key=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setString(2, key);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public static void deletePlayer(UUID uuid) throws SQLException {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = "delete from minecraft.isletopia_parameters where uuid=? ";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.executeUpdate();
        }
    }

    public static void replace(UUID source, UUID target) throws SQLException {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = "update  minecraft.isletopia_parameters set uuid=? where uuid=? ";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, target.toString());
            preparedStatement.setString(2, source.toString());
            preparedStatement.executeUpdate();
        }
    }
}
