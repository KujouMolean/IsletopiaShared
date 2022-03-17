package com.molean.isletopia.shared.database;

import com.molean.isletopia.shared.utils.Pair;

import java.sql.*;
import java.util.*;

public class CollectionDao {


    public static Set<UUID> getPlayerCollections(UUID uuid) {
        HashSet<UUID> uuids = new HashSet<>();
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    select target from minecraft.collection where source=?
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, uuid.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String string = resultSet.getString(1);
                uuids.add(UUID.fromString(string));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return uuids;
    }

    public static void addCollection(UUID source, UUID target) {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    insert into minecraft.collection(source, target) values (?,?)
                     """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, source.toString());
            preparedStatement.setString(2, target.toString());
            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void removeCollection(UUID source, UUID target) {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    delete from minecraft.collection where source=? and target =?
                     """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, source.toString());
            preparedStatement.setString(2, target.toString());
            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void deleteSource(UUID source) throws SQLException {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    delete from minecraft.collection where source=?
                     """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, source.toString());
            preparedStatement.executeUpdate();
        }
    }

    public static void deleteTarget(UUID target) throws SQLException {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    delete from minecraft.collection where target =?
                     """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, target.toString());
            preparedStatement.executeUpdate();
        }
    }

    public static void replaceSource(UUID source, UUID target) throws SQLException {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    update minecraft.collection set source=? where source=?
                     """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, target.toString());
            preparedStatement.setString(2, source.toString());
            preparedStatement.executeUpdate();
        }
    }

    public static void replaceTarget(UUID source, UUID target) throws SQLException {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    update minecraft.collection set target=? where target=?
                      """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, target.toString());
            preparedStatement.setString(2, source.toString());
            preparedStatement.executeUpdate();
        }
    }

    public static List<Pair<UUID, Integer>> getCollectionTop() throws SQLException {
        ArrayList<Pair<UUID, Integer>> objects = new ArrayList<>();
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    select target,count(*) count from minecraft.collection group by target order by count desc ;
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                UUID uuid = UUID.fromString(resultSet.getString(1));
                int integer = resultSet.getInt(2);
                objects.add(new Pair<>(uuid, integer));
            }
        }
        return objects;
    }

}
