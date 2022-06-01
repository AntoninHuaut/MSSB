package fr.maner.mssb.type.end;

import fr.maner.mssb.entity.EntityManager;
import fr.maner.mssb.entity.list.SpectatorEntity;
import fr.maner.mssb.game.GameData;
import fr.maner.mssb.game.data.IGPlayerData;
import fr.maner.mssb.type.state.InGameState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Map.Entry;
import java.util.UUID;

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
        if (this.nbLife < 1) {
            setNBLife(1);
        }
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
            if (playerData != null) {
                p.sendTitle(ChatColor.translateAlternateColorCodes('&', "&cEliminé"),
                        String.format("§6§lStats : §a§l%d ⚔ §8§l| §c§l%d ☠", playerData.getKill(), playerData.getDeath()),
                        10, 70, 20);
            }

            inGameState.initPlayer(p);
        }
    }

    @Override
    public boolean checkGameOver(InGameState inGameState, int nbPlayablePlayers) {
        return nbPlayablePlayers <= 1;
    }

    @Override
    public String getWinnerMessage(InGameState inGameState) {
        var winner = inGameState.getPlayersIGData().entrySet().stream().min(this::sortByLessDeath);
        return winner
                .map(uuidPlayerDataEntry -> String.format("&6%s &egagne le match !",
                        Bukkit.getOfflinePlayer(uuidPlayerDataEntry.getKey()).getName()))
                .orElse(null);

    }

    public int sortByLessDeath(Entry<UUID, IGPlayerData> e1, Entry<UUID, IGPlayerData> e2) {
        return e1.getValue().getDeath() - e2.getValue().getDeath();
    }

    @Override
    public String getConfigMessage() {
        return String.format("Le dernier encore en vie §7(§6%d §evie(s)§7)", getNBLife());
    }

    @Override
    public String getGoalMessage() {
        return String.format("Être le dernier survivant §7(§6%d §evie(s)§7)", getNBLife());
    }

    @Override
    public double getProgress(InGameState inGameState) {
        int totalLife = getNbPlayablePlayerAtStart() * getNBLife();
        int totalDeath = inGameState.getPlayersIGData().values().stream().map(IGPlayerData::getDeath).reduce(Integer::sum).orElse(0);
        int remainLife = totalLife - totalDeath;

        if (totalLife == 0) return 1.0D;

        return 1.0D - ((double) remainLife / totalLife);
    }
}
