package fr.maner.mssb.cmd;

import fr.maner.mssb.entity.EntityClass;
import fr.maner.mssb.entity.EntityManager;
import fr.maner.mssb.game.GameData;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class MSSBCmd implements CommandExecutor, TabExecutor {

    private final String cmd;
    private final GameData gameData;
    private final HashMap<String, MSSBArgs> argsMap = new HashMap<>();

    public MSSBCmd(String cmd, GameData gameData) {
        this.cmd = cmd;
        this.gameData = gameData;

        argsMap.put("build", new MSSBArgs("Toggle on/off du mode construction", "mssb.buildmode", true, true, this::buildMode));
        argsMap.put("rtp", new MSSBArgs("Debug tp random", "mssb.rtp", true, false, this::rtp));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(buildHelp(cmd, sender));
            return true;
        }

        if (argsMap.containsKey(args[0])) {
            MSSBArgs argObj = argsMap.get(args[0]);

            if (canUseCommand(sender, argObj)) {
                argObj.getCommand().accept(sender);
            } else {
                sender.sendMessage(" §6» §cVous ne pouvez pas effectuer cette commande");
            }
        }

        return false;
    }

    private void rtp(CommandSender sender) {
        if (!gameData.getState().hasGameStart()) {
            sender.sendMessage(" §6» §cLa partie doit être lancée");
            return;
        }

        Player p = (Player) sender;
        EntityClass entityClass = EntityManager.getInstance().getClassPlayer(p.getUniqueId());

        if (entityClass == null) return;

        entityClass.teleportOnMap(p);
    }

    private void buildMode(CommandSender sender) {
        if (gameData.getState().hasGameStart()) {
            sender.sendMessage(" §6» §cImpossible de passer en mode construction, une partie est en cours");
            return;
        }

        gameData.getGameConfig().setBuildMode(!gameData.getGameConfig().isBuildMode());
        Bukkit.broadcastMessage(String.format(" §6» §eLe mode construction a été %s par %s", (gameData.getGameConfig().isBuildMode() ? "§aactivé" : "§cdésactivé"), sender.getName()));
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        List<String> tab = new ArrayList<>();

        if (args.length <= 1) {
            tab = argsMap.entrySet().stream().filter(entry -> canUseCommand(sender, entry.getValue())).map(Entry::getKey).collect(Collectors.toList());
        }

        return tab.stream().filter(value -> value.toLowerCase().startsWith(args[args.length - 1].toLowerCase())).collect(Collectors.toList());
    }

    private String buildHelp(Command cmd, CommandSender sender) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("§eListe des commandes dont vous avez accès :");

        argsMap.forEach((arg, argObj) -> {
            if (canUseCommand(sender, argObj)) {
                stringBuilder.append(String.format("\n §6» §8/§7%s §e%s §8: §7%s", cmd.getName(), arg, argObj.getDesc()));
            }
        });

        return stringBuilder.toString();
    }

    private boolean canUseCommand(CommandSender sender, MSSBArgs argObj) {
        return (argObj.isUsableByPlayer() && sender instanceof Player && sender.hasPermission(argObj.getPerm())) || (argObj.isUsableByConsole() && sender instanceof ConsoleCommandSender);
    }

    public String getCmd() {
        return cmd;
    }
}