package fr.maner.mssb.entity;

import fr.maner.mssb.entity.list.playable.PlayableEntity;
import fr.maner.mssb.factory.armor.ArmorFactory;
import fr.maner.mssb.factory.item.SkullFactory;
import fr.maner.mssb.game.GameData;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public abstract class EntityClass {

    private GameData gameData;

    private ItemStack playerHead;
    private String color;
    private String name;

    public EntityClass(GameData gameData, String color, String name, String base64) {
        this.gameData = gameData;
        this.color = color;
        this.name = name;
        setPlayerHead(base64);
    }

    public String getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public ItemStack getPlayerHead() {
        return playerHead;
    }

    private void setPlayerHead(String base64) {
        this.playerHead = new ArmorFactory(SkullFactory.buildFromBase64(base64)).setName(getColor() + getName()).build();
    }

    public boolean isPlayableClass() {
        return this instanceof PlayableEntity;
    }

    public abstract EntityClass initPlayer(Player p);

    public abstract void teleportOnMap(Player p);

    public void fallInVoid(EntityDamageEvent e) {
        teleportOnMap((Player) e.getEntity());
    }

    public GameData getGameData() {
        return gameData;
    }
}
