package com.github.zyypj.tadeu.punish;

import com.github.zyypj.tadeu.punish.exceptions.StorageInitializationException;
import com.github.zyypj.tadeu.punish.exceptions.StorageSavingException;
import com.github.zyypj.tadeu.punish.files.MessagesController;
import com.github.zyypj.tadeu.punish.models.PunishmentRecord;
import com.github.zyypj.tadeu.punish.cache.CacheManager;
import com.github.zyypj.tadeu.punish.services.HooksServices;
import com.github.zyypj.tadeu.punish.services.MessagesServices;
import com.github.zyypj.tadeu.punish.services.PunishServices;
import com.github.zyypj.tadeu.punish.storage.StorageManager;
import com.github.zyypj.tadeu.punish.tasks.CacheSaverTask;
import com.github.zyypj.tadeuBooter.api.database.DatabaseManager;
import com.github.zyypj.tadeuBooter.api.database.config.DatabaseConfig;
import com.github.zyypj.tadeuBooter.api.database.json.DatabaseJsonConfigLoader;
import com.github.zyypj.tadeuBooter.api.minecraft.inventories.manager.InventoryManager;
import com.github.zyypj.tadeuBooter.api.minecraft.logger.Debug;
import com.google.common.base.Stopwatch;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;

@Getter
public final class PunishPlugin extends JavaPlugin {

    private File databaseConfigFile;
    private MessagesController messagesController;

    private DatabaseManager databaseManager;
    private StorageManager storageManager;
    private CacheManager cacheManager;

    private MessagesServices messagesServices;
    private PunishServices punishServices;

    @Override
    public void onEnable() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        setupBooter();
        Debug.log("", false);
        Debug.log("&aIniciando Tadeu Punish...", false);

        setupFiles();
        setupStorage();

        setupServices();

        Debug.log("&2&lTadeu Punish&2 iniciado em " + stopwatch.stop() + "!", false);
        Debug.log("", false);
    }

    @Override
    public void onDisable() {
        if (databaseManager != null && storageManager != null) {
            try {
                cacheManager.saveCacheToDatabase(storageManager);
            } catch (Exception e) {
                Debug.log("&cErro ao salvar na database", false);
                throw new StorageSavingException(e);
            }
        }

        Debug.log("", false);
        Debug.log("&e&lTadeu Punish&e desligado com sucesso!", false);
        Debug.log("", false);
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

        databaseConfigFile = new File("database-config.json");
        messagesController = new MessagesController(this);
        saveDefaultConfig();

        Debug.log("&aArquivos carregados em " + stopwatch.stop() + "!", true);
        Debug.log("", true);
    }

    private void setupStorage() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Debug.log("", true);
        Debug.log("&eCarregando armazenamento...", true);

        try {
            DatabaseConfig config = DatabaseJsonConfigLoader.loadConfig(databaseConfigFile);
            databaseManager = new DatabaseManager(config);

            storageManager = new StorageManager(databaseManager);
            cacheManager = new CacheManager();

            List<PunishmentRecord> records = storageManager.loadAllPunishments();
            cacheManager.loadCache(records);

            CacheSaverTask.schedule(this, cacheManager, storageManager, 24000L, 24000L);

        } catch (Exception e) {
            Debug.log("&cErro ao conectar a database", false);
            throw new StorageInitializationException(e);
        }

        Debug.log("&aArmazenamento carregado em " + stopwatch.stop() + "!", true);
        Debug.log("", true);
    }

    private void setupServices() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Debug.log("", true);
        Debug.log("&eCarregando armazenamento...", true);

        messagesServices = new MessagesServices(this);
        punishServices = new PunishServices(this);

        new HooksServices(this).initializeHooks();

        Debug.log("&aArmazenamento carregado em " + stopwatch.stop() + "!", true);
        Debug.log("", true);
    }
}