package fr.maner.mssb.type.game;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class KBMode extends GameType {

	private double kbMultiplier;
	private int regenKb;

	public KBMode() {
		setKbMultiplier(4.5);
		setRegenKb(5);
	}

	/* KBMode => https://docs.google.com/spreadsheets/d/1TE4CJOGk0nWGjtyo6Eb5DUJozxaGZXoqsjmYE3z3KMI/ */
	@Override
	public void setPlayerDamage(EntityDamageByEntityEvent e) {
		Player damager = (Player) e.getDamager();
		double totalDamage = (double) e.getFinalDamage() * getKbMultiplier();

		Player victim = (Player) e.getEntity();

		victim.setLevel(victim.getLevel() + (int) Math.ceil(totalDamage));

		double multi = 2 * Math.exp(victim.getLevel() * 0.0075) - 1;

		victim.setVelocity(damager.getLocation().getDirection().setY(0).normalize().multiply(multi));
	}

	@Override
	public void modifyDamage(EntityDamageEvent e) {
		if (e.isCancelled())
			return;

		Player victim = (Player) e.getEntity();

		double damageGet = (double) e.getFinalDamage() * getKbMultiplier();
		int damageInt = (int) Math.ceil(damageGet);

		victim.setExp(0F);
		victim.setLevel(victim.getLevel() + damageInt);

		e.setDamage(0.0);
	}

	public void setKbMultiplier(double kbMultiplier) {
		if (kbMultiplier < 0.5)
			return;
		this.kbMultiplier = kbMultiplier;
	}

	public void addKbMultiplier(double kbMultiplier) {
		this.kbMultiplier += kbMultiplier;
		if (this.kbMultiplier < 0.5)
			setKbMultiplier(0.5);
	}

	public void setRegenKb(int regenKb) {
		if (regenKb < 0)
			return;
		this.regenKb = regenKb;
	}

	public void addRegenKb(int regenKb) {
		this.regenKb += regenKb;
		if (this.regenKb < 0)
			setRegenKb(0);
	}

	public double getKbMultiplier() {
		return kbMultiplier;
	}

	public int getRegenKb() {
		return regenKb;
	}

	@Override
	public String getConfigMessage() {
		return String.format("§6Knockback §7| §eMultiplicateur §6×%.1f§7 §7| §eRéduction KB/sec §c-%d§7", getKbMultiplier(), getRegenKb());
	}

	@Override
	public void regenPlayer(Player p) {
		p.setLevel(Math.max(0,  p.getLevel() - getRegenKb()));
	}
}
