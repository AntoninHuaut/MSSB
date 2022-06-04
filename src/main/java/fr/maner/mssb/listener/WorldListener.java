package fr.maner.mssb.listener;

import fr.maner.mssb.game.GameData;
import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WorldListener implements Listener {

    private final GameData gameData;

    public WorldListener(GameData gameData) {
        this.gameData = gameData;

        initWorld();
    }

    private void initWorld() {
        World w = Bukkit.getWorlds().get(0);

        w.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        w.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        w.setGameRule(GameRule.DO_ENTITY_DROPS, false);
        w.setGameRule(GameRule.DO_FIRE_TICK, false);
        w.setGameRule(GameRule.DO_MOB_LOOT, false);
        w.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        w.setGameRule(GameRule.DO_TILE_DROPS, false);
        w.setGameRule(GameRule.MOB_GRIEFING, false);
        w.setGameRule(GameRule.NATURAL_REGENERATION, false);
        w.setGameRule(GameRule.SPECTATORS_GENERATE_CHUNKS, false);

        w.setGameRule(GameRule.KEEP_INVENTORY, true);
        w.setGameRule(GameRule.FORGIVE_DEAD_PLAYERS, true);

        w.setTime(6000L);
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent e) {
        e.setCancelled(e.toWeatherState());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (gameData.getGameConfig().isBuildMode()) return;

        e.setCancelled(true);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (gameData.getGameConfig().isBuildMode()) return;

        e.setCancelled(true);
    }

    @EventHandler
    public void onExplode(BlockExplodeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if (!e.getCause().equals(DamageCause.FALL)) return;

        Location loc = e.getEntity().getLocation();

        boolean hasHayBlock = false;

        for (int x = -1; x < 1 && !hasHayBlock; x++) {
            for (int y = -1; y < 1 & !hasHayBlock; y++) {
                Material matBlock = loc.clone().add(x, -1, y).getBlock().getType();

                if (matBlock.equals(Material.HAY_BLOCK)) {
                    hasHayBlock = true;
                }
            }
        }

        if (hasHayBlock) {
            loc.getWorld().spawnParticle(Particle.BLOCK_CRACK, loc, 150, 0.5, 0.75, 0.5, 0.25, Material.HAY_BLOCK.createBlockData());
            e.setCancelled(true);
        }
    }
}
