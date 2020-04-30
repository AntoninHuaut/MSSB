package fr.maner.mssb.factory;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

public class PotionFactory extends ItemFactory {
	
	private Color potionColor;

	public PotionFactory(Color color) {
		super(Material.POTION);
		this.potionColor = color;
	}

	public ItemStack build() {
		super.build();
				
		PotionMeta meta = (PotionMeta) item.getItemMeta();
		meta.setColor(potionColor);
		item.setItemMeta(meta);

		return item;
	}
}
