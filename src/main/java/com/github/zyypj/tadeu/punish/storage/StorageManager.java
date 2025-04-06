package com.github.zyypj.tadeu.punish.storage;

import com.github.zyypj.tadeu.punish.models.PunishmentRecord;
import com.github.zyypj.tadeu.punish.models.PunishType;
import com.github.zyypj.tadeuBooter.api.database.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class StorageManager {
    private final DatabaseManager databaseManager;

    public StorageManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public List<PunishmentRecord> loadAllPunishments() throws Exception {
        List<PunishmentRecord> punishments = new ArrayList<>();
        String query = "SELECT uuid, ip, reason, proof, ban_timestamp, total_duration, punish_type FROM punishments";
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                PunishmentRecord record = new PunishmentRecord();
                record.setUuid(resultSet.getString("uuid"));
                record.setIp(resultSet.getString("ip"));
                record.setReason(resultSet.getString("reason"));
                record.setProof(resultSet.getString("proof"));
                record.setBanTimestamp(resultSet.getLong("ban_timestamp"));
                record.setTotalDuration(resultSet.getLong("total_duration"));
                record.setPunishType(PunishType.valueOf(resultSet.getString("punish_type")));
                punishments.add(record);
            }
        }
        return punishments;
    }

    public void savePunishment(PunishmentRecord record) throws Exception {
        String query = "INSERT INTO punishments (uuid, ip, reason, proof, ban_timestamp, total_duration, punish_type) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, record.getUuid());
            statement.setString(2, record.getIp());
            statement.setString(3, record.getReason());
            statement.setString(4, record.getProof());
            statement.setLong(5, record.getBanTimestamp());
            statement.setLong(6, record.getTotalDuration());
            statement.setString(7, record.getPunishType().name());
            statement.executeUpdate();
        }
    }
}