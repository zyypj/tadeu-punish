package com.github.zyypj.tadeu.punish.files;

import com.github.zyypj.tadeuBooter.api.file.YAML;
import org.bukkit.plugin.Plugin;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.IOException;
import java.util.List;

public class MessagesController {
    private final YAML messagesConfig;

    public MessagesController(Plugin plugin) {
        try {
            messagesConfig = new YAML("messages.yml", plugin);
            messagesConfig.saveDefaultConfig();
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException("Falha ao carregar messages.yml", e);
        }
    }

    public String getMessage(String path) {
        return messagesConfig.getString("messages." + path, true);
    }

    public List<String> getListMessage(String path) {
        return messagesConfig.getStringList("messages." + path, true);
    }

    public void reload() {
        messagesConfig.reload();
    }
}
