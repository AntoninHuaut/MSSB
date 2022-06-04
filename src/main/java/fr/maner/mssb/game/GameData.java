package fr.maner.mssb.game;

import fr.maner.mssb.MSSB;
import fr.maner.mssb.entity.EntityClass;
import fr.maner.mssb.entity.EntityManager;
import fr.maner.mssb.factory.item.BookFactory;
import fr.maner.mssb.game.data.PlayerData;
import fr.maner.mssb.game.data.SoundData;
import fr.maner.mssb.runnable.GameRun;
import fr.maner.mssb.runnable.StartRun;
import fr.maner.mssb.runnable.itemeffect.ItemRun;
import fr.maner.mssb.type.state.GameState;
import fr.maner.mssb.type.state.InGameState;
import fr.maner.mssb.type.state.LobbyState;
import fr.maner.mssb.utils.map.MapData;
import fr.maner.mssb.utils.songs.SongConfig.SongEnum;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GameData {

    private final transient MSSB pl;
    private final Map<UUID, PlayerData> playersData = new HashMap<>();
    private final SoundData soundData;
    private final GameConfig config;

    private GameState state;
    private ItemRun itemEffectRun;
    private GameRun gameRun;

    public GameData(MSSB pl) {
        this.pl = pl;
        this.config = new GameConfig(pl);
        setGameState(new LobbyState(this), true);
        this.soundData = new SoundData(this);

        Bukkit.getOnlinePlayers().forEach(p -> getPlayersData().put(p.getUniqueId(), new PlayerData(p, this)));
    }

    public void startGame(MapData mapData) {
        if (state.hasGameStart()) return;

        getGameConfig().setBuildMode(false);
        new StartRun(pl, this, mapData);

        soundData.initSong(SongEnum.INGAME);
    }

    public void stopGame() {
        if (!state.hasGameStart()) return;

        InGameState inGameState = (InGameState) state;
        inGameState.reset();

        setGameState(new LobbyState(this), true);
        LobbyState lobbyState = (LobbyState) state;

        ItemStack book = BookFactory.buildResumeBook(inGameState);
        lobbyState.setBookResume(book);

        String winnerMsg = getGameConfig().getGameEnd().getWinnerMessage(inGameState);

        Bukkit.getOnlinePlayers().forEach(p -> {
            p.sendTitle("§eFin de partie !", ChatColor.translateAlternateColorCodes('&', winnerMsg == null ? "" : winnerMsg), 10, 70, 20);
            p.playSound(p.getLocation(), Sound.ENTITY_WITHER_DEATH, 0.25F, 1F);
            lobbyState.initPlayer(p);
            p.openBook(book);
            gameRun.sendStats(inGameState, p);

            EntityClass entityClass = EntityManager.getInstance().getClassPlayer(p.getUniqueId());

            if (entityClass != null && !entityClass.isPlayableClass()) {
                EntityManager.getInstance().removeClassPlayer(p.getUniqueId());
            }
        });

        stopRunnable();
        soundData.initSong(SongEnum.MENU);
    }

    public void createRunnable() {
        this.itemEffectRun = new ItemRun(pl, this);
        this.gameRun = new GameRun(this);
    }

    public void stopRunnable() {
        this.itemEffectRun.cancel();
        this.gameRun.cancel();
    }

    public void setGameState(GameState newState, boolean initPlayer) {
        HandlerList.unregisterAll(state);
        this.state = newState;
        this.pl.getServer().getPluginManager().registerEvents(newState, pl);

        if (initPlayer) {
            Bukkit.getOnlinePlayers().forEach(newState::initPlayer);
        }
    }

    public ItemRun getItemEffectRun() {
        return itemEffectRun;
    }

    public GameState getState() {
        return state;
    }

    public GameConfig getGameConfig() {
        return config;
    }

    public MSSB getPlugin() {
        return pl;
    }

    public void checkGameOver() {
        if (!getState().hasGameStart()) return;
        if (!getGameConfig().getGameEnd().isGameOver((InGameState) getState())) return;

        stopGame();
    }

    public Map<UUID, PlayerData> getPlayersData() {
        return playersData;
    }

    public SoundData getSoundData() {
        return soundData;
    }
}
