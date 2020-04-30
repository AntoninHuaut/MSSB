package fr.maner.mssb.inventory.init;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import fr.maner.mssb.game.GameData;

public abstract class InvGUI implements InventoryHolder {

	private static Map<UUID, InvGUI> inventoryList = new HashMap<UUID, InvGUI>();
	private static Map<UUID, UUID> inventoryOpen = new HashMap<UUID, UUID>();
	
	private GameData gameData;

	private UUID uuid;
	private Inventory inventory;
	private HashMap<Integer, InvGUIAction> actions = new HashMap<Integer, InvGUIAction>();

	public InvGUI(int invSize, String invName, GameData gameData) {
		this.inventory = Bukkit.createInventory(this, invSize, invName);
		this.uuid = UUID.randomUUID();
		this.gameData = gameData;

		InvGUI.inventoryList.put(getUUID(), this);
		initContent();
	}
	
	public abstract void initContent();

	public void setItem(int slot, ItemStack stack, InvGUIAction action){
		inventory.setItem(slot, stack);

		if (action != null) actions.put(slot, action);
		else if (actions.containsKey(slot)) actions.remove(slot);
	}

	public void setItem(int slot, ItemStack stack) {
		setItem(slot, stack, null);
	}

	public void open(Player p) {
		p.openInventory(inventory);
		inventoryOpen.put(p.getUniqueId(), getUUID());
	}

	public void delete() {
		Bukkit.getOnlinePlayers().stream().filter(p -> getInventoryOpen().get(p.getUniqueId()).equals(getUUID())).forEach(p -> p.closeInventory());
		inventoryList.remove(getUUID());
	}

	public UUID getUUID() {
		return uuid;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public static Map<UUID, InvGUI> getInventoryList() {
		return inventoryList;
	}

	public static Map<UUID, UUID> getInventoryOpen() {
		return inventoryOpen;
	}

	public Map<Integer, InvGUIAction> getActions() {
		return actions;
	}

	public interface InvGUIAction {
		void click(InvClickData clickData);
	}
	
	public GameData getGameData() {
		return gameData;
	}
}
