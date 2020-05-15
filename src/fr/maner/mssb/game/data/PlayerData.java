package fr.maner.mssb.game.data;

import org.bukkit.entity.Player;

import fr.maner.mssb.game.GameData;
import fr.maner.mssb.scoreboard.FastBoard;
import fr.maner.mssb.type.state.InGameState;
import fr.maner.mssb.utils.ConvertDate;

public class PlayerData {

	private GameData gameData;
	private Player p;
	private FastBoard board;

	public PlayerData(Player p, GameData gameData) {
		this.p = p;
		this.gameData = gameData;
		
		this.gameData.getSoundData().addPlayer(p);
	}

	public void updateLines() {
		if (board.isDeleted() || !gameData.getState().hasGameStart())
			return;

		InGameState inGameState = (InGameState) gameData.getState();

		int kill = 0, death = 0;

		IGPlayerData igPlayerData = inGameState.getPlayersIGData().get(p.getUniqueId());
		if (igPlayerData != null) {
			kill = igPlayerData.getKill();
			death = igPlayerData.getDeath();
		}

		board.updateLines("§eMode de jeu  : §a" + inGameState.getGameData().getGameConfig().getGameType().getName(), "§eMap : §a" + inGameState.getMapData().getName(),
				"§eTemps de jeu : §a" + getDuration(inGameState), "", String.format("§eKills : §a§l%d ⚔", kill), String.format("§eMorts : §c§l%d ☠", death));
	}
	
	public void createBoard() {
		this.board = new FastBoard(p);
		this.board.updateTitle("§6MSSB");
	}

	public void deleteScoreboard() {
		if (board == null || board.isDeleted())
			return;

		board.delete();
	}

	public Player getP() {
		return p;
	}

	public FastBoard getScoreboard() {
		return board;
	}

	public void setP(Player p) {
		this.p = p;
	}

	public void setScoreboard(FastBoard board) {
		this.board = board;
	}

	private String getDuration(InGameState inGameState) {
		return ConvertDate.millisToShortDHMS(System.currentTimeMillis() - inGameState.getStartTime());
	}
}
