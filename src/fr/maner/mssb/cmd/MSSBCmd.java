package fr.maner.mssb.cmd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import fr.maner.mssb.entity.EntityClass;
import fr.maner.mssb.entity.EntityManager;
import fr.maner.mssb.game.GameData;
import fr.maner.mssb.type.state.InGameState;

public class MSSBCmd implements CommandExecutor, TabExecutor {

	private String cmd;
	private GameData gameData;
	private HashMap<String, MSSBArgs> argsMap = new HashMap<String, MSSBArgs>();

	public MSSBCmd(String cmd, GameData gameData) {
		this.cmd = cmd;
		this.gameData = gameData;
		
		argsMap.put("build", new MSSBArgs("Toggle on/off du mode construction", "mssb.buildmode", true, true, sender -> buildMode(sender)));
		argsMap.put("rtp", new MSSBArgs("Debug tp random", "mssb.rtp", true, false, sender -> rtp(sender)));
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)  {
		if(args.length < 1) {
			sender.sendMessage(buildHelp(cmd, sender));
			return true;
		}
		
		if (argsMap.containsKey(args[0])) {
			MSSBArgs argObj = argsMap.get(args[0]);
			
			if (canUseCommand(sender, argObj))
				argObj.getCommand().accept(sender);
			else
				sender.sendMessage(" §6» §cVous ne pouvez pas effectuer cette commande");
		}
		
		return false;
	}
	
	private void rtp(CommandSender sender) {
		if (!(gameData.getState() instanceof InGameState)) {
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
		sender.sendMessage(String.format(" §6» §eLe mode construction a été %s", (gameData.getGameConfig().isBuildMode() ? "§aactivé" : "§cdésactivé")));
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> tab = new ArrayList<String>();

		if (args.length <= 1) 
			tab = argsMap.entrySet().stream().filter(entry -> canUseCommand(sender, entry.getValue())).map(Entry::getKey).collect(Collectors.toList());

		return tab.stream().filter(value -> value.toLowerCase().startsWith(args[args.length - 1].toLowerCase())).collect(Collectors.toList());
	}
	
	private String buildHelp(Command cmd, CommandSender sender) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("§eListe des commandes dont vous avez accès :");
		
		argsMap.forEach((arg, argObj) -> {
			if (canUseCommand(sender, argObj))
				stringBuilder.append(String.format("\n §6» §8/§7%s §e%s §8: §7%s", cmd.getName(), arg, argObj.getDesc()));
		});
		
		return stringBuilder.toString();
	}
	
	private boolean canUseCommand(CommandSender sender, MSSBArgs argObj) {
		return (argObj.isUsableByPlayer() && sender instanceof Player && sender.hasPermission(argObj.getPerm()))  ||  (argObj.isUsableByConsole() && sender instanceof ConsoleCommandSender);
	}
	
	public String getCmd() {
		return cmd;
	}
}