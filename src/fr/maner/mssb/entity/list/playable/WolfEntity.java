package fr.maner.mssb.entity.list.playable;

import java.util.Arrays;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.maner.mssb.factory.ItemFactory;
import fr.maner.mssb.factory.armor.ArmorFactory.ArmorType;
import fr.maner.mssb.factory.armor.LeatherArmorFactory;
import fr.maner.mssb.game.GameData;
import fr.maner.mssb.utils.Heads;

public class WolfEntity extends PlayableEntity {
	
	private static final String COLOR = "§f";
	private static final String NAME = "Loup";

	public WolfEntity(GameData gameData) {
		super(gameData, COLOR, NAME, Heads.HOSTILE_WOLF);
		
		setWeapons(new ItemFactory(Material.BONE).setAttackDamage(getWeaponDamage()).setName("&eOs de combat").build());
		setArmors(Arrays.asList(
				new LeatherArmorFactory(ArmorType.LEATHER_BOOTS, Color.WHITE).build(),
				new LeatherArmorFactory(ArmorType.LEATHER_LEGGINGS, Color.WHITE).build(),
				new LeatherArmorFactory(ArmorType.LEATHER_CHESTPLATE, Color.WHITE).build()
		));
	}

	@Override
	protected double getWeaponDamage() {
		return 6;
	}

	@Override
	public void initEntity() {
		getGameData().getItemEffectRun().addPotionEffect(getMainWeapon(), new PotionEffect(PotionEffectType.SPEED, 20, 0, false, false));
	}

}
