package com.molean.isletopia.shared.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

public class PlayTimeStatisticsDao {

    public static void addRecord(UUID player, String server, long start, long end) {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = "insert into minecraft.playtime_statistics(uuid, server, startTimeStamp, endTimeStamp, playtime) VALUES (?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, player.toString());
            preparedStatement.setString(2, server);
            preparedStatement.setLong(3, start);
            preparedStatement.setLong(4, end);
            preparedStatement.setLong(5, end - start);

            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static long getLastPlayTimestamp(UUID uuid) {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = "select startTimeStamp  from minecraft.playtime_statistics where uuid=?\n" +
                    "order by startTimeStamp limit 1";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, uuid.toString());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong(1);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0L;
    }

    public static long getRecentPlayTime(UUID uuid, long start) {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = "select SUM(playtime) from minecraft.playtime_statistics where uuid=? and startTimeStamp>?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setLong(2, start);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong(1);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0L;
    }

    public static long getServerRecentPlayTime(String server, long start) {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = "select SUM(playtime) from minecraft.playtime_statistics where server=? and startTimeStamp>?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, server);
            preparedStatement.setLong(2, start);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong(1);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

}
