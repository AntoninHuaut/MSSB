package fr.maner.mssb.type.game;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public abstract class GameType implements Listener {

	/* Entity is Player */
	public void modifyDamage(EntityDamageEvent e) {}
	public void modifyDamageByEntity(EntityDamageByEntityEvent e) {}
	
	public abstract String getConfigMessage();
	public abstract void callAfterPlayerDamageBy_PlayerProjectile(EntityDamageByEntityEvent e);
	public abstract void regenPlayer(Player p);
	
	public boolean isKBMode() { return this instanceof KBMode; }
	public boolean isNormalMode() { return this instanceof NormalMode; }
	
}
