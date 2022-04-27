package com.molean.isletopia.shared.database;

import com.molean.isletopia.shared.model.ChargeDetail;
import com.molean.isletopia.shared.database.DataSourceUtils;
import com.molean.isletopia.shared.database.IslandDao;
import com.molean.isletopia.shared.model.Island;
import com.molean.isletopia.shared.model.IslandId;

import java.sql.*;

public class ChargeDao {

    private static int getId(IslandId islandId) throws SQLException {
        Island islandByIslandId = IslandDao.getIslandByIslandId(islandId);
        if (islandByIslandId == null) {
            throw new RuntimeException("no such island");
        }
        return islandByIslandId.getId();
    }

    public static void create(IslandId islandId) throws SQLException {
        create(getId(islandId));
    }

    public static void set(IslandId islandId, ChargeDetail chargeDetail) throws SQLException {
        set(getId(islandId), chargeDetail);
    }

    public static ChargeDetail get(IslandId islandId) throws SQLException {
        return get(getId(islandId));
    }


    public static void create(int island_id) throws SQLException {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    insert into minecraft.charge(island_id) values (?);
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, island_id);
            preparedStatement.executeUpdate();
        }
    }


    public static void set(int island_id, ChargeDetail chargeDetail) throws SQLException {
        if (get(island_id) == null) {
            create(island_id);
        }
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    update minecraft.charge
                    set dispenser=?,
                        redstone=?,
                        piston=?,
                        tnt=?,
                        furnace=?,
                        hopper=?,
                        vehicle=?,
                        water=?,
                        food=?,
                        otherPowerUsage=?,
                        powerChargeTimes=?,
                        foodChargeTimes=?,
                        powerProduceTimes=?,
                        foodProduceTimes=?,
                        onlineMinutes=?,
                        startTime=?,
                        lastCommitTime=?
                    where island_id = ?;
                                        
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, chargeDetail.getDispenser());
            preparedStatement.setLong(2, chargeDetail.getRedstone());
            preparedStatement.setLong(3, chargeDetail.getPiston());
            preparedStatement.setLong(4, chargeDetail.getTnt());
            preparedStatement.setLong(5, chargeDetail.getFurnace());
            preparedStatement.setLong(6, chargeDetail.getHopper());
            preparedStatement.setLong(7, chargeDetail.getVehicle());
            preparedStatement.setLong(8, chargeDetail.getWater());
            preparedStatement.setLong(9, chargeDetail.getFood());
            preparedStatement.setLong(10, chargeDetail.getOtherPowerUsage());
            preparedStatement.setLong(11, chargeDetail.getPowerChargeTimes());
            preparedStatement.setLong(12, chargeDetail.getFoodChargeTimes());
            preparedStatement.setLong(13, chargeDetail.getPowerProduceTimes());
            preparedStatement.setLong(14, chargeDetail.getFoodProduceTimes());
            preparedStatement.setLong(15, chargeDetail.getOnlineMinutes());
            preparedStatement.setTimestamp(16, Timestamp.valueOf(chargeDetail.getStartTime()));
            preparedStatement.setTimestamp(17, Timestamp.valueOf(chargeDetail.getLastCommitTime()));
            preparedStatement.setInt(18, island_id);
            preparedStatement.executeUpdate();
        }
    }


    public static ChargeDetail get(int island_id) throws SQLException {
        ChargeDetail chargeDetail = new ChargeDetail();
        try (Connection connection = DataSourceUtils.getConnectionWithoutCheck()) {
            String sql = """
                    select dispenser,
                           redstone,
                           piston,
                           tnt,
                           furnace,
                           hopper,
                           vehicle,
                           water,
                           food,
                           otherPowerUsage,
                           foodChargeTimes,
                           powerChargeTimes,
                           powerProduceTimes,
                           foodProduceTimes,
                           onlineMinutes,
                           startTime,
                           lastCommitTime
                    from minecraft.charge
                    where island_id = ?;
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, island_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                long dispenser = resultSet.getLong("dispenser");
                long redstone = resultSet.getLong("redstone");
                long piston = resultSet.getLong("piston");
                long tnt = resultSet.getLong("tnt");
                long furnace = resultSet.getLong("furnace");
                long hopper = resultSet.getLong("hopper");
                long vehicle = resultSet.getLong("vehicle");
                long water = resultSet.getLong("water");
                long food = resultSet.getLong("food");
                long otherPowerUsage = resultSet.getLong("otherPowerUsage");
                int powerChargeTimes = resultSet.getInt("powerChargeTimes");
                int foodChargeTimes = resultSet.getInt("foodChargeTimes");
                int powerProduceTimes = resultSet.getInt("powerProduceTimes");
                int foodProduceTimes = resultSet.getInt("foodProduceTimes");
                int onlineMinutes = resultSet.getInt("onlineMinutes");
                Timestamp startTime = resultSet.getTimestamp("startTime");
                Timestamp lastCommitTime = resultSet.getTimestamp("lastCommitTime");
                chargeDetail.setDispenser(dispenser);
                chargeDetail.setRedstone(redstone);
                chargeDetail.setPiston(piston);
                chargeDetail.setTnt(tnt);
                chargeDetail.setFurnace(furnace);
                chargeDetail.setHopper(hopper);
                chargeDetail.setVehicle(vehicle);
                chargeDetail.setWater(water);
                chargeDetail.setFood(food);
                chargeDetail.setOtherPowerUsage(otherPowerUsage);
                chargeDetail.setPowerChargeTimes(powerChargeTimes);
                chargeDetail.setFoodChargeTimes(foodChargeTimes);
                chargeDetail.setPowerProduceTimes(powerProduceTimes);
                chargeDetail.setFoodProduceTimes(foodProduceTimes);
                chargeDetail.setOnlineMinutes(onlineMinutes);
                chargeDetail.setStartTime(startTime.toLocalDateTime());
                chargeDetail.setLastCommitTime(lastCommitTime.toLocalDateTime());
                return chargeDetail;
            } else {
                return null;
            }
        }
    }

}
