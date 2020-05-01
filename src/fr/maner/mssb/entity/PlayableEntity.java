package fr.maner.mssb.entity;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
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
	public void teleportOnMap(Player p) {
		if (!(getGameData().getState() instanceof InGameState)) {
			p.sendMessage(" §6» §cErreur sur le TP");
			return;
		}

		MapData mapData = ((InGameState) getGameData().getState()).getMapData();
		//p.teleport(lookAt(mapData.getRandomLoc(), mapData.getLoc()));
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

	/*private Location lookAt(Location loc, Location lookat) {
		loc = loc.clone();

		double dx = lookat.getX() - loc.getX();
		double dy = lookat.getY() - loc.getY();
		double dz = lookat.getZ() - loc.getZ();

		if (dx != 0) {
			if (dx < 0)
				loc.setYaw((float) (1.5 * Math.PI));
			else
				loc.setYaw((float) (0.5 * Math.PI));

			loc.setYaw((float) loc.getYaw() - (float) Math.atan(dz / dx));
		} else if (dz < 0)
			loc.setYaw((float) Math.PI);

		double dxz = Math.sqrt(Math.pow(dx, 2) + Math.pow(dz, 2));

		loc.setPitch((float) -Math.atan(dy / dxz));
		loc.setYaw(-loc.getYaw() * 180f / (float) Math.PI);
		loc.setPitch(loc.getPitch() * 180f / (float) Math.PI);

		return loc;
	}*/
}
