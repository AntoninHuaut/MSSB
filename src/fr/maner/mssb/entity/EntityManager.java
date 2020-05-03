package fr.maner.mssb.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import fr.maner.mssb.entity.list.RandomEntity;
import fr.maner.mssb.entity.list.SpectatorEntity;
import fr.maner.mssb.entity.list.playable.BlazeEntity;
import fr.maner.mssb.entity.list.playable.PlayableEntity;
import fr.maner.mssb.entity.list.playable.SkeletonEntity;
import fr.maner.mssb.entity.list.playable.WolfEntity;
import fr.maner.mssb.entity.list.playable.ZombieEntity;
import fr.maner.mssb.game.GameData;

public class EntityManager {
	
	private static EntityManager instance;
	
	private HashMap<UUID, EntityClass> playersClass = new HashMap<UUID, EntityClass>();
	
	public List<EntityClass> getEntityList(GameData gameData) {
		List<EntityClass> entityList = new ArrayList<EntityClass>();
		
		entityList.add(new RandomEntity(gameData));
		entityList.add(new SpectatorEntity(gameData));
		entityList.addAll(getPlayableEntityList(gameData));
		
		return entityList;
	}
	
	public List<PlayableEntity> getPlayableEntityList(GameData gameData) {
		List<PlayableEntity> playableEntityList = new ArrayList<PlayableEntity>();
		
		playableEntityList.add(new BlazeEntity(gameData));
		playableEntityList.add(new SkeletonEntity(gameData));
		playableEntityList.add(new WolfEntity(gameData));
		playableEntityList.add(new ZombieEntity(gameData));
		
		return playableEntityList;
	}
	
	@Nullable
	public EntityClass getClassPlayer(UUID pUUID) {
		return playersClass.get(pUUID);
	}
	
	@Nullable
	public PlayableEntity getPlayableClassPlayer(UUID pUUID) {
		EntityClass entity = playersClass.get(pUUID);
		if (entity == null || !(entity instanceof PlayableEntity)) return null;
		
		return (PlayableEntity) entity;
	}
	
	public void setClassPlayer(UUID pUUID, EntityClass entityClass) {
		playersClass.put(pUUID, entityClass);
	}
	
	public boolean removeClassPlayer(UUID pUUID) {
		if (playersClass.containsKey(pUUID)) {
			playersClass.remove(pUUID);			
			return true;
		}
		
		return false;
	}
	
	public void resetClassPlayers() {
		playersClass.clear();
	}

	public static EntityManager getInstance() {
		if (instance == null) instance = new EntityManager();
		return instance;
	}
}
