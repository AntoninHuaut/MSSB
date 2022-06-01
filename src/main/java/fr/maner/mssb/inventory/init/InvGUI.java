package fr.maner.mssb.inventory.init;

import fr.maner.mssb.game.GameData;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class InvGUI implements InventoryHolder {

    private static final Map<UUID, InvGUI> inventoryList = new HashMap<>();
    private static final Map<UUID, UUID> inventoryOpen = new HashMap<>();

    private final GameData gameData;

    private final UUID uuid;
    private final Inventory inventory;
    private final HashMap<Integer, InvGUIAction> actions = new HashMap<>();

    public InvGUI(int invSize, String invName, GameData gameData) {
        this.inventory = Bukkit.createInventory(this, invSize, invName);
        this.uuid = UUID.randomUUID();
        this.gameData = gameData;

        InvGUI.inventoryList.put(getUUID(), this);
        initContent();
    }

    public abstract void initContent();

    public void setItem(int slot, ItemStack stack, InvGUIAction action) {
        inventory.setItem(slot, stack);

        if (action != null) actions.put(slot, action);
        else actions.remove(slot);
    }

    public void setItem(int slot, ItemStack stack) {
        setItem(slot, stack, null);
    }

    public void open(Player p) {
        p.openInventory(inventory);
        inventoryOpen.put(p.getUniqueId(), getUUID());
    }

    public void delete() {
        Bukkit.getOnlinePlayers().stream()
                .filter(p -> getInventoryOpen().get(p.getUniqueId()).equals(getUUID()))
                .forEach(HumanEntity::closeInventory);
        inventoryList.remove(getUUID());
    }

    public UUID getUUID() {
        return uuid;
    }

    public @NotNull Inventory getInventory() {
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
