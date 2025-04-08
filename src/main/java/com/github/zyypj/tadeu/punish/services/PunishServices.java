package com.github.zyypj.tadeu.punish.services;

import com.github.zyypj.tadeu.punish.PunishPlugin;
import com.github.zyypj.tadeu.punish.models.PunishmentRecord;
import com.github.zyypj.tadeu.punish.cache.CacheManager;
import com.github.zyypj.tadeu.punish.storage.StorageManager;
import com.github.zyypj.tadeuBooter.api.minecraft.lang.text.utils.FancyTime;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class PunishServices {

    private final PunishPlugin plugin;
    private final StorageManager storageManager;
    private final CacheManager cacheManager;

    public PunishServices(PunishPlugin plugin) {
        this.plugin = plugin;
        this.storageManager = plugin.getStorageManager();
        this.cacheManager = plugin.getCacheManager();
    }

    public void applyPunishment(PunishmentRecord record) throws Exception {
        plugin.getMessagesServices().broadcastPunishment(record);
        cacheManager.addPunishment(record);
        storageManager.savePunishment(record);
    }

    public boolean isPlayerPunished(Player player) {
        return isPlayerPunished(player.getUniqueId().toString());
    }

    public boolean isPlayerPunished(String uuid) {
        PunishmentRecord record = cacheManager.getPunishmentByUuid(uuid);
        return record != null && isPunishmentActive(record);
    }

    public PunishmentRecord getActivePunishment(Player player) {
        return getActivePunishment(player.getUniqueId().toString());
    }

    public PunishmentRecord getActivePunishment(String uuid) {
        PunishmentRecord record = cacheManager.getPunishmentByUuid(uuid);
        if (record == null) {
            return null;
        }

        if (record.getTotalDuration() == 0) {
            return record;
        }

        long remainingTime = getRemainingPunishmentTime(record);
        if (remainingTime <= 0) {
            removePunishment(uuid);
            return null;
        }
        return record;
    }


    public List<PunishmentRecord> getPunishmentHistory(Player player) throws Exception {
        return getPunishmentHistory(player.getUniqueId().toString());
    }

    public List<PunishmentRecord> getPunishmentHistory(String uuid) throws Exception {
        List<PunishmentRecord> allPunishments = storageManager.loadAllPunishments();
        return allPunishments.stream()
                .filter(record -> record.getUuid().equals(uuid))
                .collect(Collectors.toList());
    }

    public void removePunishment(Player player) {
        removePunishment(player.getUniqueId().toString());
    }

    public void removePunishment(String uuid) {
        PunishmentRecord record = cacheManager.getPunishmentByUuid(uuid);
        if (record != null) {
            cacheManager.getUuidIndex().remove(uuid);
            cacheManager.getIdIndex().remove(record.getId());
        }
    }

    private boolean isPunishmentActive(PunishmentRecord record) {
        if (record.getTotalDuration() == 0) {
            return true;
        }
        long expiryTime = record.getBanTimestamp() + record.getTotalDuration();
        return System.currentTimeMillis() < expiryTime;
    }

    public long getRemainingPunishmentTime(PunishmentRecord record) {
        if (record.getTotalDuration() == 0) {
            return 0;
        }
        long expiryTime = record.getBanTimestamp() + record.getTotalDuration();
        long remaining = expiryTime - System.currentTimeMillis();
        return remaining > 0 ? remaining : 0;
    }

    public FancyTime getRemainingPunishmentFancyTime(PunishmentRecord record) {
        long remainingMillis = getRemainingPunishmentTime(record);
        if (remainingMillis <= 0) {
            return new FancyTime("0seg");
        }
        long totalSeconds = remainingMillis / 1000;

        int years = (int) (totalSeconds / (365L * 24 * 3600));
        totalSeconds %= (365L * 24 * 3600);
        int months = (int) (totalSeconds / (30L * 24 * 3600));
        totalSeconds %= (30L * 24 * 3600);
        int weeks = (int) (totalSeconds / (7L * 24 * 3600));
        totalSeconds %= (7L * 24 * 3600);
        int days = (int) (totalSeconds / (24L * 3600));
        totalSeconds %= (24L * 3600);
        int hours = (int) (totalSeconds / 3600);
        totalSeconds %= 3600;
        int minutes = (int) (totalSeconds / 60);
        int seconds = (int) (totalSeconds % 60);

        StringBuilder sb = new StringBuilder();
        if (years > 0) sb.append(years).append("a ");
        if (months > 0) sb.append(months).append("m ");
        if (weeks > 0) sb.append(weeks).append("s ");
        if (days > 0) sb.append(days).append("d ");
        if (hours > 0) sb.append(hours).append("h ");
        if (minutes > 0) sb.append(minutes).append("min ");
        if (seconds > 0) sb.append(seconds).append("seg");

        String fancyTimeString = sb.toString().trim();
        return new FancyTime(fancyTimeString);
    }


    public String getBanExpiryDate(PunishmentRecord record) {
        if (record.getTotalDuration() == 0) {
            return "Permanente";
        }
        long expiryTime = record.getBanTimestamp() + record.getTotalDuration();
        Date expiryDate = new Date(expiryTime);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy 'às' HH:mm");
        return sdf.format(expiryDate);
    }
}