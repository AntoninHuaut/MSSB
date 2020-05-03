package fr.maner.mssb;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import fr.maner.mssb.cmd.MSSBCmd;
import fr.maner.mssb.game.GameData;
import fr.maner.mssb.inventory.init.InvGUIListener;
import fr.maner.mssb.listener.EntityListener;
import fr.maner.mssb.listener.PlayerListener;
import fr.maner.mssb.listener.WorldListener;

/* https://www.spigotmc.org/wiki/creating-a-gui-inventory/ */
public class MSSB extends JavaPlugin {
	
	private GameData gameData;
	
	@Override
	public void onEnable() {
		this.gameData = new GameData(this);
		
		initListeners(getServer().getPluginManager());
		
		MSSBCmd mssbCmd = new MSSBCmd("mssb", gameData);
		getCommand(mssbCmd.getCmd()).setExecutor(mssbCmd);
		getCommand(mssbCmd.getCmd()).setTabCompleter(mssbCmd);
	}
	
	@Override
	public void onDisable() {
		gameData.getState().reset();
	}

	private void initListeners(PluginManager pm) {
		pm.registerEvents(new EntityListener(), this);
		pm.registerEvents(new PlayerListener(gameData), this);
		pm.registerEvents(new WorldListener(gameData), this);
		
		pm.registerEvents(new InvGUIListener(), this);
	}

	public GameData getGameData() {
		return gameData;
	}
}
