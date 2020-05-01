package fr.maner.mssb.type.end;

import fr.maner.mssb.game.GameData;
import fr.maner.mssb.type.state.InGameState;

public abstract class GameEnd {

	public abstract boolean isGameOver(GameData gameData, InGameState inGameState);
	
	public boolean isKillEnd() { return this instanceof KillEnd; }
	public boolean isLifeEnd() { return this instanceof LifeEnd; }
	public boolean isTimeLimitEnd() { return this instanceof TimeLimitEnd; }
	
}
