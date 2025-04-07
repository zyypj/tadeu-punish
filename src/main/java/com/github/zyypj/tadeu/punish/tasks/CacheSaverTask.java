package com.github.zyypj.tadeu.punish.tasks;

import com.github.zyypj.tadeu.punish.exceptions.StorageSavingException;
import com.github.zyypj.tadeu.punish.storage.CacheManager;
import com.github.zyypj.tadeu.punish.storage.StorageManager;
import com.github.zyypj.tadeuBooter.api.minecraft.logger.Debug;
import org.bukkit.plugin.java.JavaPlugin;

public class CacheSaverTask implements Runnable {

    private final JavaPlugin plugin;
    private final CacheManager cacheManager;
    private final StorageManager storageManager;

    public CacheSaverTask(JavaPlugin plugin, CacheManager cacheManager, StorageManager storageManager) {
        this.plugin = plugin;
        this.cacheManager = cacheManager;
        this.storageManager = storageManager;
    }

    @Override
    public void run() {
        try {
            cacheManager.saveCacheToDatabase(storageManager);
            Debug.log("&aCache salvo na database com sucesso.", true);
        } catch (Exception e) {
            Debug.log("&cErro ao salvar na database", false);
            throw new StorageSavingException(e);
        }
    }

    public static void schedule(JavaPlugin plugin, CacheManager cacheManager, StorageManager storageManager, long delayTicks, long periodTicks) {
        plugin.getServer().getScheduler().runTaskTimer(plugin, new CacheSaverTask(plugin, cacheManager, storageManager), delayTicks, periodTicks);
    }
}