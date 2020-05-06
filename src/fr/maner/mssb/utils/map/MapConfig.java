package fr.maner.mssb.utils.map;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import fr.maner.mssb.utils.JsonConfig;

public class MapConfig extends JsonConfig {

	private World world;
	private List<MapData> mapDataList = new ArrayList<MapData>();

	public MapConfig(JavaPlugin pl) {
		super(pl, "map");
		this.world = pl.getServer().getWorlds().get(0);

		loadMaps();
	}

	private void loadMaps() {
		JsonArray array = getJsonArray();

		for (JsonElement element : array) {
			if (!element.isJsonObject())
				return;

			JsonObject mapObj = element.getAsJsonObject();

			JsonElement itemElement = get(mapObj, "item");
			JsonElement locElement = get(mapObj, "loc");
			if (!itemElement.isJsonObject() || !locElement.isJsonObject())
				return;

			JsonObject itemObj = itemElement.getAsJsonObject();
			JsonObject locObj = locElement.getAsJsonObject();

			Location loc = new Location(world, getDouble(locObj, "x"), getDouble(locObj, "y"), getDouble(locObj, "z"),
					getFloat(locObj, "yaw"), getFloat(locObj, "pitch"));
			loc.add(0.5, 0, 0.5);

			MapData mapData = new MapData().setItem(getString(itemObj, "name"), getString(itemObj, "material"),
					getString(itemObj, "opt_SkullData"), getInt(itemObj, "minY"), getInt(itemObj, "maxY"),
					getInt(itemObj, "radius")).setLoc(loc);
			
			mapDataList.add(mapData);
		}
	}

	public List<MapData> getMapData() {
		return mapDataList;
	}
}
