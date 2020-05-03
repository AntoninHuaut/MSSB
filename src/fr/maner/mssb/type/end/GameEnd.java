package fr.maner.mssb.type.end;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.maner.mssb.entity.EntityManager;
import fr.maner.mssb.game.GameData;
import fr.maner.mssb.type.state.InGameState;

public abstract class GameEnd {
	
	private int nbPlayablePlayerAtStart;

	public abstract String getConfigMessage();
	public abstract String getObjectifMessage();
	public abstract double getProgress(InGameState inGameState);
	public abstract String getWinnerMessage(InGameState inGameState);

	public abstract boolean checkGameOver(InGameState inGameState, int nbPlayablePlayers);
	public boolean isGameOver(InGameState inGameState) {
		int nbPlayablePlayers = getNbPlayablePlayers();

		return nbPlayablePlayers <= 1 || checkGameOver(inGameState, nbPlayablePlayers);
	}

	public void checkPlayer(GameData gameData, InGameState inGameState, Player p) {}
	public boolean isKillEnd() { return this instanceof KillEnd; }
	public boolean isLifeEnd() { return this instanceof LifeEnd; }
	public boolean isTimeLimitEnd() { return this instanceof TimeLimitEnd; }

	public int getNbPlayablePlayerAtStart() {
		return nbPlayablePlayerAtStart;
	}
	
	public void setNbPlayablePlayerAtStart() {
		this.nbPlayablePlayerAtStart = getNbPlayablePlayers();
	}
	
	public int getNbPlayablePlayers() {
		Collection<? extends Player> players = Bukkit.getOnlinePlayers();
		return (int) players.stream().filter(p -> EntityManager.getInstance().getPlayableClassPlayer(p.getUniqueId()) != null).count();
	}
}
