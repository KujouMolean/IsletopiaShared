package com.molean.isletopia.shared.database;

import com.molean.isletopia.shared.model.Achievement;
import com.molean.isletopia.shared.model.ClubAchievement;
import com.molean.isletopia.shared.model.PlayerAchievement;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class AchievementDao {
    public static void addAchievement(Achievement achievement) throws SQLException {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = "insert into minecraft.achievement(name, display, description, access, score, icon) VALUES (?,?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, achievement.getName());
            preparedStatement.setString(2, achievement.getDisplay());
            preparedStatement.setString(3, achievement.getDescription());
            preparedStatement.setString(4, achievement.getAccess());
            preparedStatement.setInt(5, achievement.getScore());
            preparedStatement.setString(6, achievement.getIcon());
            preparedStatement.executeUpdate();
        }
    }

    public static void updateAchievement(Achievement achievement) throws SQLException {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = "update minecraft.achievement set display=?,description=?,access=?,score=?,icon=? where name=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(6, achievement.getName());
            preparedStatement.setString(1, achievement.getDisplay());
            preparedStatement.setString(2, achievement.getDescription());
            preparedStatement.setString(3, achievement.getAccess());
            preparedStatement.setInt(4, achievement.getScore());
            preparedStatement.setString(5, achievement.getIcon());
            preparedStatement.executeUpdate();
        }
    }

    public static Collection<String> getAchievements() throws SQLException {
        ArrayList<String> strings = new ArrayList<>();

        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = "select name from minecraft.achievement";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                strings.add(name);
            }
        }
        return strings;
    }

    public static Achievement getAchievement(String name) throws SQLException {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = "select name, display, description, access, score, icon from minecraft.achievement where name=?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Achievement achievement = new Achievement();
                achievement.setName(resultSet.getString("name"));
                achievement.setDisplay(resultSet.getString("display"));
                achievement.setDescription(resultSet.getString("description"));
                achievement.setAccess(resultSet.getString("access"));
                achievement.setScore(resultSet.getInt("score"));
                achievement.setIcon(resultSet.getString("icon"));
                return achievement;
            }
        }
        return null;
    }

    public static void removeAchievement(String name) throws SQLException {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = "delete from minecraft.achievement where name=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
        }
    }

    public static void addPlayerAchievement(PlayerAchievement playerAchievement) throws SQLException {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = "insert into minecraft.player_achievement(uuid, achievement,time) VALUES (?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, playerAchievement.getOwner().toString());
            preparedStatement.setString(2, playerAchievement.getAchievement());
            LocalDateTime localDateTime = playerAchievement.getLocalDateTime();
            localDateTime = localDateTime == null ? LocalDateTime.now() : localDateTime;
            preparedStatement.setTimestamp(3, Timestamp.valueOf(localDateTime));
            preparedStatement.executeUpdate();
        }
    }

    public static void removePlayerAchievement(String name, UUID owner) throws SQLException {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = "delete from minecraft.player_achievement where uuid=? and achievement=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, owner.toString());
            preparedStatement.setString(2, name);
            preparedStatement.executeUpdate();
        }
    }

    public static List<PlayerAchievement> getPlayerAchievements(UUID owner) throws SQLException {
        ArrayList<PlayerAchievement> achievementEntries = new ArrayList<>();
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = "select uuid, achievement, time from minecraft.player_achievement where uuid=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, owner.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                PlayerAchievement playerAchievement = new PlayerAchievement();
                playerAchievement.setAchievement(resultSet.getString("achievement"));
                playerAchievement.setOwner(UUID.fromString(resultSet.getString("uuid")));
                playerAchievement.setLocalDateTime(resultSet.getTimestamp("time").toLocalDateTime());
                achievementEntries.add(playerAchievement);
            }
        }
        return achievementEntries;
    }

    public static List<ClubAchievement> getClubAchievements(UUID owner) throws SQLException {
        ArrayList<ClubAchievement> achievementEntries = new ArrayList<>();
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = "select club, achievement, time from minecraft.club_achievement where club=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, owner.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                ClubAchievement playerAchievement = new ClubAchievement();
                playerAchievement.setAchievement(resultSet.getString("achievement"));
                playerAchievement.setClub(resultSet.getString("club"));
                playerAchievement.setLocalDateTime(resultSet.getTimestamp("time").toLocalDateTime());
                achievementEntries.add(playerAchievement);
            }
        }
        return achievementEntries;
    }

    public static int getAchievementScores(UUID owner) throws SQLException {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    select sum(score)
                    from minecraft.achievement
                    where name in (select achievement from minecraft.player_achievement where uuid = ?);
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, owner.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }
        return 0;
    }


}
