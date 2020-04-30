package fr.maner.mssb.type.game;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import fr.maner.mssb.entity.EntityManager;
import fr.maner.mssb.entity.PlayableEntity;

public class NormalMode extends GameType {
	
	private double regenHealth;
	
	public NormalMode() {
		setRegenHealth(0.1);
	}

	@Override
	public void setPlayerDamage(EntityDamageByEntityEvent e) {
		Player damager = (Player) e.getDamager();
		double totalDamage = e.getFinalDamage();

		PlayableEntity playableClass = EntityManager.getInstance().getPlayableClassPlayer(damager.getUniqueId());

		if (playableClass != null) {
			ItemStack mainHand = damager.getInventory().getItemInMainHand();

			if (mainHand != null && mainHand.isSimilar(playableClass.getMainWeapon()))
				totalDamage += playableClass.getWeaponDamage();
		}

		e.setDamage(totalDamage);
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

}
