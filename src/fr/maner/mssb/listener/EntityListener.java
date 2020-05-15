package fr.maner.mssb.listener;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import fr.maner.mssb.game.GameData;

public class EntityListener implements Listener {

	private GameData gameData;

	public EntityListener(GameData gameData) {
		this.gameData = gameData;
	}
	
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
	
	@EventHandler
    public void onItemFrameInteraction(PlayerInteractEntityEvent e) {
		if (gameData.getGameConfig().isBuildMode())
			return;
		
        if (e.getRightClicked().getType().equals(EntityType.ITEM_FRAME))
            e.setCancelled(true);
    }
	
	@EventHandler
    public void onItemFrameDamage(EntityDamageEvent e) {
		if (gameData.getGameConfig().isBuildMode())
			return;
		
        if (e.getEntity().getType().equals(EntityType.ITEM_FRAME))
            e.setCancelled(true);
    }
}
