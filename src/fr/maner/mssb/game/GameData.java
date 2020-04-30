package fr.maner.mssb.game;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;

import fr.maner.mssb.MSSB;
import fr.maner.mssb.type.state.GameState;
import fr.maner.mssb.type.state.InGameState;
import fr.maner.mssb.type.state.LobbyState;
import fr.maner.mssb.utils.map.MapData;

public class GameData {
	
	private transient MSSB pl;
	
	public GameData(MSSB pl) {
		this.pl = pl;
		this.config = new GameConfig(pl);
		setGameState(new LobbyState(this));
	}
	
	private GameState state;
	private GameConfig config;
	
	public void startGame(MapData mapData) {
		if (state.hasGameStart()) return;
		
		getGameConfig().setBuildMode(false);
		setGameState(new InGameState(this, mapData));
	}
	
	public void stopGame() {
		if (!state.hasGameStart()) return;
		
		setGameState(new LobbyState(this));
		
		// TODO
	}
	
	private void setGameState(GameState newState) {
		HandlerList.unregisterAll(state);
		this.state = newState;
		pl.getServer().getPluginManager().registerEvents(newState, pl);
		
		Bukkit.getOnlinePlayers().forEach(p -> newState.initPlayer(p));
	}
	
	public GameState getState() {
		return state;
	}
	
	public GameConfig getGameConfig() {
		return config;
	}
	
	public MSSB getPlugin() {
		return pl;
	}
}
