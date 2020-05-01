package fr.maner.mssb.entity;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.maner.mssb.factory.ItemFactory;
import fr.maner.mssb.factory.SkullFactory;
import fr.maner.mssb.game.GameData;
import fr.maner.mssb.type.state.InGameState;

public abstract class EntityClass {
	
	private GameData gameData;
	
	private ItemStack itemDisplay;
	private String color;
	private String name;
	
	public EntityClass(GameData gameData, String color, String name, String base64) {
		this.gameData = gameData;
		this.color = color;
		this.name = name;
		setItemDisplay(base64);
	}
	
	public String getColor() {
		return color;
	}

	public String getName() {
		return name;
	}
	
	public ItemStack getItemDisplay() {
		return itemDisplay;
	}
	
	private void setItemDisplay(String base64) {
		this.itemDisplay = new ItemFactory(SkullFactory.buildFromBase64(base64)).setName(getColor() + getName()).build();
	}
	
	public boolean isPlayableClass() { return this instanceof PlayableEntity; }
	
	public abstract void initPlayer(Player p);
	
	public void teleportOnMap(Player p) {
		if (!getGameData().getState().hasGameStart()) return;
		
		p.teleport(((InGameState) getGameData().getState()).getMapData().getLoc());
	}
	
	public GameData getGameData() {
		return gameData;
	}
}
