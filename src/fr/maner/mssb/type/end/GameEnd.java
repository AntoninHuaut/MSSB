package fr.maner.mssb.type.end;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.maner.mssb.entity.EntityManager;
import fr.maner.mssb.game.GameData;
import fr.maner.mssb.type.state.InGameState;

public abstract class GameEnd {

	public abstract boolean checkGameOver(InGameState inGameState, int nbPlayablePlayers);
	public boolean isGameOver(InGameState inGameState) {
		Collection<? extends Player> players = Bukkit.getOnlinePlayers();
		int nbPlayablePlayers = (int) players.stream().filter(p -> EntityManager.getInstance().getPlayableClassPlayer(p.getUniqueId()) != null).count();
		
		return nbPlayablePlayers <= 1 || checkGameOver(inGameState, nbPlayablePlayers);
	}
	
	public void checkPlayer(GameData gameData, InGameState inGameState, Player p) {}
	public boolean isKillEnd() { return this instanceof KillEnd; }
	public boolean isLifeEnd() { return this instanceof LifeEnd; }
	public boolean isTimeLimitEnd() { return this instanceof TimeLimitEnd; }
	
}
