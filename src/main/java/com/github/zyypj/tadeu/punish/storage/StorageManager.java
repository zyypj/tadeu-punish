package com.github.zyypj.tadeu.punish.storage;

import com.github.zyypj.tadeu.punish.models.PunishmentRecord;
import com.github.zyypj.tadeu.punish.models.PunishType;
import com.github.zyypj.tadeuBooter.api.database.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StorageManager {
    private final DatabaseManager databaseManager;

    public StorageManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public List<PunishmentRecord> loadAllPunishments() throws Exception {
        List<PunishmentRecord> punishments = new ArrayList<>();
        String query = "SELECT id, uuid, ip, reason, proof, ban_timestamp, total_duration, punish_type FROM punishments";
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                PunishmentRecord record = new PunishmentRecord();
                record.setId(resultSet.getLong("id"));
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
        String query = "INSERT INTO punishments (id, uuid, ip, reason, proof, ban_timestamp, total_duration, punish_type) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, record.getId());
            statement.setString(2, record.getUuid());
            statement.setString(3, record.getIp());
            statement.setString(4, record.getReason());
            statement.setString(5, record.getProof());
            statement.setLong(6, record.getBanTimestamp());
            statement.setLong(7, record.getTotalDuration());
            statement.setString(8, record.getPunishType().name());
            statement.executeUpdate();
        }
    }

    public void saveAllPunishments(Collection<PunishmentRecord> records) throws Exception {
        String query = "INSERT INTO punishments (id, uuid, ip, reason, proof, ban_timestamp, total_duration, punish_type) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            for (PunishmentRecord record : records) {
                statement.setLong(1, record.getId());
                statement.setString(2, record.getUuid());
                statement.setString(3, record.getIp());
                statement.setString(4, record.getReason());
                statement.setString(5, record.getProof());
                statement.setLong(6, record.getBanTimestamp());
                statement.setLong(7, record.getTotalDuration());
                statement.setString(8, record.getPunishType().name());
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }
}