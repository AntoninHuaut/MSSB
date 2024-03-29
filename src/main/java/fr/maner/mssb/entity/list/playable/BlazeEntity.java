package fr.maner.mssb.entity.list.playable;

import fr.maner.mssb.factory.ItemFactory;
import fr.maner.mssb.factory.armor.ArmorFactory.ArmorType;
import fr.maner.mssb.factory.armor.LeatherArmorFactory;
import fr.maner.mssb.game.GameData;
import fr.maner.mssb.utils.Heads;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Random;

public class BlazeEntity extends PlayableEntity {

    private static final String COLOR = "§6";
    private static final String NAME = "Blaze";
    private static final int FIRE_DURATION_SECOND = 50; // 50 ticks => 2.5s

    public BlazeEntity(GameData gameData) {
        super(gameData, COLOR, NAME, Heads.BLAZE);

        setWeapons(new ItemFactory(Material.BLAZE_POWDER).setAttackDamage(getWeaponDamage()).setName("&cPoudre de feu").build());
        setArmors(Arrays.asList(new LeatherArmorFactory(ArmorType.LEATHER_BOOTS, Color.fromRGB(255, 85, 48)).build(),
                new LeatherArmorFactory(ArmorType.LEATHER_LEGGINGS, Color.fromRGB(255, 85, 48)).build(),
                new LeatherArmorFactory(ArmorType.LEATHER_CHESTPLATE, Color.fromRGB(255, 85, 48)).build()));
    }

    @Override
    protected double getWeaponDamage() {
        return 5;
    }

    private final Random random = new Random();

    @Override
    public void playableEntityFightEntity(Player damager, Entity victim) {
        if (random.nextInt(3) <= 1) {
            victim.setFireTicks(FIRE_DURATION_SECOND);
        }
    }
}
