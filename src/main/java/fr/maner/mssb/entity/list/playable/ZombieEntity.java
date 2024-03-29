package fr.maner.mssb.entity.list.playable;

import fr.maner.mssb.factory.ItemFactory;
import fr.maner.mssb.factory.armor.ArmorFactory.ArmorType;
import fr.maner.mssb.factory.armor.LeatherArmorFactory;
import fr.maner.mssb.game.GameData;
import fr.maner.mssb.utils.Heads;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

public class ZombieEntity extends PlayableEntity {

    private static final String COLOR = "§3";
    private static final String NAME = "Zombie";
    private static final int POTION_EFFECT_DURATION_SECOND = 50; // 50 ticks => 2.5s

    public ZombieEntity(GameData gameData) {
        super(gameData, COLOR, NAME, Heads.ZOMBIE);

        setWeapons(new ItemFactory(Material.IRON_SHOVEL).setAttackDamage(getWeaponDamage()).setName("&ePelle du mineur").setUnbreakable(true).addEnchantment(Enchantment.KNOCKBACK, 1).build());
        setArmors(Arrays.asList(
                new LeatherArmorFactory(ArmorType.LEATHER_BOOTS, Color.fromRGB(99, 129, 112)).setBonusArmorProtection(3).build(),
                new LeatherArmorFactory(ArmorType.LEATHER_LEGGINGS, Color.fromRGB(99, 129, 112)).setBonusArmorProtection(2).build(),
                new LeatherArmorFactory(ArmorType.LEATHER_CHESTPLATE, Color.fromRGB(99, 129, 112)).setBonusArmorProtection(2).build()
        ));
    }

    @Override
    protected double getWeaponDamage() {
        return 2.5;
    }

    @Override
    public void playableEntityFightEntity(Player damager, Entity victim) {
        if (!(victim instanceof Player)) return;

        ((Player) victim).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, POTION_EFFECT_DURATION_SECOND, 0));
        damager.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, POTION_EFFECT_DURATION_SECOND, 0));
    }
}
