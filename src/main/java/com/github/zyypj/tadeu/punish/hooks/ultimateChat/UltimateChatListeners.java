package com.github.zyypj.tadeu.punish.hooks.ultimateChat;

import br.net.fabiozumbi12.UltimateChat.Bukkit.API.SendChannelMessageEvent;
import com.github.zyypj.tadeu.punish.PunishPlugin;
import com.github.zyypj.tadeu.punish.models.PunishmentRecord;
import com.github.zyypj.tadeuBooter.api.minecraft.logger.Debug;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public class UltimateChatListeners implements Listener {

    private final PunishPlugin plugin;

    @EventHandler
    public void onSendChannelMessage(SendChannelMessageEvent e) {
        PunishmentRecord record = plugin.getPunishServices().getActivePunishment((Player) e.getSender());
        if (record == null) return;

        plugin.getMessagesServices().sendPersonalPunishmentMessage(record);
        Debug.log("&e" + e.getSender().getName() + " tentou mandar uma mensagem enquanto está mutado.", true);
        e.setCancelled(true);
    }
}
