package fr.maner.mssb.entity;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import fr.maner.mssb.game.GameData;
import fr.maner.mssb.type.state.InGameState;
import fr.maner.mssb.utils.map.MapData;

public abstract class PlayableEntity extends EntityClass {

	private ItemStack mainWeapon;
	private List<ItemStack> armors = new ArrayList<ItemStack>();

	public PlayableEntity(GameData gameData, String color, String name, String base64) {
		super(gameData, color, name, base64);
	}

	@Override
	public void initPlayer(Player p) {
		p.setGameMode(GameMode.SURVIVAL);

		p.getInventory().setItem(0, mainWeapon);
		p.getInventory().setArmorContents(armors.toArray(new ItemStack[0]));
	}

	public abstract double getWeaponDamage();

	public abstract void initEntity();
	
	@Override
	public void fallInVoid(EntityDamageEvent e) {
		((Damageable) e.getEntity()).setHealth(0.0D);
		e.setCancelled(true);
	}

	@Override
	public void teleportOnMap(Player p) {
		if (!getGameData().getState().hasGameStart()) {
			p.sendMessage(" §6» §cErreur sur le TP");
			return;
		}

		MapData mapData = ((InGameState) getGameData().getState()).getMapData();
		p.teleport(mapData.getRandomLoc(p));
	}

	public void setMainWeapon(ItemStack mainWeapon) {
		this.mainWeapon = mainWeapon;
	}

	public ItemStack getMainWeapon() {
		return mainWeapon;
	}

	public List<ItemStack> getArmors() {
		return armors;
	}

	public void setArmors(List<ItemStack> armors) {
		this.armors.clear();
		this.armors.addAll(armors);
		this.armors.add(getItemDisplay());
	}
}
