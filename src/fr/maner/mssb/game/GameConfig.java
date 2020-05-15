package fr.maner.mssb.game;

import fr.maner.mssb.MSSB;
import fr.maner.mssb.type.end.GameEnd;
import fr.maner.mssb.type.end.LifeEnd;
import fr.maner.mssb.type.game.GameType;
import fr.maner.mssb.type.game.NormalMode;
import fr.maner.mssb.utils.map.MapConfig;
import fr.maner.mssb.utils.songs.SongConfig;
import fr.maner.mssb.utils.specialloc.SpecialLocConfig;

public class GameConfig {
	
	private MapConfig mapConfig;
	private SongConfig songConfig;
	private SpecialLocConfig specialLogConfig;
	
	private GameType gameType;
	private GameEnd gameEnd;

	private boolean buildMode;

	public GameConfig(MSSB pl) {
		this.mapConfig = new MapConfig(pl);
		this.songConfig = new SongConfig(pl);
		this.specialLogConfig = new SpecialLocConfig(pl);
		
		setGameType(new NormalMode());
		setGameEnd(new LifeEnd());
		setBuildMode(false);
	}

	public GameType getGameType() {
		return gameType;
	}

	public void setGameType(GameType gameType) {
		this.gameType = gameType;
	}
	
	public GameEnd getGameEnd() {
		return gameEnd;
	}

	public void setGameEnd(GameEnd gameEnd) {
		this.gameEnd = gameEnd;
	}

	public boolean isBuildMode() {
		return buildMode;
	}

	public void setBuildMode(boolean buildMode) {
		this.buildMode = buildMode;
	}

	public MapConfig getMapConfig() {
		return mapConfig;
	}
	
	public SongConfig getSongConfig() {
		return songConfig;
	}
	
	public SpecialLocConfig getSpecialLogConfig() {
		return specialLogConfig;
	}
}
