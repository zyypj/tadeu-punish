package com.github.zyypj.tadeu.punish.hooks.generic;

import com.github.zyypj.tadeu.punish.PunishPlugin;
import com.github.zyypj.tadeu.punish.models.PunishmentRecord;
import com.github.zyypj.tadeuBooter.api.minecraft.logger.Debug;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@RequiredArgsConstructor
public class GenericListeners implements Listener {

    private final PunishPlugin plugin;

    @EventHandler
    public void onPlayerMessage(AsyncPlayerChatEvent e) {
        PunishmentRecord record = plugin.getPunishServices().getActivePunishment(e.getPlayer());
        if (record == null) return;

        plugin.getMessagesServices().sendPersonalPunishmentMessage(record);
        Debug.log("&e" + e.getPlayer().getName() + " tentou mandar uma mensagem enquanto está mutado.", true);
        e.setCancelled(true);
    }
}
