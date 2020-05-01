package fr.maner.mssb.listener;

import java.util.EnumSet;
import java.util.Set;
import java.util.function.Function;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.ShulkerBox;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Bed;
import org.bukkit.block.data.type.Fence;
import org.bukkit.block.data.type.TrapDoor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.maner.mssb.entity.EntityClass;
import fr.maner.mssb.entity.EntityManager;
import fr.maner.mssb.game.GameData;

public class PlayerListener implements Listener {

	private GameData gameData;

	public PlayerListener(GameData gameData) {
		this.gameData = gameData;
	}

	@EventHandler
	public void onPlayerExpChange(PlayerExpChangeEvent e) {
		if (gameData.getGameConfig().getGameType().isKBMode())
			return;

		e.setAmount(0);
		e.getPlayer().setExp(0F);
		e.getPlayer().setLevel(0);
	}

	@EventHandler
	public void onAsyncChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		EntityClass entityClass = EntityManager.getInstance().getClassPlayer(p.getUniqueId());

		String prefix;
		String colorMessage;

		if (entityClass == null)
			prefix = "§8[§7?§8]";
		else
			prefix = String.format("§8[%s%s§8]", entityClass.getColor(), entityClass.getName());

		if (!gameData.getState().hasGameStart())
			colorMessage = "f";
		else
			colorMessage = entityClass == null || !entityClass.isPlayableClass() ? "7" : "f";

		e.setFormat(String.format("%s §b%s §e» §%s%s", prefix, p.getName(), colorMessage, e.getMessage()));
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		e.setJoinMessage(String.format("§8[§a+§8] %s", e.getPlayer().getName()));
	}

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e) {
		e.setQuitMessage(String.format("§8[§c-§8] %s", e.getPlayer().getName()));
		EntityManager.getInstance().removeClassPlayer(e.getPlayer().getUniqueId());
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onItemsDrop(PlayerDropItemEvent e) {
		if (gameData.getGameConfig().isBuildMode())
			return;

		e.setCancelled(true);
	}

	@EventHandler
	public void onFood(FoodLevelChangeEvent e) {
		e.setCancelled(true);
	}

	private Set<Material> cancelInteract = EnumSet.of(Material.CHEST, Material.TRAPPED_CHEST, Material.FURNACE,
			Material.CRAFTING_TABLE, Material.ANVIL, Material.SMOKER, Material.GRINDSTONE, Material.BARREL,
			Material.HOPPER, Material.DISPENSER, Material.DROPPER, Material.DAYLIGHT_DETECTOR, Material.ENDER_CHEST,
			Material.NOTE_BLOCK, Material.ENDER_CHEST, Material.CRAFTING_TABLE, Material.LOOM,
			Material.CARTOGRAPHY_TABLE, Material.BLAST_FURNACE);
	private final Function<Block, Boolean> CHECK_BAN = block -> {
		BlockData blockData = block.getBlockData();
		return blockData instanceof Bed || blockData instanceof ShulkerBox || blockData instanceof Fence || blockData instanceof TrapDoor;
	};

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (gameData.getGameConfig().isBuildMode())
			return;

		Block b = e.getClickedBlock();

		if (b == null)
			return;

		if (cancelInteract.contains(b.getType())
				|| (e.getAction().equals(Action.PHYSICAL) && b.getType().equals(Material.FARMLAND))
				|| CHECK_BAN.apply(b))
			e.setCancelled(true);
	}

}
