package com.github.zyypj.tadeu.punish.collections;

import com.github.zyypj.tadeu.punish.models.PunishmentRecord;
import com.github.zyypj.tadeuBooter.api.collections.GenericCache;
import com.github.zyypj.tadeuBooter.api.collections.MappablePair;

public class PunishmentCache extends GenericCache<String, PunishmentRecord, PunishmentRecord> {
    @Override
    public MappablePair<String, PunishmentRecord> apply(PunishmentRecord record) {
        return new MappablePair<>(record.getUuid(), record);
    }
}