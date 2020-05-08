package fr.maner.mssb.utils.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.maner.mssb.factory.ItemFactory;
import fr.maner.mssb.factory.item.SkullFactory;

public class MapData {

	private Random random = new Random();

	private String name;
	private String author;
	private ItemStack is;
	private Location loc;

	private int minY;
	private int maxY;
	private int radius;
	
	public MapData(String author, String name, int minY, int maxY, int radius) {
		this.author = author;
		this.name = name;
		this.minY = minY;
		this.maxY = maxY;
		this.radius = radius;
	}

	public MapData setItem(String material, String opt_SkullData) {
		Material mat;

		try {
			mat = Material.valueOf(material);
		} catch (NullPointerException | IllegalArgumentException e) {
			is = new ItemFactory(Material.STONE).setName("Chargement échoué").build();
			e.printStackTrace();
			return this;
		}

		ItemFactory itemFactory;

		if (mat.equals(Material.PLAYER_HEAD) && !opt_SkullData.isEmpty())
			itemFactory = new ItemFactory(SkullFactory.buildFromBase64(opt_SkullData));
		else
			itemFactory = new ItemFactory(mat);

		is = itemFactory.setName(name).addLore("§7§oRéalisé par " + author).build();

		return this;
	}

	public Location getRandomLoc(Player p) {
		int xHight = getLoc().getBlockX() + radius;
		int xLow = getLoc().getBlockX() - radius;

		int zHight = getLoc().getBlockZ() + radius;
		int zLow = getLoc().getBlockZ() - radius;

		Location locTp = null;

		while (locTp == null)
			locTp = getLocation(xHight, xLow, zHight, zLow);

		locTp.add(0.5, -0.9, 0.5);
		locTp.setPitch(p.getLocation().getPitch());
		locTp.setYaw(p.getLocation().getYaw());
		return locTp;
	}

	public Location getLocation(int xHight, int xLow, int zHight, int zLow) {
		int xGet = random.nextInt(xHight - xLow) + xLow;
		int zGet = random.nextInt(zHight - zLow) + zLow;
		int tempMaxY = getLoc().getWorld().getHighestBlockYAt(xGet, zGet);

		tempMaxY = Math.max(tempMaxY, maxY);

		List<Location> yListLoc = new ArrayList<Location>();

		for (int testY = minY; testY < tempMaxY; testY++) {
			Location testLoc = new Location(getLoc().getWorld(), xGet, testY, zGet);

			if (checkBlock(testLoc))
				yListLoc.add(testLoc);
		}

		if (yListLoc.isEmpty())
			return null;

		return yListLoc.get(random.nextInt(yListLoc.size()));
	}

	public boolean checkBlock(Location loc) {
		World world = loc.getWorld();

		Block legs = world.getBlockAt(loc);
		Block below = world.getBlockAt(loc.add(0, -1, 0));
		Block above = world.getBlockAt(loc.add(0, 2, 0));

		return below.getType().isSolid() && below.getType().isOccluding() && (above.getType() == Material.AIR)
				&& (legs.getType() == Material.AIR);
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

	public int getMinY() {
		return minY;
	}

	public String getAuthor() {
		return author;
	}

	public String getName() {
		return name;
	}
}
