package fr.maner.mssb.inventory;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import fr.maner.mssb.factory.ItemFactory;
import fr.maner.mssb.factory.SkullFactory;
import fr.maner.mssb.inventory.init.InvGUI.InvGUIAction;
import fr.maner.mssb.utils.Heads;

public enum ConfigGUIEnum {

	GTYPE_NORMAL(1, new ItemFactory(Material.DIAMOND_SWORD).setName("&aMode normal")
					.addLore("&7Système de vie classique", "&7Vous devez tuer vos adversaires", "&8&oCliquez pour passer en mode KB")
					.addItemFlags(ItemFlag.HIDE_ATTRIBUTES).
					build()),

	GTYPE_KB(1, new ItemFactory(Material.SNOWBALL).setName("&aKnockback Mode")
					.addLore("&7Pas de système de vie", "&7Plus vous subissez de dégats", "&7Plus vous êtes projeté loin", "&8&oCliquez pour passer en mode normal")
					.build()),

	KB_MULTIPLIER(0, new ItemFactory(Material.FISHING_ROD).addLore(getDoubleInfos()).build()),
	REGEN_KB(2, ItemFactory.createPotion(Color.AQUA).addItemFlags(ItemFlag.HIDE_POTION_EFFECTS).addLore("&7Réduction hors combat").addLore(getDoubleInfos()).buildPotion()),
	REGEN_HEALTH(2, ItemFactory.createPotion(Color.FUCHSIA).addItemFlags(ItemFlag.HIDE_POTION_EFFECTS).addLore("&7Régénération hors combat").addLore(getDoubleInfos()).buildPotion()),

	GEND_KILL(7, new ItemFactory(SkullFactory.buildFromBase64(Heads.DEATH)).setName("&aLimitation par kill")
			.addLore("&7Lorsqu'un joueur atteint le nombre de kill défini", "&7Le jeu se termine")
			.build()),
	
	GEND_LIFE(7, new ItemFactory(SkullFactory.buildFromBase64(Heads.HEART)).setName("&aLimitation par vie")
			.addLore("&7Quand vous mourrez, vous perdez une vie", "&7Vous êtes éliminé quand vous n'en avez plus", "&7Le jeu se termine quand il n'y a plus qu'un seul joueur")
			.build()),
	
	GEND_TIME_LIMIT(7, new ItemFactory(SkullFactory.buildFromBase64(Heads.CLOCK)).setName("&aLimitation de temps")
			.addLore("&7Définissez la durée fixe de la partie")
			.build()),

	NB_LIFE(8, new ItemFactory(Material.PAPER).addLore(getIntInfos()).build()),
	NB_KILL(8, new ItemFactory(Material.PAPER).addLore(getIntInfos()).build()),
	TIME_LIMIT(8, new ItemFactory(Material.PAPER).addLore(getDoubleInfos()).build()),
	
	START_GAME(22, new ItemFactory(Material.GREEN_DYE).setName("&aCommencer la partie").build());

	private int slot;
	private ItemStack item;
	private InvGUIAction action;

	private ConfigGUIEnum(int slot, ItemStack item) {
		this.slot = slot;
		this.item = item;
	}

	public int getSlot() {
		return slot;
	}

	public ItemStack getItem() {
		return item;
	}

	public void setAction(InvGUIAction action) {
		this.action = action;
	}

	public InvGUIAction getAction() {
		return action;
	}

	public static String[] getDoubleInfos() {
		return new String[] { "&7Clic normal &8: &a+&7/&c- &b1.0", "&7Shift clic &8: &a+&7/&c- &b0.5", "&7Drop item &8: &a+ &b0.1" };
	}
	
	public static String[] getIntInfos() {
		return new String[] { "&7Clic normal &8: &a+&7/&c- &b5", "&7Shift clic &8: &a+&7/&c- &b1" };
	}
}