package fr.maner.mssb.listener;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import fr.maner.mssb.game.GameData;

public class WorldListener implements Listener {

	private GameData gameData;

	public WorldListener(GameData gameData) {
		this.gameData = gameData;
		
		initWorld();
	}

	private void initWorld() {
		World w = Bukkit.getWorlds().get(0);

		w.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
		w.setGameRule(GameRule.DO_FIRE_TICK, false);
		w.setGameRule(GameRule.DO_MOB_SPAWNING, false);
		w.setGameRule(GameRule.MOB_GRIEFING, false);
		w.setGameRule(GameRule.NATURAL_REGENERATION, false);
		w.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);

		w.setGameRule(GameRule.KEEP_INVENTORY, true);
		w.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
		
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
}
