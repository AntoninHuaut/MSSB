package fr.maner.mssb.factory;

import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class LeatherArmorFactory extends ArmorFactory {
		
	private Color armorColor;

	public LeatherArmorFactory(ArmorType armorType, Color color) {
		super(armorType);
		
		this.armorColor = color;
	}

	public ItemStack build() {
		super.build();
				
		LeatherArmorMeta itemMeta = (LeatherArmorMeta) item.getItemMeta();
		itemMeta.setColor(armorColor);
		item.setItemMeta(itemMeta);

		return item;
	}
}
