package fr.maner.mssb.factory;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ArmorFactory extends ItemFactory {

	private ArmorType armorType;
	private double bonusArmorProtection;

	public ArmorFactory(ArmorType armorType) {
		super(Material.valueOf(armorType.name()));

		this.armorType = armorType;
		setBonusArmorProtection(0.0);
		this.setUnbreakable(true);
	}

	public ArmorFactory(ItemStack buildFromBase64) {
		super(buildFromBase64);

		this.armorType = ArmorType.valueOf(buildFromBase64.getType().name());
		setBonusArmorProtection(0.0);
		this.setUnbreakable(true);
	}

	public ItemFactory setBonusArmorProtection(double bonusArmorProtection) {
		this.bonusArmorProtection = bonusArmorProtection;
		return this;
	}

	public ItemStack build() {
		super.build();

		ItemMeta itemMeta = item.getItemMeta();

		itemMeta.removeAttributeModifier(Attribute.GENERIC_ARMOR);
		itemMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(),
				"armorProtection", armorType.getCustomBasicArmor() + bonusArmorProtection, AttributeModifier.Operation.ADD_NUMBER, armorType.getSlot()));

		item.setItemMeta(itemMeta);

		return item;
	}

	public enum ArmorType {
		LEATHER_HELMET(0, EquipmentSlot.HEAD),

		PLAYER_HEAD(1, EquipmentSlot.HEAD),
		LEATHER_CHESTPLATE(3, EquipmentSlot.CHEST),
		LEATHER_LEGGINGS(2, EquipmentSlot.LEGS),
		LEATHER_BOOTS(1, EquipmentSlot.FEET);

		private EquipmentSlot slot;
		private double customBasicArmor;

		private ArmorType(double customBasicArmor, EquipmentSlot slot) {
			this.slot = slot;
			this.customBasicArmor = customBasicArmor;
		}

		public EquipmentSlot getSlot() {
			return slot;
		}

		public double getCustomBasicArmor() {
			return customBasicArmor;
		}
	}
}
