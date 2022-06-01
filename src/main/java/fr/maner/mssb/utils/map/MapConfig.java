package fr.maner.mssb.utils.map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fr.maner.mssb.utils.JsonConfig;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class MapConfig extends JsonConfig {

    private final World world;
    private final List<MapData> mapDataList = new ArrayList<>();

    public MapConfig(JavaPlugin pl) {
        super(pl, "map");
        this.world = pl.getServer().getWorlds().get(0);

        loadMaps();
    }

    private void loadMaps() {
        JsonArray array = getJsonArray();

        for (JsonElement element : array) {
            if (!element.isJsonObject()) continue;

            JsonObject mapObj = element.getAsJsonObject();

            JsonElement dataElement = get(mapObj, "data");
            JsonElement locElement = get(mapObj, "loc");
            if (!dataElement.isJsonObject() || !locElement.isJsonObject()) continue;

            JsonObject itemObj = dataElement.getAsJsonObject();
            JsonObject locObj = locElement.getAsJsonObject();

            Location loc = new Location(world,
                    getDouble(locObj, "x"), getDouble(locObj, "y"), getDouble(locObj, "z"),
                    getFloat(locObj, "yaw"), getFloat(locObj, "pitch"));
            loc.add(0.5, 0, 0.5);

            MapData mapData = new MapData(getString(itemObj, "author"), getString(itemObj, "name"),
                    getInt(itemObj, "minY"), getInt(itemObj, "maxY"), getInt(itemObj, "radius"));

            mapData.setItem(getString(itemObj, "material"), getString(itemObj, "opt_SkullData")).setLoc(loc);

            mapDataList.add(mapData);
        }
    }

    public List<MapData> getMapData() {
        return mapDataList;
    }
}
