package fr.maner.mssb.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

public class EntityListener implements Listener {

	@EventHandler
	public void onEntityCombust(EntityCombustEvent e) {
		if (!(e.getEntity() instanceof org.bukkit.entity.Player))
			e.setCancelled(true); 
	}

	@EventHandler
	public void onExpTarget(EntityTargetLivingEntityEvent e) {
		if (e.getEntity() instanceof org.bukkit.entity.ExperienceOrb)
			e.getEntity().remove(); 
	}

	@EventHandler
	public void onItemsDropEntity(EntityDeathEvent e) {
		e.getDrops().clear();
	}

	@EventHandler
	public void onEntityExplode(EntityExplodeEvent e) {
		e.blockList().clear();
	}
}
