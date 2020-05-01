package fr.maner.mssb.runnable;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import fr.maner.mssb.game.GameData;
import fr.maner.mssb.type.state.InGameState;
import fr.maner.mssb.utils.map.MapData;

public class StartRun implements Runnable {

	private GameData gameData;
	private MapData mapData;
	private int taskId;
	private int iteration;

	public StartRun(JavaPlugin pl, GameData gameData, MapData mapData) {
		this.taskId = Bukkit.getScheduler().runTaskTimer(pl, this, 0, 20).getTaskId();
		this.iteration = 0;
		this.gameData = gameData;
		this.mapData = mapData;
	}

	@Override
	public void run() {
		String title = null;
		int fadeIn = 5, stay = 10, fadeOut = 5;

		switch (iteration) {
		case 0:
			Bukkit.getOnlinePlayers().forEach(p -> {
				p.getInventory().clear();
				p.closeInventory();
			});
			
			title = "&aLa partie va commencer";
			stay = 30;
			break;
		case 1:
			break;
		case 2:
			title = "&eDébut dans &c&l3";
			break;
		case 3:
			title = "&eDébut dans &6&l2";
			break;
		case 4:
			title = "&eDébut dans &a&l1";
			break;
		case 5:
			startGame();
			break;
		}

		if (title != null)
			for (Player p : Bukkit.getOnlinePlayers())
				p.sendTitle(ChatColor.translateAlternateColorCodes('&', title), null, fadeIn, stay, fadeOut);

		iteration++;
	}

	private void startGame() {
		cancel();
		initPlayer(); // LobbyState
		
		gameData.createRunnable();
		gameData.setGameState(new InGameState(gameData, mapData), false);
		
		initPlayer(); // InGameState
	}

	private void initPlayer() {
		Bukkit.getOnlinePlayers().forEach(p -> gameData.getState().initPlayer(p));
	}

	public void cancel() {
		Bukkit.getScheduler().cancelTask(taskId);
	}
}
