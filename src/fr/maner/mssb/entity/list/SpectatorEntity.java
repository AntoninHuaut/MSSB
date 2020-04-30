package fr.maner.mssb.entity.list;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import fr.maner.mssb.entity.EntityClass;
import fr.maner.mssb.game.GameData;
import fr.maner.mssb.utils.Heads;

public class SpectatorEntity extends EntityClass {

	private static final String COLOR = "§e";
	private static final String NAME = "Spectateur";

	public SpectatorEntity(GameData gameData) {
		super(gameData, COLOR, NAME, Heads.CAMERA);
	}

	@Override
	public void initPlayer(Player p) {
		if (!getGameData().getState().hasGameStart()) return;
		
		p.setGameMode(GameMode.SPECTATOR);
	}

}
