package com.github.zyypj.tadeu.punish;

import com.github.zyypj.tadeuBooter.api.minecraft.inventories.manager.InventoryManager;
import com.github.zyypj.tadeuBooter.api.minecraft.logger.Debug;
import com.google.common.base.Stopwatch;
import org.bukkit.plugin.java.JavaPlugin;

public final class PunishPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        setupBooter();
        Debug.log("", false);
        Debug.log("&aIniciando Tadeu Punish...", false);

        setupFiles();

        Debug.log("&2&lTadeu Punish&2 iniciado em " + stopwatch.stop() + "!", false);
        Debug.log("", false);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    
    private void setupBooter() {
        Debug.setPlugin(this);
        Debug.setPrefix("§8§l[TadeuPunish-DEBUG] §f");
        InventoryManager.enable(this);
    }

    private void setupFiles() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Debug.log("", true);
        Debug.log("&eCarregando arquivos...", true);

        saveDefaultConfig();

        Debug.log("&aArquivos carregados em " + stopwatch.stop() + "!", true);
        Debug.log("", true);
    }
}
