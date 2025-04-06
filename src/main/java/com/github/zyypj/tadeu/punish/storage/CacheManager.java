package com.github.zyypj.tadeu.punish.storage;

import com.github.zyypj.tadeu.punish.models.PunishmentRecord;
import lombok.Getter;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class CacheManager {
    private final Map<String, PunishmentRecord> uuidIndex;
    private final Map<Long, PunishmentRecord> idIndex;
    private long lastId;

    public CacheManager() {
        this.uuidIndex = new HashMap<>();
        this.idIndex = new HashMap<>();
        this.lastId = 0;
    }

    public void loadCache(List<PunishmentRecord> records) {
        for (PunishmentRecord record : records) {
            addPunishment(record);
        }
    }

    public PunishmentRecord getPunishmentByUuid(String uuid) {
        return uuidIndex.get(uuid);
    }

    public PunishmentRecord getPunishmentById(long id) {
        return idIndex.get(id);
    }

    public void addPunishment(PunishmentRecord record) {
        if (record.getId() == 0) {
            record.setId(++lastId);
        } else if (record.getId() > lastId) {
            lastId = record.getId();
        }
        uuidIndex.put(record.getUuid(), record);
        idIndex.put(record.getId(), record);
    }

    public Collection<PunishmentRecord> getAllPunishments() {
        return idIndex.values();
    }

    public void saveCacheToDatabase(StorageManager storageManager) throws Exception {
        storageManager.saveAllPunishments(getAllPunishments());
    }
}