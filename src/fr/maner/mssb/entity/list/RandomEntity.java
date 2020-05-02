package fr.maner.mssb.entity.list;

import java.util.List;
import java.util.Random;

import org.bukkit.entity.Player;

import fr.maner.mssb.entity.EntityClass;
import fr.maner.mssb.entity.EntityManager;
import fr.maner.mssb.entity.list.playable.PlayableEntity;
import fr.maner.mssb.game.GameData;
import fr.maner.mssb.utils.Heads;

public class RandomEntity extends EntityClass {

	private static final String COLOR = "§e";
	private static final String NAME = "Aléatoire";
	private static Random random = new Random();

	public RandomEntity(GameData gameData) {
		super(gameData, COLOR, NAME, Heads.RANDOM);
	}

	@Override
	public EntityClass initPlayer(Player p) {
		List<PlayableEntity> playableEntity = EntityManager.getInstance().getPlayableEntityList(getGameData());
		EntityClass newEntity;

		if (playableEntity.isEmpty()) {
			newEntity = new SpectatorEntity(getGameData());
			p.sendMessage(" §6» §cAucune classe jouable est disponible");
		}
		else
			newEntity = playableEntity.get(random.nextInt(playableEntity.size()));


		EntityManager.getInstance().setClassPlayer(p.getUniqueId(), newEntity);
		newEntity.initPlayer(p);

		p.sendMessage(String.format(" §6» §eVous avez obtenu la classe %s%s", newEntity.getColor(), newEntity.getName()));
		
		return newEntity;
	}

	@Override
	public void teleportOnMap(Player p) {
		p.kickPlayer("§cErreur, vous ne devriez pas être en random à ce stade de la partie");
	}

}
