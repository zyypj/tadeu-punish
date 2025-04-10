package com.github.zyypj.tadeu.punish.services;

import com.github.zyypj.tadeu.punish.PunishPlugin;
import com.github.zyypj.tadeu.punish.models.PunishmentRecord;
import com.github.zyypj.tadeu.punish.models.PunishType;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.List;

@RequiredArgsConstructor
public class MessagesServices {

    private final PunishPlugin plugin;

    public void broadcastPunishment(PunishmentRecord record) {
        PunishType punishType = record.getPunishType();

        if (!plugin.getConfig().getBoolean("broadcast." + punishType.name(), true)) {
            sendPersonalPunishmentMessage(record);
            return;
        }

        Player punishedPlayer = plugin.getServer().getPlayer(record.getUuid());
        String punishedPlayerName = (punishedPlayer != null) ? punishedPlayer.getName() : record.getUuid();

        String messageKey = "broadcast." + punishType.name();
        List<String> messages = plugin.getMessagesController().getListMessage(messageKey);
        if (messages.isEmpty()) {
            return;
        }

        String reason = record.getReason();
        String duration = record.getTotalDuration() == 0 ? plugin.getMessagesController().getMessage("permanent") : plugin.getPunishServices().getRemainingPunishmentFancyTime(record).toFancyString();
        String proof = record.getProof();
        String id = String.valueOf(record.getId());
        String endDay = "";

        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (player.getName().equalsIgnoreCase(punishedPlayerName)) {
                sendPersonalPunishmentMessage(record);
                continue;
            }
            for (String line : messages) {
                String formattedLine = line
                        .replace("{PLAYER-NAME}", punishedPlayerName)
                        .replace("{REASON}", reason)
                        .replace("{DURATION}", duration)
                        .replace("{PROOF}", proof)
                        .replace("{ID}", id)
                        .replace("{END-DAY}", endDay);
                player.sendMessage(formattedLine);
            }
        }
    }

    public void sendPersonalPunishmentMessage(PunishmentRecord record) {
        PunishType punishType = record.getPunishType();
        String messageKey = "pessoal-message." + punishType.name();
        List<String> messages = plugin.getMessagesController().getListMessage(messageKey);
        if (messages.isEmpty()) {
            return;
        }

        Player punishedPlayer = plugin.getServer().getPlayer(record.getUuid());
        if (punishedPlayer == null) {
            return;
        }

        String playerName = punishedPlayer.getName();
        String reason = record.getReason();
        String proof = record.getProof();
        String id = String.valueOf(record.getId());
        String duration = record.getTotalDuration() == 0 ? plugin.getMessagesController().getMessage("permanent") : plugin.getPunishServices().getRemainingPunishmentFancyTime(record).toFancyString();
        String endDay = "";
        if (punishType == PunishType.TEMPBAN || punishType == PunishType.TEMPMUTE) {
            endDay = plugin.getPunishServices().getBanExpiryDate(record);
        }

        for (String line : messages) {
            String formattedLine = line
                    .replace("{PLAYER-NAME}", playerName)
                    .replace("{REASON}", reason)
                    .replace("{DURATION}", duration)
                    .replace("{PROOF}", proof)
                    .replace("{ID}", id)
                    .replace("{END-DAY}", endDay);
            punishedPlayer.sendMessage(formattedLine);
        }
    }

    public List<String> getPersonalPunishmentMessage(PunishmentRecord record) {
        PunishType punishType = record.getPunishType();
        String messageKey = "pessoal-message." + punishType.name();
        List<String> messages = plugin.getMessagesController().getListMessage(messageKey);
        if (messages.isEmpty()) {
            return null;
        }

        Player punishedPlayer = plugin.getServer().getPlayer(record.getUuid());
        if (punishedPlayer == null) {
            return null;
        }

        return messages;
    }
}