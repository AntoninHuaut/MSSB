package fr.maner.mssb.utils.map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import fr.maner.mssb.factory.ItemFactory;
import fr.maner.mssb.factory.SkullFactory;

public class MapData {

	private ItemStack is;
	private Location loc;

	public MapData setItem(String name, String material, String opt_SkullData) {
		Material mat;

		try {
			mat = Material.valueOf(material);
		} catch(NullPointerException | IllegalArgumentException e) {
			is = new ItemFactory(Material.STONE).setName("Chargement échoué").build();
			e.printStackTrace();
			return this;
		}
		
		ItemFactory itemFactory;
		
		if (mat.equals(Material.PLAYER_HEAD) && !opt_SkullData.isEmpty())
			itemFactory = new ItemFactory(SkullFactory.buildFromBase64(opt_SkullData));
		else
			itemFactory = new ItemFactory(mat);
		
		is = itemFactory.setName(name).build();

		return this;
	}

	public MapData setLoc(Location loc) {
		this.loc = loc;
		return this;
	}
	
	public ItemStack getIs() {
		return is;
	}

	public Location getLoc() {
		return loc;
	}
}
