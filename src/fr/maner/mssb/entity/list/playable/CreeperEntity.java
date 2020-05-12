package fr.maner.mssb.entity.list.playable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import fr.maner.mssb.entity.EntityManager;
import fr.maner.mssb.factory.ItemFactory;
import fr.maner.mssb.factory.armor.ArmorFactory.ArmorType;
import fr.maner.mssb.factory.armor.LeatherArmorFactory;
import fr.maner.mssb.game.GameData;
import fr.maner.mssb.game.IGPlayerData;
import fr.maner.mssb.runnable.itemeffect.ItemData;
import fr.maner.mssb.type.state.InGameState;
import fr.maner.mssb.utils.Heads;

public class CreeperEntity extends PlayableEntity {

	private static final String COLOR = "§a";
	private static final String NAME = "Creeper";
	private ItemStack explosive = new ItemFactory(Material.CREEPER_SPAWN_EGG).setName("&cExplosif").build();

	public CreeperEntity(GameData gameData) {
		super(gameData, COLOR, NAME, Heads.CHARGED_CREEPER);

		setWeapons(new ItemFactory(Material.GUNPOWDER).setAttackDamage(getWeaponDamage()).setName("&aPoudre répulsive")
				.addEnchantment(Enchantment.KNOCKBACK, 1).build(), new ItemFactory(explosive.clone()).setAmount(2).build());
		setArmors(Arrays.asList(new LeatherArmorFactory(ArmorType.LEATHER_BOOTS, Color.fromRGB(12, 208, 18)).build(),
				new LeatherArmorFactory(ArmorType.LEATHER_LEGGINGS, Color.fromRGB(12, 208, 18)).build(),
				new LeatherArmorFactory(ArmorType.LEATHER_CHESTPLATE, Color.fromRGB(12, 208, 18)).build()));
	}

	@Override
	protected double getWeaponDamage() {
		return 2.5;
	}

	@Override
	public void initEntity() {
		List<ItemData> itemDataList = new ArrayList<ItemData>();
		itemDataList.add(new ItemData(explosive, 5, 6));

		getGameData().getItemEffectRun().addItemStackGive(this, itemDataList);
	}

	@EventHandler
	public void onPlayerSpawnExplosive(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		PlayableEntity playable = EntityManager.getInstance().getPlayableClassPlayer(p.getUniqueId());

		if (playable == null || !(playable instanceof CreeperEntity))
			return;

		ItemStack inHand = p.getInventory().getItemInMainHand();
		if (!inHand.isSimilar(explosive))
			return;
		
		e.setCancelled(true);

		if (inHand.getAmount() > 1)
			inHand.setAmount(inHand.getAmount() - 1);
		else
			p.getInventory().setItemInMainHand(new ItemStack(Material.AIR));

		Location loc = e.getClickedBlock() == null ? p.getLocation() : e.getClickedBlock().getLocation().add(0, 1, 0);
		spawnExplosive(p, loc);
	}
	
	@EventHandler
	public void onEntityDamageByCreeper(EntityDamageByEntityEvent e) {
		if (e.isCancelled()) return;
		
		Entity damager = e.getDamager();
		Entity victim = e.getEntity();
		if (!damager.getType().equals(EntityType.CREEPER) || !victim.getType().equals(EntityType.PLAYER)) return;
		
		Creeper creeper = (Creeper) damager;		
		if (!creeper.hasMetadata("owner")) return;
		
		Player pVic = (Player) victim;
		for (MetadataValue metadata : creeper.getMetadata("owner")) {			
			if(metadata.asString().equals(pVic.getUniqueId().toString()))
				e.setCancelled(true);
			else {
				if(!getGameData().getState().hasGameStart()) return;
				InGameState igState = (InGameState) getGameData().getState();
				
				IGPlayerData igPlayerData = igState.getPlayersIGData().get(pVic.getUniqueId());
				if (igPlayerData != null)
					igPlayerData.setDamager(UUID.fromString(metadata.asString()));
			}
		}
	}
	
	private Random random = new Random();
	
	private void spawnExplosive(Player p, Location loc) {
		loc.setYaw(p.getLocation().getYaw());
		loc.setPitch(p.getLocation().getPitch());
		Creeper creeper = (Creeper) loc.getWorld().spawnEntity(loc, EntityType.CREEPER);
		creeper.setAware(false);
		creeper.setInvulnerable(true);
		creeper.setPowered(random.nextInt(15) == 0);
		creeper.setCanPickupItems(false);
		creeper.setCustomNameVisible(true);
		creeper.setMetadata("owner", new FixedMetadataValue(getGameData().getPlugin(), p.getUniqueId().toString()));
		
		new CreeperRun(getGameData().getPlugin(), creeper);
	}
	
	public class CreeperRun implements Runnable {
		
		private static final int TIME_TICK = 30;
		
		private Creeper creeper;
		private int taskId;
		private int currentTick;
		
		public CreeperRun(JavaPlugin pl, Creeper creeper) {
			this.creeper = creeper;
			this.taskId = Bukkit.getScheduler().runTaskTimer(pl, this, 0L, 1L).getTaskId();
			this.currentTick = 0;
		}

		@Override
		public void run() {
			double percent = (double) currentTick*100 / TIME_TICK;
			creeper.setCustomName(String.format("§c§l§kX §r§e§l%.1f %% §c§l§kX", percent));
			currentTick++;
			
			Location newLoc = creeper.getLocation();
			newLoc.setYaw(creeper.getLocation().getYaw() + 51F);
			creeper.teleport(newLoc);
			
			if (currentTick >= TIME_TICK) {
				Bukkit.getScheduler().cancelTask(taskId);
				creeper.explode();
			}
		}
	}
}
