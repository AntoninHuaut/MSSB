package fr.maner.mssb.entity.list.playable;

import fr.maner.mssb.factory.ItemFactory;
import fr.maner.mssb.factory.armor.ArmorFactory.ArmorType;
import fr.maner.mssb.factory.armor.LeatherArmorFactory;
import fr.maner.mssb.game.GameData;
import fr.maner.mssb.utils.Heads;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;

import java.util.Arrays;
import java.util.Random;

public class SkeletonEntity extends PlayableEntity {

    private static final String COLOR = "§7";
    private static final String NAME = "Squelette";

    public SkeletonEntity(GameData gameData) {
        super(gameData, COLOR, NAME, Heads.SKELETON);

        setWeapons(new ItemFactory(Material.BOW).setAttackDamage(getWeaponDamage()).setName("&cArc de feu")
                        .addEnchantment(Enchantment.ARROW_INFINITE, 1).addEnchantment(Enchantment.ARROW_KNOCKBACK, 1).setUnbreakable(true).build(),
                new ItemFactory(Material.ARROW).build());

        setArmors(Arrays.asList(new LeatherArmorFactory(ArmorType.LEATHER_BOOTS, Color.fromRGB(159, 173, 199)).build(),
                new LeatherArmorFactory(ArmorType.LEATHER_LEGGINGS, Color.fromRGB(159, 173, 199)).build(),
                new LeatherArmorFactory(ArmorType.LEATHER_CHESTPLATE, Color.fromRGB(159, 173, 199)).build()));
    }

    @Override
    protected double getWeaponDamage() {
        return 1.5;
    }

    private final Random random = new Random();

    @EventHandler
    public void onEntityShootArrow(EntityShootBowEvent e) {
        if (!e.getProjectile().getType().equals(EntityType.ARROW)) return;

        if (random.nextInt(4) == 0) {
            e.getProjectile().setFireTicks(20 * 5);
        }
    }
}
