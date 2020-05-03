package fr.maner.mssb.entity.list.playable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import fr.maner.mssb.entity.EntityClass;
import fr.maner.mssb.game.GameData;
import fr.maner.mssb.type.state.InGameState;
import fr.maner.mssb.utils.map.MapData;

public abstract class PlayableEntity extends EntityClass {

	private List<ItemStack> weapons;
	private List<ItemStack> armors = new ArrayList<ItemStack>();

	public PlayableEntity(GameData gameData, String color, String name, String base64) {
		super(gameData, color, name, base64);
	}

	@Override
	public EntityClass initPlayer(Player p) {
		p.setGameMode(GameMode.SURVIVAL);

		for (int i = 0; i < weapons.size(); i++)
			p.getInventory().setItem(i, weapons.get(i));

		p.getInventory().setArmorContents(armors.toArray(new ItemStack[0]));

		return this;
	}
	
	public void initEntity() {};
	public void playableEntityFightEntity(Player damager, Entity victim) {};
	public Entity playableEntityShootProjectile(Player shooter, Entity projectile) { return null; };

	protected abstract double getWeaponDamage();

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

	public void setWeapons(ItemStack... weapons) {
		this.weapons = Arrays.asList(weapons);
	}

	public ItemStack getMainWeapon() {
		return weapons.get(0);
	}

	public List<ItemStack> getArmors() {
		return armors;
	}

	public void setArmors(List<ItemStack> armors) {
		this.armors.clear();
		this.armors.addAll(armors);
		this.armors.add(getPlayerHead());
	}
}
