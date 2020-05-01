package fr.maner.mssb.runnable;

import org.bukkit.Bukkit;

import fr.maner.mssb.game.GameData;
import fr.maner.mssb.game.IGPlayerData;
import fr.maner.mssb.type.state.InGameState;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class GameRun implements Runnable {

	private GameData gameData;

	private int taskId;

	public GameRun(GameData gameData) {
		this.gameData = gameData;
		this.taskId = Bukkit.getScheduler().runTaskTimer(gameData.getPlugin(), this, 20, 20).getTaskId();
	}

	@Override
	public void run() {
		if (!gameData.getState().hasGameStart())
			return;

		InGameState inGameState = (InGameState) gameData.getState();

		Bukkit.getOnlinePlayers().forEach(p -> {
			IGPlayerData playerData = inGameState.getPlayersIGData().get(p.getUniqueId());
			if (playerData == null)
				return;

			p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
					String.format("§6§lStats : §a§l%d ⚔ §8§l| §c§l%d ☠", playerData.getKill(), playerData.getDeath())));
		});

		gameData.checkGameOver();
	}

	public void cancel() {
		Bukkit.getScheduler().cancelTask(taskId);
	}

}
