package fr.maner.mssb.entity.list.playable;

import java.util.Arrays;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.maner.mssb.factory.ArmorFactory.ArmorType;
import fr.maner.mssb.factory.ItemFactory;
import fr.maner.mssb.factory.LeatherArmorFactory;
import fr.maner.mssb.game.GameData;
import fr.maner.mssb.utils.Heads;

public class BlazeEntity extends PlayableEntity {

	private static final String COLOR = "§6";
	private static final String NAME = "Blaze";

	public BlazeEntity(GameData gameData) {
		super(gameData, COLOR, NAME, Heads.BLAZE);

		setMainWeapon(new ItemFactory(Material.BLAZE_POWDER).setAttackDamage(getWeaponDamage()).setName("&cPoudre de feu").addEnchantment(Enchantment.FIRE_ASPECT, 1).build());
		setArmors(Arrays.asList(
				new LeatherArmorFactory(ArmorType.LEATHER_BOOTS, Color.fromRGB(255, 85, 48)).build(),
				new LeatherArmorFactory(ArmorType.LEATHER_LEGGINGS, Color.fromRGB(255, 85, 48)).build(),
				new LeatherArmorFactory(ArmorType.LEATHER_CHESTPLATE, Color.fromRGB(255, 85, 48)).build()
				));
	}

	@Override
	protected double getWeaponDamage() {
		return 5;
	}

	@Override
	public void initEntity() {
		getGameData().getItemEffectRun().addPotionEffect(getMainWeapon(), new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20, 0, false, false));
	}

	private int secondCount = 0;

	@Override
	public void runEverySecond(Player p) {
		secondCount++;

		if (secondCount % 4 == 0) {
			getMainWeapon().addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 1);
			p.getInventory().setItem(0, getMainWeapon());
		}
			
		else if (getMainWeapon().containsEnchantment(Enchantment.FIRE_ASPECT)) {
			getMainWeapon().removeEnchantment(Enchantment.FIRE_ASPECT);
			p.getInventory().setItem(0, getMainWeapon());
		}
	}

	@Override
	public void playableEntityFightEntity(Player damager, Entity victim) {}
}
