package com.github.zyypj.tadeu.punish.storage;

import com.github.zyypj.tadeu.punish.collections.PunishmentCache;
import com.github.zyypj.tadeu.punish.models.PunishmentRecord;
import java.util.List;

public class CacheManager {
    private final PunishmentCache punishmentCache;

    public CacheManager() {
        this.punishmentCache = new PunishmentCache();
    }

    public void loadCache(List<PunishmentRecord> records) {
        punishmentCache.addAll(records);
    }

    public PunishmentRecord getPunishment(String uuid) {
        return punishmentCache.get(uuid).orElse(null);
    }

    public void addPunishment(PunishmentRecord record) {
        punishmentCache.add(record);
    }

    public PunishmentCache getPunishmentCache() {
        return punishmentCache;
    }
}