package fr.maner.mssb.type.end;

import java.util.UUID;

import org.bukkit.Bukkit;

import java.util.Map.Entry;
import java.util.Optional;

import fr.maner.mssb.game.data.IGPlayerData;
import fr.maner.mssb.type.state.InGameState;

public class KillEnd extends GameEnd {

	private int nbKillWin;

	public KillEnd() {
		setNBKill(10);
	}

	public void setNBKill(int nbKill) {
		if (nbKill < 1) return;
		this.nbKillWin = nbKill;
	}

	public void addKill(int nbKill) {
		this.nbKillWin += nbKill;
		if (this.nbKillWin < 1) setNBKill(1);
	}

	public int getNBKill() {
		return nbKillWin;
	}

	@Override
	public boolean checkGameOver(InGameState inGameState, int nbPlayablePlayers) {
		return inGameState.getPlayersIGData().entrySet().stream().filter(entry -> entry.getValue().getKill() >= nbKillWin).count() > 0;
	}

	@Override
	public String getWinnerMessage(InGameState inGameState) {
		Optional<Entry<UUID, IGPlayerData>> winner = inGameState.getPlayersIGData().entrySet().stream().sorted(this::sortByKill).findFirst();
		if (!winner.isPresent()) return null;

		return String.format("&6%s &egagne le match !", Bukkit.getOfflinePlayer(winner.get().getKey()).getName());
	}

	public int sortByKill(Entry<UUID, IGPlayerData> e1, Entry<UUID, IGPlayerData> e2) {
		return e2.getValue().getKill() - e1.getValue().getKill();
	}

	@Override
	public String getConfigMessage() {
		return String.format("Le premier qui atteint §6%d §ekill(s)", getNBKill());
	}

	@Override
	public String getObjectifMessage() {
		return String.format("Être le 1er à §6%d §ekill(s)", getNBKill());
	}

	@Override
	public double getProgress(InGameState inGameState) {
		Optional<Entry<UUID, IGPlayerData>> playerTopKiller = inGameState.getPlayersIGData().entrySet().stream().sorted(this::sortByKill).findFirst();
		if (playerTopKiller == null || !playerTopKiller.isPresent()) return 0D;
		
		return (double) playerTopKiller.get().getValue().getKill() / getNBKill();
	}
}
