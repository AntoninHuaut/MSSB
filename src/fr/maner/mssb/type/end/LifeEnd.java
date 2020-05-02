package fr.maner.mssb.type.end;

import java.util.Optional;
import java.util.UUID;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import fr.maner.mssb.entity.EntityManager;
import fr.maner.mssb.entity.list.SpectatorEntity;
import fr.maner.mssb.game.GameData;
import fr.maner.mssb.game.IGPlayerData;
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
	public void checkPlayer(GameData gameData, InGameState inGameState, Player p) {
		IGPlayerData pData = inGameState.getPlayersIGData().get(p.getUniqueId());
		if (pData == null) return;

		if (pData.getDeath() >= nbLife) {
			EntityManager.getInstance().setClassPlayer(p.getUniqueId(), new SpectatorEntity(gameData));

			IGPlayerData playerData = inGameState.getPlayersIGData().get(p.getUniqueId());
			if (playerData != null)
				p.sendTitle(ChatColor.translateAlternateColorCodes('&', "&cEliminé"), String.format("§6§lStats : §a§l%d ⚔ §8§l| §c§l%d ☠", playerData.getKill(), playerData.getDeath()), 10, 70, 20);

			inGameState.initPlayer(p);
		}
	}

	@Override
	public boolean checkGameOver(InGameState inGameState, int nbPlayablePlayers) {
		return nbPlayablePlayers <= 1;
	}

	@Override
	public String getWinnerMessage(InGameState inGameState) {
		Optional<Entry<UUID, IGPlayerData>> winner = inGameState.getPlayersIGData().entrySet().stream().sorted(this::sortByLessDeath).findFirst();
		if (!winner.isPresent()) return null;

		return String.format("&6%s &egagne le match !", Bukkit.getPlayer(winner.get().getKey()).getName());
	}
	
	public int sortByLessDeath(Entry<UUID, IGPlayerData> e1, Entry<UUID, IGPlayerData> e2) {
		return e1.getValue().getDeath() - e2.getValue().getDeath();
	}

	@Override
	public String getConfigMessage() {
		return String.format("§eLe dernier encore en vie §7(§6%d §evie(s)§7)", getNBLife());
	}
}
