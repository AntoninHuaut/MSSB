package fr.maner.mssb.entity.list.playable;

import fr.maner.mssb.entity.PlayableEntity;
import fr.maner.mssb.game.GameData;
import fr.maner.mssb.utils.Heads;

public class SpiderEntity extends PlayableEntity {
	
	private static final String COLOR = "§0";
	private static final String NAME = "Araignée";

	public SpiderEntity(GameData gameData) {
		super(gameData, COLOR, NAME, Heads.SPIDER);
	}

	@Override
	public double getWeaponDamage() {
		return 6;
	}

}
