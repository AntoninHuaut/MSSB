package fr.maner.mssb.entity;

import fr.maner.mssb.entity.list.RandomEntity;
import fr.maner.mssb.entity.list.SpectatorEntity;
import fr.maner.mssb.entity.list.playable.*;
import fr.maner.mssb.game.GameData;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class EntityManager {

    private static EntityManager instance;

    private HashMap<UUID, EntityClass> playersClass = new HashMap<UUID, EntityClass>();

    private List<EntityClass> entityList = new ArrayList<EntityClass>();
    private List<PlayableEntity> playableEntityList = new ArrayList<PlayableEntity>();

    public List<EntityClass> getEntityList(GameData gameData) {
        if (entityList.isEmpty()) {
            entityList.add(new RandomEntity(gameData));
            entityList.add(new SpectatorEntity(gameData));
            entityList.addAll(getPlayableEntityList(gameData));
        }

        return entityList;
    }

    public List<PlayableEntity> getPlayableEntityList(GameData gameData) {
        if (playableEntityList.isEmpty()) {
            playableEntityList.add(new BlazeEntity(gameData));
            playableEntityList.add(new CreeperEntity(gameData));
            playableEntityList.add(new DrownedEntity(gameData));
            playableEntityList.add(new SkeletonEntity(gameData));
            playableEntityList.add(new WolfEntity(gameData));
            playableEntityList.add(new ZombieEntity(gameData));

            playableEntityList.forEach(ent -> gameData.getPlugin().getServer().getPluginManager().registerEvents(ent, gameData.getPlugin()));
        }

        return playableEntityList;
    }

    @Nullable
    public EntityClass getClassPlayer(UUID pUUID) {
        return playersClass.get(pUUID);
    }

    @Nullable
    public PlayableEntity getPlayableClassPlayer(UUID pUUID) {
        EntityClass entity = playersClass.get(pUUID);
        if (!(entity instanceof PlayableEntity))
            return null;

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
        if (instance == null)
            instance = new EntityManager();
        return instance;
    }
}
