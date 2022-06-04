package fr.maner.mssb.game.data;

import java.util.UUID;

public class IGPlayerData {

    private int kill;
    private int death;

    private UUID lastDamager;
    private long lastDamage;

    public IGPlayerData() {
        this.kill = 0;
        this.death = 0;

        this.lastDamage = 0;
        this.lastDamager = null;
    }

    public void setDamager(UUID damager) {
        this.lastDamager = damager;
        this.lastDamage = System.currentTimeMillis();
    }

    public UUID getLastDamager() {
        return lastDamager;
    }

    public void addDeath() {
        this.death++;
    }

    public void addKill() {
        this.kill++;
    }

    public int getKill() {
        return kill;
    }

    public int getDeath() {
        return death;
    }

    public long getLastDamage() {
        return lastDamage;
    }

}
