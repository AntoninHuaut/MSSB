package fr.maner.mssb.entity.list.playable;

import java.util.Arrays;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.maner.mssb.entity.PlayableEntity;
import fr.maner.mssb.factory.ItemFactory;
import fr.maner.mssb.factory.LeatherArmorFactory;
import fr.maner.mssb.factory.LeatherArmorFactory.LeatherArmor;
import fr.maner.mssb.game.GameData;
import fr.maner.mssb.utils.Heads;

public class WolfEntity extends PlayableEntity {
	
	private static final String COLOR = "§f";
	private static final String NAME = "Loup";

	public WolfEntity(GameData gameData) {
		super(gameData, COLOR, NAME, Heads.HOSTILE_WOLF);
		
		setMainWeapon(new ItemFactory(Material.BONE).setName("&eOs de combat").build());
		setArmors(Arrays.asList(
				new LeatherArmorFactory(LeatherArmor.LEATHER_BOOTS, Color.WHITE).build(),
				new LeatherArmorFactory(LeatherArmor.LEATHER_LEGGINGS, Color.WHITE).build(),
				new LeatherArmorFactory(LeatherArmor.LEATHER_CHESTPLATE, Color.WHITE).build()
		));
	}

	@Override
	public double getWeaponDamage() {
		return 6;
	}

	@Override
	public void initEntity() {
		getGameData().getItemEffectRun().addPotionEffect(getMainWeapon(), new PotionEffect(PotionEffectType.SPEED, 30, 0, false, false));
	}

}
