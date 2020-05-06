package fr.maner.mssb.utils.specialloc;

import java.util.HashMap;

import javax.annotation.Nullable;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import fr.maner.mssb.utils.JsonConfig;

public class SpecialLocConfig extends JsonConfig {

	private World world;
	private HashMap<SpecialLocEnum, Location> specialLocMap = new HashMap<SpecialLocEnum, Location>();

	public SpecialLocConfig(JavaPlugin pl) {
		super(pl, "specialLoc");
		this.world = pl.getServer().getWorlds().get(0);

		loadSpecialLoc();
	}

	private void loadSpecialLoc() {
		JsonObject object = getJsonObject();

		for (SpecialLocEnum key : SpecialLocEnum.values()) {
			JsonElement element = object.get(key.toString());
			if (!element.isJsonObject())
				return;

			JsonObject locObj = element.getAsJsonObject();
			Location loc = new Location(world, getDouble(locObj, "x"), getDouble(locObj, "y"), getDouble(locObj, "z"),
					getFloat(locObj, "yaw"), getFloat(locObj, "pitch"));
			loc.add(0.5, 0, 0.5);
			specialLocMap.put(key, loc);
		}
	}

	@Nullable
	public Location getLocation(SpecialLocEnum location) {
		return specialLocMap.getOrDefault(location, null);
	}

	public void tpPlayer(Player p, SpecialLocEnum location) {
		Location loc = getLocation(location);

		if (loc == null)
			p.sendMessage("§6» §cUne erreur est survenue lors de votre téléportation");
		else
			p.teleport(loc);
	}
}
