package fr.maner.mssb.entity;

import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.maner.mssb.game.GameData;

public abstract class PlayableEntity extends EntityClass {
	
	private ItemStack mainWeapon;
	private List<ItemStack> armors;

	public PlayableEntity(GameData gameData, String color, String name, String base64) {
		super(gameData, color, name, base64);
	}
	
	
	@Override
	public void initPlayer(Player p) {
		p.setGameMode(GameMode.SURVIVAL);
	}
	
	public abstract double getWeaponDamage();
	
	public void setMainWeapon(ItemStack mainWeapon) {
		this.mainWeapon = mainWeapon;
	}
	
	public ItemStack getMainWeapon() {
		return mainWeapon;
	}
	
	public List<ItemStack> getArmors() {
		return armors;
	}

	public void setArmors(List<ItemStack> armors) {
		this.armors = armors;
	}
}
