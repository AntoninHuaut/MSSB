package fr.maner.mssb.type.game;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import fr.maner.mssb.entity.EntityManager;
import fr.maner.mssb.entity.PlayableEntity;

public class KBMode extends GameType {
	
	private double kbMultiplier;
	private double regenKb;
	
	public KBMode() {
		setKbMultiplier(1.0);
		setRegenKb(5);
	}
	
	@Override
	public void setPlayerDamage(EntityDamageByEntityEvent e) { 
		Player damager = (Player) e.getDamager();
		double totalDamage = (double) e.getFinalDamage() * getKbMultiplier();

		PlayableEntity playableClass = EntityManager.getInstance().getPlayableClassPlayer(damager.getUniqueId());

		if (playableClass != null) {
			ItemStack mainHand = damager.getInventory().getItemInMainHand();

			if (mainHand != null && mainHand.isSimilar(playableClass.getMainWeapon()))
				totalDamage += (double) playableClass.getWeaponDamage() * getKbMultiplier();
		}
		
		Player victim = (Player) e.getEntity();

		victim.setExp(0F);
		victim.setLevel(victim.getLevel() + (int) Math.ceil(totalDamage));

		double multi = victim.getLevel();
		multi /= 60;

		for (int i = 100; i < victim.getLevel(); i += 100)
			if (i < victim.getLevel() && i >= 100)
				multi *= 1.5;

		victim.setVelocity(damager.getLocation().getDirection().setY(0).normalize().multiply(multi));
	}
	
	@Override
	public void modifyDamage(EntityDamageEvent e) {
		if (e.isCancelled()) return;
		
		Player victim = (Player) e.getEntity();
		
		double damageGet = (double) e.getFinalDamage() * getKbMultiplier();
		int damageInt = (int) Math.ceil(damageGet);
		
		victim.setExp(0F);
		victim.setLevel(victim.getLevel() + damageInt);

		e.setDamage(0.0);
	}
	
	public void setKbMultiplier(double kbMultiplier) {
		if (kbMultiplier < 0.5) return;
		this.kbMultiplier = kbMultiplier;
	}
	
	public void addKbMultiplier(double kbMultiplier) {
		this.kbMultiplier += kbMultiplier;
		if (this.kbMultiplier < 0.5) setKbMultiplier(0.5);
	}
	
	public void setRegenKb(double regenKb) {
		if (regenKb < 0) return;
		this.regenKb = regenKb;
	}
	
	public void addRegenKb(double regenKb) {
		this.regenKb += regenKb;
		if (this.regenKb < 0) setRegenKb(0);
	}
	
	public double getKbMultiplier() {
		return kbMultiplier;
	}
	
	public double getRegenKb() {
		return regenKb;
	}
}
