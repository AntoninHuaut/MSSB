package fr.maner.mssb.type.state;

import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import fr.maner.mssb.game.GameData;

public abstract class GameState implements Listener {
	
	private GameData gameData;
	
	public GameState(GameData gameData) {
		this.gameData = gameData;
	}
	
	public GameData getGameData() {
		return gameData;
	}
	
	public boolean hasGameStart() { return this instanceof InGameState; }
	
	public void initPlayer(Player p) {
		p.getInventory().clear();
		p.closeInventory();
		p.setFireTicks(0);
		p.setFallDistance(0F);
		p.setLevel(0);
		p.setExp(0F);
		p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue());
		p.setGameMode(GameMode.SURVIVAL);
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if (getGameData().getGameConfig().isBuildMode()) return;
		if (!(e.getWhoClicked() instanceof Player)) return;
		
		e.setCancelled(true);
	}
	
	public abstract void playerBelowYLimit(Player p);
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		if (gameData.getGameConfig().isBuildMode()) return;

		Player p = e.getPlayer();

		if (p.getLocation().getBlockY() < getMinY())
			this.playerBelowYLimit(p);
	}
	
	public abstract void onPlayerJoin(Player p);
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		if (getGameData().getGameConfig().isBuildMode()) return;
		
		this.onPlayerJoin(e.getPlayer());
	}
	
	public abstract int getMinY();
	public abstract void reset();
}
