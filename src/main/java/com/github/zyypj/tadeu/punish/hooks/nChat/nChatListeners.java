package com.github.zyypj.tadeu.punish.hooks.nChat;

import com.github.zyypj.tadeu.punish.PunishPlugin;
import com.github.zyypj.tadeu.punish.models.PunishmentRecord;
import com.github.zyypj.tadeuBooter.api.minecraft.logger.Debug;
import com.nickuc.chat.api.events.PrivateMessageEvent;
import com.nickuc.chat.api.events.PublicMessageEvent;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public class nChatListeners implements Listener {

    private final PunishPlugin plugin;

    @EventHandler
    public void onPrivateMessage(PrivateMessageEvent e) {
        PunishmentRecord record = plugin.getPunishServices().getActivePunishment(e.getSender());
        if (record == null) return;

        plugin.getMessagesServices().sendPersonalPunishmentMessage(record);
        Debug.log("&e" + e.getSender().getName() + " tentou mandar uma mensagem enquanto está mutado.", true);
        e.setCancelled(true);
    }

    @EventHandler
    public void onPublicMessage(PublicMessageEvent e) {
        PunishmentRecord record = plugin.getPunishServices().getActivePunishment(e.getSender());
        if (record == null) return;

        plugin.getMessagesServices().sendPersonalPunishmentMessage(record);
        Debug.log("&e" + e.getSender().getName() + " tentou mandar uma mensagem enquanto está mutado.", true);
        e.setCancelled(true);
    }
}
