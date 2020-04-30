package fr.maner.mssb.type.state;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import fr.maner.mssb.entity.EntityClass;
import fr.maner.mssb.entity.EntityManager;
import fr.maner.mssb.entity.PlayableEntity;
import fr.maner.mssb.entity.list.SpectatorEntity;
import fr.maner.mssb.game.GameData;
import fr.maner.mssb.utils.map.MapData;

public class InGameState extends GameState {
	
	private MapData mapData;
	
	private HashMap<UUID, Integer> nbKills = new HashMap<UUID, Integer>();
	private HashMap<UUID, Integer> nbDeaths = new HashMap<UUID, Integer>();
	@SuppressWarnings("unused")
	private long currentTime;

	public InGameState(GameData gameData, MapData mapData) {
		super(gameData);
		
		this.mapData = mapData;
		initState();
	}
	
	public void initState() {
		this.currentTime = System.currentTimeMillis();		
		Bukkit.getOnlinePlayers().forEach(p -> {
			PlayableEntity playableClass = EntityManager.getInstance().getPlayableClassPlayer(p.getUniqueId());
			
			if (playableClass != null) {
				nbDeaths.put(p.getUniqueId(), 0);
				nbKills.put(p.getUniqueId(), 0);
			}
		});
		
		EntityManager.getInstance().getPlayableEntityList(getGameData()).forEach(pEntity -> pEntity.initEntity());
	}

	@Override
	public void initPlayer(Player p) {
		super.initPlayer(p);
		
		EntityClass entityClass = EntityManager.getInstance().getClassPlayer(p.getUniqueId());
		
		if (entityClass == null) {
			SpectatorEntity spec = new SpectatorEntity(getGameData());
			entityClass = spec;
			EntityManager.getInstance().setClassPlayer(p.getUniqueId(), spec);
		}
		
		entityClass.initPlayer(p);
		p.teleport(mapData.getLoc());
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerDamage(EntityDamageEvent e) {
		if (e.isCancelled()) return;

		Entity ent = e.getEntity();

		if (!(ent instanceof Player)) return;

		if (e.getCause().equals(EntityDamageEvent.DamageCause.VOID)) 
			((Player) ent).setHealth(0.0D);
		else 
			getGameData().getGameConfig().getGameType().modifyDamage(e);
	}

	@EventHandler
	public void onPlayerFightPlayer(EntityDamageByEntityEvent e) {
		if (!(e.getEntity() instanceof Player) || !(e.getDamager() instanceof Player)) return;

		getGameData().getGameConfig().getGameType().setPlayerDamage(e);
	}

	@Override
	public void playerBelowYLimit(Player p) {
		p.getLocation().setY(-100);
	}
	
	@Override
	public void onPlayerJoin(Player p) {
		Bukkit.getScheduler().runTaskLater(getGameData().getPlugin(), () -> initPlayer(p), 1L);
	}
	
	public MapData getMapData() {
		return mapData;
	}
}
