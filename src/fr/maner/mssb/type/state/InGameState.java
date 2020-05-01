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
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import fr.maner.mssb.entity.EntityClass;
import fr.maner.mssb.entity.EntityManager;
import fr.maner.mssb.entity.PlayableEntity;
import fr.maner.mssb.entity.list.SpectatorEntity;
import fr.maner.mssb.game.GameData;
import fr.maner.mssb.game.IGPlayerData;
import fr.maner.mssb.utils.map.MapData;

public class InGameState extends GameState {

	private final long TIME_KILL = 3500; // ms

	private MapData mapData;
	private HashMap<UUID, IGPlayerData> playersIGData = new HashMap<UUID, IGPlayerData>();

	private long startTime;

	public InGameState(GameData gameData, MapData mapData) {
		super(gameData);

		this.mapData = mapData;
		initState();
	}

	public void initState() {
		this.startTime = System.currentTimeMillis();
		Bukkit.getOnlinePlayers().forEach(p -> {
			PlayableEntity playableClass = EntityManager.getInstance().getPlayableClassPlayer(p.getUniqueId());

			if (playableClass != null)
				playersIGData.put(p.getUniqueId(), new IGPlayerData());
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
		entityClass.teleportOnMap(p);
	}

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player && e.getDamager() instanceof Player)
			playersIGData.get(e.getEntity().getUniqueId()).setDamager(e.getDamager().getUniqueId());
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		Player victim = e.getEntity();
		UUID victimUUID = victim.getUniqueId();
		IGPlayerData victimData = playersIGData.get(victimUUID);
		
		if (victimData == null) return;

		UUID killerUUID = victimData.getLastDamager();

		victimData.addDeath();

		boolean isKillByPlayer = false;

		if (killerUUID != null) {
			IGPlayerData killerData = playersIGData.get(killerUUID);

			if (killerData != null) {
				boolean isKillValid = killerData.getLastDamage() + TIME_KILL > System.currentTimeMillis();

				if (isKillValid) {
					killerData.addKill();
					isKillByPlayer = true;

					e.setDeathMessage(String.format("§c✖  §6%s §ea été tué par §6%s", e.getEntity().getName(),
							Bukkit.getPlayer(killerUUID).getName()));
				}
			}
		}

		if (!isKillByPlayer)
			e.setDeathMessage(String.format("§c✖  §6%s §eest mort", e.getEntity().getName()));

		getGameData().checkGameOver();
	}

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent e) {
		e.setRespawnLocation(mapData.getLoc());
		Bukkit.getScheduler().runTaskLater(getGameData().getPlugin(), () -> initPlayer(e.getPlayer()), 1);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerDamage(EntityDamageEvent e) {
		if (e.isCancelled())
			return;

		Entity ent = e.getEntity();

		if (!(ent instanceof Player))
			return;

		if (e.getCause().equals(EntityDamageEvent.DamageCause.VOID)) {
			((Player) ent).setHealth(0.0D);
			e.setCancelled(true);
		} else
			getGameData().getGameConfig().getGameType().modifyDamage(e);
	}

	@EventHandler
	public void onPlayerFightPlayer(EntityDamageByEntityEvent e) {
		if (!(e.getEntity() instanceof Player) || !(e.getDamager() instanceof Player))
			return;

		getGameData().getGameConfig().getGameType().setPlayerDamage(e);
	}

	@Override
	public void playerBelowYLimit(Player p) {
		p.teleport(p.getLocation().subtract(0, 200, 0));
	}

	@Override
	public void onPlayerJoin(Player p) {
		Bukkit.getScheduler().runTaskLater(getGameData().getPlugin(), () -> initPlayer(p), 1L);
	}

	public MapData getMapData() {
		return mapData;
	}

	@Override
	public int getMinY() {
		return mapData.getMinY();
	}

	public HashMap<UUID, IGPlayerData> getPlayersIGData() {
		return playersIGData;
	}

	public long getStartTime() {
		return startTime;
	}
}
