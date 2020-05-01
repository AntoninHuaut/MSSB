package fr.maner.mssb.type.end;

import fr.maner.mssb.game.GameData;
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
	public boolean isGameOver(GameData gameData, InGameState inGameState) {
		return inGameState.getPlayersIGData().entrySet().stream().filter(entry -> entry.getValue().getKill() >= nbKillWin).count() > 0;
	}
}
