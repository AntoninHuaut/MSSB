package fr.maner.mssb.runnable;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import fr.maner.mssb.entity.EntityManager;
import fr.maner.mssb.entity.list.playable.PlayableEntity;
import fr.maner.mssb.game.GameData;
import fr.maner.mssb.game.IGPlayerData;
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
				sendGameInfos(p);
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
		
		Bukkit.getOnlinePlayers().forEach(p -> {
			p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.3F, (0.7F + (0.3F * iteration)));
		});

		if (title != null)
			for (Player p : Bukkit.getOnlinePlayers())
				p.sendTitle(ChatColor.translateAlternateColorCodes('&', title), null, fadeIn, stay, fadeOut);

		iteration++;
	}

	private void sendGameInfos(Player p) {
		p.sendMessage("§b§lParamètres de la partie :\n");
		p.sendMessage(" §aMode de jeu : " + gameData.getGameConfig().getGameType().getConfigMessage());
		p.sendMessage(" §aParamètre de victoire : " + gameData.getGameConfig().getGameEnd().getConfigMessage());
		p.sendMessage(" §aMap sélectionnée : §e" + mapData.getName() + " §7(§epar §6" + mapData.getAuthor() + "§7)");
	}

	private void startGame() {
		cancel();
		initPlayer(); // LobbyState

		gameData.createRunnable();
		gameData.setGameState(new InGameState(gameData, mapData), false);

		initPlayer(); // InGameState
		initStart();
	}

	private void initStart() {
		EntityManager.getInstance().getPlayableEntityList(gameData).forEach(pEntity -> pEntity.initEntity());

		Bukkit.getOnlinePlayers().forEach(p -> {
			PlayableEntity playableEntity = EntityManager.getInstance().getPlayableClassPlayer(p.getUniqueId());

			if (playableEntity != null)
				((InGameState) gameData.getState()).getPlayersIGData().put(p.getUniqueId(), new IGPlayerData());
		});
		
		gameData.getGameConfig().getGameEnd().setNbPlayablePlayerAtStart();
	}

	private void initPlayer() {
		Bukkit.getOnlinePlayers().forEach(p -> {
			gameData.getState().initPlayer(p);
			setAttackSpeed(p, 16.0D);
		});
	}

	private void setAttackSpeed(Player p, double attackSpeed) {
		p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(attackSpeed);
		p.saveData();
	}

	public void cancel() {
		Bukkit.getScheduler().cancelTask(taskId);
	}
}
