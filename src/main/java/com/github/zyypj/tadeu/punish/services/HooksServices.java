package com.github.zyypj.tadeu.punish.services;

import com.github.zyypj.tadeu.punish.PunishPlugin;
import com.github.zyypj.tadeu.punish.hooks.generic.GenericListeners;
import com.github.zyypj.tadeu.punish.hooks.legendChat.LegendChatListeners;
import com.github.zyypj.tadeu.punish.hooks.nChat.nChatListeners;
import com.github.zyypj.tadeuBooter.api.minecraft.logger.Debug;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Listener;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class HooksServices {

    private final PunishPlugin plugin;

    public void initializeHooks() {
        Debug.log("", true);
        Debug.log("&aProcurando plugins conhecidos...", true);
        List<Listener> listeners = new java.util.ArrayList<>(Collections.emptyList());
        if (havePlugin("nChat")) {
            Debug.log("&aPlugin nChat encontrado!", true);
            listeners.add(new nChatListeners(plugin));
        }
        if (havePlugin("LegendChat")) {
            Debug.log("&aPlugin LegendChat encontrado!", true);
            listeners.add(new LegendChatListeners(plugin));
        }

        if (!listeners.isEmpty()) {
            registerListeners(listeners);
            return;
        }

        Debug.log("&cNenhum plugin conhecido encontrado!", true);

        registerGenericListener();

        Debug.log("", true);
    }

    private boolean havePlugin(String pluginName) {
        return plugin.getServer().getPluginManager().getPlugin(pluginName) != null;
    }

    private void registerListeners(List<Listener> listeners) {
        for (Listener listener : listeners) {
            plugin.getServer().getPluginManager().registerEvents(listener, plugin);
        }
        Debug.log("&aConexão entre plugins realizada.", true);
        Debug.log("", true);
    }

    private void registerGenericListener() {
        Debug.log("&cUtilizando sistema de chat genérico.", true);
        plugin.getServer().getPluginManager().registerEvents(new GenericListeners(plugin), plugin);
    }
}