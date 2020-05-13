package fr.maner.mssb.type.game;

import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class KBMode extends GameType {

	private double kbMultiplier;
	private int regenKb;

	public KBMode() {
		super("Knockback");
		setKbMultiplier(4.5);
		setRegenKb(5);
	}

	/*
	 * KBMode => https://docs.google.com/spreadsheets/d/
	 * 1TE4CJOGk0nWGjtyo6Eb5DUJozxaGZXoqsjmYE3z3KMI/
	 */
	@Override
	public void callAfterPlayerDamageBy_PlayerProjectile(EntityDamageByEntityEvent e) {
		Entity damager = e.getDamager();
		double totalDamage = (double) e.getFinalDamage() * getKbMultiplier();

		Player victim = (Player) e.getEntity();

		victim.setLevel(victim.getLevel() + (int) Math.ceil(totalDamage));

		final int maxLevelMulti = Math.min(1133, victim.getLevel());
		final int maxLevelKB = Math.min(2000, victim.getLevel());

		double multi = 2 * Math.exp(maxLevelMulti * 0.0075) - 1;
		double yMulti = 2 * Math.exp(maxLevelKB * 0.00075) - 2;

		victim.setVelocity(damager.getLocation().getDirection().setY(0).normalize().multiply(multi).setY(yMulti));
	}

	private List<DamageCause> entityIgnore = Arrays.asList(DamageCause.ENTITY_ATTACK, DamageCause.ENTITY_EXPLOSION, DamageCause.ENTITY_SWEEP_ATTACK);

	@Override
	public void modifyDamageByEntity(EntityDamageByEntityEvent e) {
		if (e.isCancelled())
			return;

		setKBPlayer((Player) e.getEntity(), e.getFinalDamage());
		e.setDamage(0.0);
	}

	@Override
	public void modifyDamage(EntityDamageEvent e) {
		if (e.isCancelled())
			return;

		if (entityIgnore.contains(e.getCause()))
			return;

		setKBPlayer((Player) e.getEntity(), e.getFinalDamage());
		e.setDamage(0.0);
	}

	private void setKBPlayer(Player victim, double finalDamage) {
		double damageGet = (double) finalDamage * getKbMultiplier();
		int damageInt = (int) Math.ceil(damageGet);

		victim.setExp(0F);
		victim.setLevel(victim.getLevel() + damageInt);
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
		return String.format("§6%s §7| §eMultiplicateur §6×%.1f§7 §7| §eRéduction KB/sec §c-%d§7", getName(), getKbMultiplier(), getRegenKb());
	}

	@Override
	public void regenPlayer(Player p) {
		p.setLevel(Math.max(0, p.getLevel() - getRegenKb()));
	}
}
