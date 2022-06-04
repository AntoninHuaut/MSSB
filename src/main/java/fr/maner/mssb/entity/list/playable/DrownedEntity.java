package fr.maner.mssb.entity.list.playable;

import fr.maner.mssb.entity.EntityManager;
import fr.maner.mssb.factory.ItemFactory;
import fr.maner.mssb.factory.armor.ArmorFactory.ArmorType;
import fr.maner.mssb.factory.armor.LeatherArmorFactory;
import fr.maner.mssb.game.GameData;
import fr.maner.mssb.utils.Heads;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DrownedEntity extends PlayableEntity {

    private static final String COLOR = "§b";
    private static final String NAME = "Drowned";

    public DrownedEntity(GameData gameData) {
        super(gameData, COLOR, NAME, Heads.DROWNED);

        setWeapons(new ItemFactory(Material.TRIDENT).setAttackDamage(getWeaponDamage()).setName("&bTrident")
                .setUnbreakable(true).addEnchantment(Enchantment.LOYALTY, 3).build());
        setArmors(Arrays.asList(new LeatherArmorFactory(ArmorType.LEATHER_BOOTS, Color.fromRGB(29, 166, 255)).build(),
                new LeatherArmorFactory(ArmorType.LEATHER_LEGGINGS, Color.fromRGB(29, 166, 255)).build(),
                new LeatherArmorFactory(ArmorType.LEATHER_CHESTPLATE, Color.fromRGB(29, 166, 255)).build()));
    }

    @Override
    protected double getWeaponDamage() {
        return 2;
    }

    @Override
    public void initEntity() {
        getGameData().getItemEffectRun().addPotionEffect(getMainWeapon(), new PotionEffect(PotionEffectType.SLOW, 20, 0, false, false));
    }

    private final Map<UUID, TridentRunnable> tridentRunMap = new HashMap<>();

    class TridentRunnable implements Runnable {
        private static final long TIME_TRIDENT = 1750;

        private final Player shooter;
        private final Trident trident;
        private final int taskId;
        private final long startTime;

        public TridentRunnable(Plugin plugin, Player shooter, Trident trident) {
            this.shooter = shooter;
            this.trident = trident;
            this.taskId = Bukkit.getScheduler().runTaskTimer(plugin, this, 0L, 1L).getTaskId();
            this.startTime = System.currentTimeMillis();
        }

        @Override
        public void run() {
            if (shooter.getInventory().contains(getMainWeapon())) {
                shooter.getInventory().removeItem(getMainWeapon());
            }

            if (startTime + TIME_TRIDENT < System.currentTimeMillis()) {
                cancel();
                killEntity();

                if (!shooter.getInventory().contains(getMainWeapon()) && getGameData().getState().hasGameStart()) {
                    shooter.getInventory().addItem(getMainWeapon());
                } else if (!getGameData().getState().hasGameStart()) {
                    shooter.getInventory().removeItem(getMainWeapon());
                }
            }
        }

        public void cancel() {
            if (Bukkit.getScheduler().isCurrentlyRunning(taskId)) {
                Bukkit.getScheduler().cancelTask(taskId);
            }
        }

        public void killEntity() {
            if (!trident.isDead()) {
                trident.remove();
            }
        }
    }

    @EventHandler
    public void onTridentLaunch(ProjectileLaunchEvent e) {
        if (e.getEntityType() != EntityType.TRIDENT) return;

        final Trident trident = (Trident) e.getEntity();
        final Player shooter = (Player) e.getEntity().getShooter();
        final UUID shooterUUID = shooter.getUniqueId();

        TridentRunnable tridentRun = tridentRunMap.remove(shooterUUID);
        if (tridentRun != null) {
            tridentRun.cancel();
            tridentRun.killEntity();
        }

        tridentRunMap.put(shooter.getUniqueId(), new TridentRunnable(getGameData().getPlugin(), shooter, trident));
    }

    @EventHandler
    public void onDrownedEntityDie(PlayerDeathEvent e) {
        Player p = e.getEntity();
        PlayableEntity playable = EntityManager.getInstance().getPlayableClassPlayer(p.getUniqueId());

        if (!(playable instanceof DrownedEntity)) return;

        TridentRunnable tridentRun = tridentRunMap.remove(p.getUniqueId());
        if (tridentRun != null) {
            tridentRun.cancel();
        }
    }

    @EventHandler
    public void onTridentCollision(ProjectileHitEvent e) {
        if (!e.getEntity().getType().equals(EntityType.TRIDENT)) return;

        Entity entityHit = e.getHitEntity();

        if (entityHit != null || e.getHitBlock() != null) {
            if (!(e.getEntity().getShooter() instanceof Player)) return;

            e.getEntity().remove();
        }
    }
}