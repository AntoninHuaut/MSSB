package fr.maner.mssb.type.state;

import fr.maner.mssb.factory.ItemFactory;
import fr.maner.mssb.game.GameData;
import fr.maner.mssb.inventory.ClassGUI;
import fr.maner.mssb.inventory.ConfigGUI;
import fr.maner.mssb.inventory.MapGUI;
import fr.maner.mssb.utils.specialloc.SpecialLocEnum;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class LobbyState extends GameState {

    private static final int MIN_Y = 100;

    private final ClassGUI classGUI;
    private final ConfigGUI configGUI;
    private final MapGUI mapGUI;

    private final ItemStack selectorClassIS;
    private final ItemStack configGame;

    private ItemStack bookResume;

    public LobbyState(GameData gameData) {
        super(gameData);

        this.selectorClassIS = new ItemFactory(Material.COMPASS).setName("§eSélecteur de classe").build();
        this.configGame = new ItemFactory(Material.BLAZE_ROD).setName("§eConfigurer la partie").build();

        this.classGUI = new ClassGUI(gameData);
        this.configGUI = new ConfigGUI(gameData);
        this.mapGUI = new MapGUI(gameData, MapGUI.computeInvSize(gameData.getGameConfig().getMapConfig().getMapData()));
        this.mapGUI.initContent(); // constructor cannot initialize
    }

    @Override
    public void initPlayer(Player p) {
        super.initPlayer(p);

        if (p.hasPermission("mssb.config")) {
            p.getInventory().setItem(3, selectorClassIS);
            p.getInventory().setItem(5, configGame);
        } else {
            p.getInventory().setItem(4, selectorClassIS);
        }

        if (bookResume != null) {
            p.getInventory().setItem(0, bookResume);
        }

        getGameData().getGameConfig().getSpecialLogConfig().tpPlayer(p, SpecialLocEnum.LOBBY);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        e.setDeathMessage(null);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (getGameData().getGameConfig().isBuildMode()) return;

        ItemStack is = e.getItem();

        if (selectorClassIS.isSimilar(is)) classGUI.open(e.getPlayer());
        else if (configGame.isSimilar(is)) configGUI.open(e.getPlayer());
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        e.setCancelled(true);
    }

    @Override
    public void onPlayerJoin(Player p) {
        Bukkit.getScheduler().runTaskLater(getGameData().getPlugin(), () -> initPlayer(p), 1L);
    }

    @Override
    public void playerBelowYLimit(Player p) {
        initPlayer(p);
    }

    public void updateUserWithClass() {
        configGUI.updateUserWithClass();
    }

    public void openMapSelectorGUI(Player p) {
        mapGUI.open(p);
        p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_HIT, 0.75F, 1F);
    }

    @Override
    public int getMinY() {
        return MIN_Y;
    }

    public ItemStack getBookResume() {
        return bookResume;
    }

    public void setBookResume(ItemStack bookResume) {
        this.bookResume = bookResume;
    }

    @Override
    public void reset() {
    }
}
