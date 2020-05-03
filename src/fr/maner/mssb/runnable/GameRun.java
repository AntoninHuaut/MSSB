package fr.maner.mssb.runnable;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.maner.mssb.game.GameData;
import fr.maner.mssb.game.IGPlayerData;
import fr.maner.mssb.type.end.GameEnd;
import fr.maner.mssb.type.state.InGameState;
import fr.maner.mssb.utils.ConvertDate;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

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
			sendActionBarStats(inGameState, p);

			gameData.getGameConfig().getGameType().regenPlayer(p);
		});

		gameData.checkGameOver();
	}

	public void sendActionBarStats(InGameState inGameState, Player p) {
		IGPlayerData playerData = inGameState.getPlayersIGData().get(p.getUniqueId());

		if (playerData != null)
			p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
					TextComponent.fromLegacyText(String.format("§6§lStats : §a§l%d ⚔ §8§l| §c§l%d ☠ §8§l| §b§l%s",
							playerData.getKill(), playerData.getDeath(), getDuration(inGameState))));
	}

	private String getDuration(InGameState inGameState) {
		return ConvertDate.millisToShortDHMS(System.currentTimeMillis() - inGameState.getStartTime());
	}

	public void cancel() {
		Bukkit.getScheduler().cancelTask(taskId);
	}

}
