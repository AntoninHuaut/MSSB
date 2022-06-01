package fr.maner.mssb.type.end;

import fr.maner.mssb.game.data.IGPlayerData;
import fr.maner.mssb.type.state.InGameState;
import org.bukkit.Bukkit;

import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;

public class KillEnd extends GameEnd {

    private int nbKillWin;

    public KillEnd() {
        setNBKill(10);
    }

    public void setNBKill(int nbKill) {
        if (nbKill < 1) return;
        this.nbKillWin = nbKill;
    }

    public void addKill(int nbKill) {
        this.nbKillWin += nbKill;
        if (this.nbKillWin < 1) {
            setNBKill(1);
        }
    }

    public int getNBKill() {
        return nbKillWin;
    }

    @Override
    public boolean checkGameOver(InGameState inGameState, int nbPlayablePlayers) {
        return inGameState.getPlayersIGData().entrySet().stream().anyMatch(entry -> entry.getValue().getKill() >= nbKillWin);
    }

    @Override
    public String getWinnerMessage(InGameState inGameState) {
        var winner = inGameState.getPlayersIGData().entrySet().stream().min(this::sortByKill);
        return winner
                .map(uuidPlayerDataEntry -> String.format("&6%s &egagne le match !",
                        Bukkit.getOfflinePlayer(uuidPlayerDataEntry.getKey()).getName()))
                .orElse(null);

    }

    public int sortByKill(Entry<UUID, IGPlayerData> e1, Entry<UUID, IGPlayerData> e2) {
        return e2.getValue().getKill() - e1.getValue().getKill();
    }

    @Override
    public String getConfigMessage() {
        return String.format("Le premier qui atteint §6%d §ekill(s)", getNBKill());
    }

    @Override
    public String getGoalMessage() {
        return String.format("Être le 1er à §6%d §ekill(s)", getNBKill());
    }

    @Override
    public double getProgress(InGameState inGameState) {
        var playerTopKiller = inGameState.getPlayersIGData().entrySet().stream().min(this::sortByKill);
        if (playerTopKiller.isEmpty()) return 0D;

        return (double) playerTopKiller.get().getValue().getKill() / getNBKill();
    }
}
