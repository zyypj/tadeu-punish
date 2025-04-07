package com.github.zyypj.tadeu.punish.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PunishmentRecord {
    private long id;
    private String uuid;
    private String ip;
    private String reason;
    private String proof;
    private long banTimestamp;
    private long totalDuration;
    private PunishType punishType;
}