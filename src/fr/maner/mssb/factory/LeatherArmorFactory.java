package fr.maner.mssb.factory;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class LeatherArmorFactory extends ItemFactory {
	
	public enum LeatherArmor {
		LEATHER_HELMET,
		LEATHER_CHESTPLATE,
		LEATHER_LEGGINGS,
		LEATHER_BOOTS;
	}
	
	private Color armorColor;

	public LeatherArmorFactory(LeatherArmor leatherArmor, Color color) {
		super(Material.valueOf(leatherArmor.name()));
		this.armorColor = color;
	}

	public ItemStack build() {
		super.build();
				
		LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
		meta.setColor(armorColor);
		item.setItemMeta(meta);

		return item;
	}
}
