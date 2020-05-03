package fr.maner.mssb.type.game;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class NormalMode extends GameType {
	
	private double regenHealth;
	
	public NormalMode() {
		setRegenHealth(0.1);
	}
	
	public void setRegenHealth(double regenHealth) {
		if (regenHealth < 0) return;
		this.regenHealth = regenHealth;
	}
	
	public void addRegenHealth(double regenHealth) {
		this.regenHealth += regenHealth;
		if (this.regenHealth < 0) setRegenHealth(0);
	}

	public double getRegenHealth() {
		return regenHealth;
	}

	@Override
	public String getConfigMessage() {
		return String.format("§6Normal §7| §eRégénération pv/sec §a+%.1f§7", getRegenHealth());
	}

	@Override
	public void setPlayerDamage(EntityDamageByEntityEvent e) {}

	@Override
	public void regenPlayer(Player p) {
		p.setHealth(Math.min(p.getHealth() + getRegenHealth(), p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue()));
	}
}
