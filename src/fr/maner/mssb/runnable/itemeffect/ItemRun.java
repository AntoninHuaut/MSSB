package fr.maner.mssb.runnable.itemeffect;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

import fr.maner.mssb.entity.EntityManager;
import fr.maner.mssb.entity.list.playable.PlayableEntity;
import fr.maner.mssb.game.GameData;

public class ItemRun implements Runnable {

	private HashMap<ItemStack, List<PotionEffect>> potionsEffects = new HashMap<ItemStack, List<PotionEffect>>();
	private HashMap<PlayableEntity, List<ItemData>> itemstackGive = new HashMap<PlayableEntity, List<ItemData>>();

	private GameData gameData;
	private int taskId;

	public ItemRun(JavaPlugin pl, GameData gameData) {
		this.taskId = Bukkit.getScheduler().runTaskTimer(pl, this, 20, 10).getTaskId();
		this.gameData = gameData;
	}

	@Override
	public void run() {
		//
		Set<ItemStack> items = potionsEffects.keySet();

		Bukkit.getOnlinePlayers().stream()
		.filter(p -> items.contains(p.getInventory().getItemInMainHand()))
		.forEach(p -> p.addPotionEffects(potionsEffects.get(p.getInventory().getItemInMainHand())));
		
		//

		EntityManager.getInstance().getPlayableEntityList(gameData).forEach(playable -> {
			if (!itemstackGive.containsKey(playable)) return;
			itemstackGive.get(playable).forEach(itemData -> itemData.addIteration());
		});

		Bukkit.getOnlinePlayers().stream()
		.forEach(p -> {
			PlayableEntity playable = EntityManager.getInstance().getPlayableClassPlayer(p.getUniqueId());
			if (playable == null || !itemstackGive.containsKey(playable)) return;

			for (ItemData itemData : itemstackGive.get(playable))
				if (itemData.getIteration() == 0) {
					if (itemData.getMaxAmount() > 0 && p.getInventory().containsAtLeast(itemData.getIs(), itemData.getMaxAmount())) continue;
					
					p.getInventory().addItem(itemData.getIs());
				}
		});
	}

	public void cancel() {
		Bukkit.getScheduler().cancelTask(taskId);
	}

	public void addPotionEffect(ItemStack is, PotionEffect... potionEffect) {
		potionsEffects.put(is, Arrays.asList(potionEffect));
	}

	public void addItemStackGive(PlayableEntity playable, List<ItemData> itemDatas) {
		itemstackGive.put(playable, itemDatas);
	}
}
