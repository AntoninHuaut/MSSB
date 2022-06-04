package fr.maner.mssb.type.game;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public abstract class GameType implements Listener {

    private final String name;

    public GameType(String name) {
        this.name = name;
    }

    /* Entity is Player */
    public void modifyDamage(EntityDamageEvent e) {
    }

    public void modifyDamageByEntity(EntityDamageByEntityEvent e) {
    }

    public void getDamageByPlayerOrProjectile(EntityDamageByEntityEvent e) {

    }

    public abstract String getConfigMessage();

    public abstract void regenPlayer(Player p);

    public boolean isKBMode() {
        return this instanceof KBMode;
    }

    public boolean isNormalMode() {
        return this instanceof NormalMode;
    }

    public String getName() {
        return name;
    }
}
