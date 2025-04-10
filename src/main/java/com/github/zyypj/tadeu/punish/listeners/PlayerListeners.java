package com.github.zyypj.tadeu.punish.listeners;

import com.github.zyypj.tadeu.punish.PunishPlugin;
import com.github.zyypj.tadeu.punish.models.PunishType;
import com.github.zyypj.tadeu.punish.models.PunishmentRecord;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.List;

@RequiredArgsConstructor
public class PlayerListeners implements Listener {

    private final PunishPlugin plugin;

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent e) {
        PunishmentRecord record = plugin.getPunishServices().getActivePunishment(e.getPlayer());
        if (record == null) return;

        if (!(record.getPunishType() == PunishType.BAN
                || record.getPunishType() == PunishType.TEMPBAN
                || record.getPunishType() == PunishType.IPBAN)) return;

        List<String> messages = plugin.getMessagesController().getListMessage("pessoal-message." + record.getPunishType().name());
        if (messages == null || messages.isEmpty()) {
            e.disallow(PlayerLoginEvent.Result.KICK_BANNED, "Você está banido.");
            return;
        }

        String reason = record.getReason();
        String proof = record.getProof();
        String id = String.valueOf(record.getId());
        String duration = record.getTotalDuration() == 0 ? plugin.getMessagesController().getMessage("permanent") : plugin.getPunishServices().getRemainingPunishmentFancyTime(record).toFancyString();
        String endDay = "";
        if (record.getPunishType() == PunishType.TEMPBAN) {
            endDay = plugin.getPunishServices().getBanExpiryDate(record);
        }

        StringBuilder kickMessage = new StringBuilder();
        for (String line : messages) {
            line = line.replace("{REASON}", reason)
                    .replace("{PROOF}", proof)
                    .replace("{ID}", id)
                    .replace("{DURATION}", duration)
                    .replace("{END-DAY}", endDay);
            kickMessage.append(line).append("\n");
        }

        e.disallow(PlayerLoginEvent.Result.KICK_BANNED, kickMessage.toString().trim());
    }
}