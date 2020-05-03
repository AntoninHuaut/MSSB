package fr.maner.mssb.entity.list.playable;

import java.util.Arrays;
import java.util.Random;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import fr.maner.mssb.factory.ItemFactory;
import fr.maner.mssb.factory.armor.ArmorFactory.ArmorType;
import fr.maner.mssb.factory.armor.LeatherArmorFactory;
import fr.maner.mssb.game.GameData;
import fr.maner.mssb.utils.Heads;

public class SkeletonEntity extends PlayableEntity {

	private static final String COLOR = "§7";
	private static final String NAME = "Squelette";

	public SkeletonEntity(GameData gameData) {
		super(gameData, COLOR, NAME, Heads.SKELETON);

		setWeapons(new ItemFactory(Material.BOW).setAttackDamage(getWeaponDamage()).setName("&cArc de feu")
				.addEnchantment(Enchantment.ARROW_INFINITE, 1).setUnbreakable(true).build(),
				new ItemFactory(Material.ARROW).build());

		setArmors(Arrays.asList(new LeatherArmorFactory(ArmorType.LEATHER_BOOTS, Color.fromRGB(159, 173, 199)).build(),
				new LeatherArmorFactory(ArmorType.LEATHER_LEGGINGS, Color.fromRGB(159, 173, 199)).build(),
				new LeatherArmorFactory(ArmorType.LEATHER_CHESTPLATE, Color.fromRGB(159, 173, 199)).build()));
	}

	@Override
	protected double getWeaponDamage() {
		return 1.5;
	}
	
	private Random random = new Random();

	@Override
	public Entity playableEntityShootProjectile(Player shooter, Entity projectile) {
		if (random.nextInt(4) == 0)
			projectile.setFireTicks(20 * 5);
		
		return projectile;
	}
}
