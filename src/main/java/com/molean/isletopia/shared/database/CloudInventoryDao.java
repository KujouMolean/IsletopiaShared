package com.molean.isletopia.shared.database;

import com.molean.isletopia.shared.model.CloudInventorySlot;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CloudInventoryDao {
    public static List<CloudInventorySlot> getInventorySlotsSnapshot(UUID owner) throws SQLException {
        ArrayList<CloudInventorySlot> cloudInventorySlots = new ArrayList<>();
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    select material,amount from minecraft.cloud_inventory where uuid=?
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, owner.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String material = resultSet.getString("material");
                int amount = resultSet.getInt("amount");
                CloudInventorySlot cloudInventorySlot = new CloudInventorySlot();
                cloudInventorySlot.setAmount(amount);
                cloudInventorySlot.setMaterial(material);
                cloudInventorySlot.setOwner(owner);
                cloudInventorySlots.add(cloudInventorySlot);
            }
        }
        return cloudInventorySlots;
    }

    public static boolean produce(UUID uuid, String material, int amount) {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    update minecraft.cloud_inventory set amount=amount+? where uuid=? and material=?
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, amount);
            preparedStatement.setString(2, uuid.toString());
            preparedStatement.setString(3, material);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean containsSlot(UUID uuid, String material) {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    select * from minecraft.cloud_inventory where uuid=? and material=?
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setString(2, material);
            return preparedStatement.executeQuery().next();
        } catch (SQLException e) {
            return false;
        }
    }

    public static void create(UUID uuid, String material) throws SQLException {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    insert into minecraft.cloud_inventory(uuid, material, amount) values(?,?,0)
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setString(2, material);
            preparedStatement.executeUpdate();
        }
    }

    public static boolean consume(UUID uuid, String material, int amount) {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    update minecraft.cloud_inventory set amount=amount-? where uuid=? and material=?
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, amount);
            preparedStatement.setString(2, uuid.toString());
            preparedStatement.setString(3, material);
            int i = preparedStatement.executeUpdate();
            return i > 0;
        } catch (SQLException e) {
            return false;
        }
    }

}
