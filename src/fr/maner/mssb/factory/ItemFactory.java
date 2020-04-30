package fr.maner.mssb.factory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;

public class ItemFactory {

	private ItemStack item;

	private int amount = 1;
	private String name;
	private boolean unbreakable = false;
	
	private Color potionColor;
	
	private List<String> lores = new ArrayList<String>();
	private Map<Enchantment, Integer> enchantments = new HashMap<Enchantment, Integer>();
	private Set<ItemFlag> itemFlags = new HashSet<ItemFlag>();

	public ItemFactory(Material material) {
		this.item = new ItemStack(material);
	}

	public ItemFactory(ItemStack itemStack) {
		this.item = itemStack;
		this.amount = item.getAmount();

		ItemMeta itemMeta = item.getItemMeta();
		this.unbreakable = itemMeta.isUnbreakable();
		this.enchantments = itemMeta.getEnchants();
		this.itemFlags = itemMeta.getItemFlags();

		if (!itemMeta.getDisplayName().isEmpty()) this.name = itemMeta.getDisplayName();
		if (itemMeta.getLore() != null) this.lores = itemMeta.getLore();
	}

	public ItemFactory setAmount(int amount) {
		this.amount = amount;
		return this;
	}

	public ItemFactory setName(String name) {
		this.name = name;
		return this;
	}	

	public ItemFactory setUnbreakable(boolean unbreakable) {
		this.unbreakable = unbreakable;
		return this;
	}

	public ItemFactory addLore(String... lore) {
		this.lores.addAll(Arrays.asList(lore));
		return this;
	}
	
	public ItemFactory setLore(String... lore) {
		this.lores.clear();
		this.addLore(lore);
		return this;
	}

	public ItemFactory addEnchantment(Entry<Enchantment, Integer> enchantment) {
		this.enchantments.put(enchantment.getKey(), enchantment.getValue());
		return this;
	}

	public ItemFactory addItemFlags(ItemFlag... itemFlags) {
		this.itemFlags.addAll(Arrays.asList(itemFlags));
		return this;
	}

	public ItemFactory setPotionColor(Color color) {
		this.potionColor = color;
		return this;
	}

	public ItemStack build() {
		item.setAmount(amount);
		item.addEnchantments(enchantments);

		ItemMeta itemMeta = item.getItemMeta();

		if (name != null) itemMeta.setDisplayName(convert(name));
		itemFlags.forEach(itemMeta::addItemFlags);

		itemMeta.setLore(lores.stream().map(lore -> convert(lore)).collect(Collectors.toList()));
		itemMeta.setUnbreakable(unbreakable);

		this.item.setItemMeta(itemMeta);
		return item;
	}

	public ItemStack buildPotion() {
		build();
		if (!(item.getItemMeta() instanceof PotionMeta)) return null;
		
		PotionMeta meta = (PotionMeta) item.getItemMeta();
		meta.setColor(potionColor);
		item.setItemMeta(meta);

		return item;
	}

	private String convert(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}

	public static ItemFactory createPotion(Color color) {
		return new ItemFactory(Material.POTION).setPotionColor(color);
	}
}
