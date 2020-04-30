package fr.maner.mssb.inventory.init;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class InvGUIListener implements Listener {

	@EventHandler
	public void onClick(InventoryClickEvent e){
		if (!(e.getWhoClicked() instanceof Player)) return;

		Player player = (Player) e.getWhoClicked();
		UUID playerUUID = player.getUniqueId();

		UUID inventoryUUID = InvGUI.getInventoryOpen().get(playerUUID);

		if (inventoryUUID != null) {
			e.setCancelled(true);
			InvGUI gui = InvGUI.getInventoryList().get(inventoryUUID);
			InvGUI.InvGUIAction action = gui.getActions().get(e.getSlot());

			if (action != null)
				action.click(new InvClickData(e));
		}
	}

	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		InvGUI.getInventoryOpen().remove(e.getPlayer().getUniqueId());
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		InvGUI.getInventoryOpen().remove(e.getPlayer().getUniqueId());
	}
}
