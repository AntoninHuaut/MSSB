package fr.maner.mssb.runnable;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.maner.mssb.game.GameData;
import fr.maner.mssb.type.end.GameEnd;
import fr.maner.mssb.type.state.InGameState;

public class GameRun implements Runnable {

	private GameData gameData;
	private GameEnd gameEnd;

	private int taskId;

	public GameRun(GameData gameData) {
		this.gameData = gameData;
		this.gameEnd = gameData.getGameConfig().getGameEnd();
		this.taskId = Bukkit.getScheduler().runTaskTimer(gameData.getPlugin(), this, 20, 20).getTaskId();
	}

	@Override
	public void run() {
		if (!gameData.getState().hasGameStart())
			return;

		InGameState inGameState = (InGameState) gameData.getState();
		inGameState.getBossBar().setProgress(1.0D - gameEnd.getProgress(inGameState));

		Bukkit.getOnlinePlayers().forEach(p -> {
			sendStats(inGameState, p);

			gameData.getGameConfig().getGameType().regenPlayer(p);
		});

		gameData.checkGameOver();
	}

	public void sendStats(InGameState inGameState, Player p) {
		// TODO SET STATS BELOW NAME
//		inGameState.getPlayersData().get(p.getUniqueId()).updateLines();
//		IGPlayerData igPlayerData = inGameState.getPlayersIGData().get(p.getUniqueId());
//
//		if (igPlayerData != null)
//			p.sendMessage(String.format("§6§lStats : §a§l%d ⚔ §8§l| §c§l%d ☠", igPlayerData.getKill(), igPlayerData.getDeath()));
	}

	public void cancel() {
		Bukkit.getScheduler().cancelTask(taskId);
	}

}
