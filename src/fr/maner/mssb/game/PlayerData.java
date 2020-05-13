package fr.maner.mssb.game;

import org.bukkit.entity.Player;

import fr.maner.mssb.scoreboard.FastBoard;
import fr.maner.mssb.type.state.InGameState;
import fr.maner.mssb.utils.ConvertDate;

public class PlayerData {

	private InGameState inGameState;
	private Player p;
	private FastBoard board;

	public PlayerData(Player p, InGameState inGameState) {
		this.p = p;
		this.inGameState = inGameState;
		this.board = new FastBoard(p);
		this.board.updateTitle("§6MSSB");
	}

	public void updateLines() {
		if (board.isDeleted()) return;
		
		int kill = 0, death = 0;
		
		IGPlayerData igPlayerData = inGameState.getPlayersIGData().get(p.getUniqueId());
		if (igPlayerData != null) {
			kill = igPlayerData.getKill();
			death = igPlayerData.getDeath();
		}
		
		this.board.updateLines(
				"§eMode de jeu  : §a" + inGameState.getGameData().getGameConfig().getGameType().getName(),
				"§eMap : §a" + inGameState.getMapData().getName(),
				"§eTemps de jeu : §a" + getDuration(inGameState),
				"",
				String.format("§eKills : §a§l%d ⚔", kill),
				String.format("§eMorts : §c§l%d ☠", death));
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
