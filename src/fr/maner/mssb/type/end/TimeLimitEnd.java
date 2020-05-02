package fr.maner.mssb.type.end;

import java.util.Optional;
import java.util.UUID;
import java.util.Map.Entry;

import org.bukkit.Bukkit;

import fr.maner.mssb.game.IGPlayerData;
import fr.maner.mssb.type.state.InGameState;

public class TimeLimitEnd extends GameEnd {
	
	private double timeLimit;
	private long timeMS;
	
	public TimeLimitEnd() {
		setTimeLimit(7.5);
	}

	public void setTimeLimit(double timeLimit) {
		if (timeLimit < 1.0) return;
		
		this.timeLimit = timeLimit;
		refreshTimeMS();
	}
	
	public void addTimeLimit(double d) {
		this.timeLimit += d;
		refreshTimeMS();
		
		if (this.timeLimit < 1) setTimeLimit(1.0);
	}
	
	private void refreshTimeMS() {
		this.timeMS = (long) (timeLimit * 60 * 1000);
	}
	
	public double getTimeLimit() {
		return timeLimit;
	}

	@Override
	public boolean checkGameOver(InGameState inGameState, int nbPlayablePlayers) {
		return inGameState.getStartTime() + timeMS < System.currentTimeMillis();
	}

	@Override
	public String getWinnerMessage(InGameState inGameState) {
		Optional<Entry<UUID, IGPlayerData>> winner = inGameState.getPlayersIGData().entrySet().stream().sorted(this::sortByKill).findFirst();
		if (!winner.isPresent()) return null;

		return String.format("&6%s &egagne le match !", Bukkit.getPlayer(winner.get().getKey()).getName());
	}

	public int sortByKill(Entry<UUID, IGPlayerData> e1, Entry<UUID, IGPlayerData> e2) {
		return e2.getValue().getKill() - e1.getValue().getKill();
	}
	
	@Override
	public String getConfigMessage() {
		return String.format("§eCelui avec le plus de kills au bout de §6%.1f §eminute(s)", getTimeLimit());
	}
}
