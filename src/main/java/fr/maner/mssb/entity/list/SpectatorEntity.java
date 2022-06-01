package fr.maner.mssb.entity.list;

import fr.maner.mssb.entity.EntityClass;
import fr.maner.mssb.game.GameData;
import fr.maner.mssb.type.state.InGameState;
import fr.maner.mssb.utils.Heads;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class SpectatorEntity extends EntityClass {

    private static final String COLOR = "§e";
    private static final String NAME = "Spectateur";

    public SpectatorEntity(GameData gameData) {
        super(gameData, COLOR, NAME, Heads.CAMERA);
    }

    @Override
    public EntityClass initPlayer(Player p) {
        if (!getGameData().getState().hasGameStart()) return this;

        p.setGameMode(GameMode.SPECTATOR);
        return this;
    }

    @Override
    public void teleportOnMap(Player p) {
        if (!getGameData().getState().hasGameStart()) return;

        p.teleport(((InGameState) getGameData().getState()).getMapData().getLoc());
    }

}
