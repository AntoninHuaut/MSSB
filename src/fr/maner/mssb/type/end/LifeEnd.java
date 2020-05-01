package fr.maner.mssb.type.end;

import fr.maner.mssb.game.GameData;
import fr.maner.mssb.type.state.InGameState;

public class LifeEnd extends GameEnd {
	
	private int nbLife;
	
	public LifeEnd() {
		setNBLife(5);
	}

	public void setNBLife(int nbLife) {
		if (nbLife < 1) return;
		this.nbLife = nbLife;
	}
	
	public void addLife(int nbLife) {
		this.nbLife += nbLife;
		if (this.nbLife < 1) setNBLife(1);
	}
	
	public int getNBLife() {
		return nbLife;
	}

	@Override
	public boolean isGameOver(GameData gameData, InGameState inGameState) {
		return inGameState.getPlayersIGData().entrySet().stream().filter(entry -> entry.getValue().getDeath() >= nbLife).count() > 0;
	}
}
