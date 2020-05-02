package fr.maner.mssb.type.game;

import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public abstract class GameType implements Listener {

	public void modifyDamage(EntityDamageEvent e) {}
	
	public abstract String getConfigMessage();
	public abstract void setPlayerDamage(EntityDamageByEntityEvent e);
	
	public boolean isKBMode() { return this instanceof KBMode; }
	public boolean isNormalMode() { return this instanceof NormalMode; }
	
}
