package fr.maner.mssb.listener;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

public class EntityListener implements Listener {

	@EventHandler
	public void onEntityCombust(EntityCombustEvent e) {
		if (e.getEntity() instanceof Player || e.getEntity() instanceof Arrow) return;

		e.setCancelled(true); 
	}

	@EventHandler
	public void onExpTarget(EntityTargetLivingEntityEvent e) {
		if (e.getEntity() instanceof ExperienceOrb)
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
