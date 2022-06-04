package fr.maner.mssb.type.state;

import fr.maner.mssb.entity.EntityClass;
import fr.maner.mssb.entity.EntityManager;
import fr.maner.mssb.entity.list.RandomEntity;
import fr.maner.mssb.game.GameData;
import fr.maner.mssb.game.data.IGPlayerData;
import fr.maner.mssb.game.data.PlayerData;
import fr.maner.mssb.utils.map.MapData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InGameState extends GameState {

    private static final long TIME_KILL = 5000; // ms

    private final BossBar bossBar;

    private final MapData mapData;
    private final Map<UUID, IGPlayerData> playersIGData = new HashMap<>();

    private long startTime;

    public InGameState(GameData gameData, MapData mapData) {
        super(gameData);

        this.mapData = mapData;
        this.bossBar = Bukkit.createBossBar("§6Objectif §7: §e" + gameData.getGameConfig().getGameEnd().getGoalMessage(), BarColor.BLUE, BarStyle.SEGMENTED_10);
        this.bossBar.setVisible(true);

        initState();
    }

    public void initState() {
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void initPlayer(Player p) {
        super.initPlayer(p);
        bossBar.addPlayer(p);

        EntityClass entityClass = EntityManager.getInstance().getClassPlayer(p.getUniqueId());

        if (entityClass == null) {
            RandomEntity randomEntity = new RandomEntity(getGameData());
            entityClass = randomEntity;
            EntityManager.getInstance().setClassPlayer(p.getUniqueId(), randomEntity);
        }

        getGameData().getPlayersData().get(p.getUniqueId()).createBoard();

        entityClass.initPlayer(p).teleportOnMap(p);
        Bukkit.getOnlinePlayers().forEach(pGet -> pGet.showPlayer(getGameData().getPlugin(), p));
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        Player pDamager = null;
        if (e.getDamager().getType().equals(EntityType.PLAYER)) {
            pDamager = (Player) e.getDamager();
        } else if (e.getDamager() instanceof Projectile proj && proj.getShooter() instanceof Player shooter) {
            pDamager = shooter;
        }

        if (pDamager == null) return;

        if (e.getEntity().getType().equals(EntityType.PLAYER)) {
            playersIGData.get(e.getEntity().getUniqueId()).setDamager(pDamager.getUniqueId());
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player victim = e.getEntity();
        resetFire(victim);
        UUID victimUUID = victim.getUniqueId();
        IGPlayerData victimData = playersIGData.get(victimUUID);

        if (victimData == null) return;

        UUID killerUUID = victimData.getLastDamager();

        victimData.addDeath();

        boolean isKillByPlayer = false;
        boolean isKillValid = victimData.getLastDamage() + TIME_KILL > System.currentTimeMillis();

        if (killerUUID != null && isKillValid) {
            IGPlayerData killerData = playersIGData.get(killerUUID);

            if (killerData != null) {
                killerData.addKill();
                isKillByPlayer = true;

                OfflinePlayer offKiller = Bukkit.getOfflinePlayer(killerUUID);
                e.setDeathMessage(String.format("§c☠ %s §a⚔ %s", e.getEntity().getName(), offKiller.getName()));

                if (offKiller.isOnline()) {
                    Player killer = offKiller.getPlayer();
                    killer.playSound(killer.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 2F, 0.5F);
                }
            }
        }

        if (!isKillByPlayer) {
            e.setDeathMessage(String.format("§c☠ %s", e.getEntity().getName()));
        }

        getGameData().getGameConfig().getGameEnd().checkPlayer(getGameData(), this, victim);
        getGameData().checkGameOver();
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        Bukkit.getOnlinePlayers().forEach(pGet -> pGet.hidePlayer(getGameData().getPlugin(), e.getPlayer())); // Hide player before random tp
        e.setRespawnLocation(mapData.getLoc());

        Bukkit.getScheduler().runTaskLater(getGameData().getPlugin(), () -> initPlayer(e.getPlayer()), 1);
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if (e.isCancelled()) return;

        Entity ent = e.getEntity();

        if (!(ent instanceof Player)) return;

        if (e.getCause().equals(EntityDamageEvent.DamageCause.VOID)) {
            EntityClass entityClass = EntityManager.getInstance().getClassPlayer(ent.getUniqueId());
            if (entityClass == null) return;

            entityClass.fallInVoid(e);
        } else {
            getGameData().getGameConfig().getGameType().modifyDamage(e);
        }
    }

    @EventHandler
    public void onPlayerFightPlayer(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player)) return;

        getGameData().getGameConfig().getGameType().modifyDamageByEntity(e);

        if (e.getDamager().getType().equals(EntityType.PLAYER) || e.getCause().equals(DamageCause.PROJECTILE)) {
            getGameData().getGameConfig().getGameType().getDamageByPlayerOrProjectile(e);

        }
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

    @Override
    public void reset() {
        this.bossBar.removeAll();
        getGameData().getPlayersData().values().forEach(PlayerData::deleteScoreboard);
    }

    private void resetFire(Player p) {
        if (p.getFireTicks() > 0) {
            p.setFireTicks(0);
            p.setVisualFire(true);
            Bukkit.getScheduler().runTaskLater(getGameData().getPlugin(), () -> p.setVisualFire(false), 1);
        }
    }

    public Map<UUID, IGPlayerData> getPlayersIGData() {
        return playersIGData;
    }

    public long getStartTime() {
        return startTime;
    }

    public BossBar getBossBar() {
        return bossBar;
    }
}
