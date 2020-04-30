package fr.maner.mssb.runnable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

public class ItemEffectRun implements Runnable {
	
	private HashMap<ItemStack, List<PotionEffect>> potionsEffects = new HashMap<ItemStack, List<PotionEffect>>();
	
	private int taskId;
	
	public ItemEffectRun(JavaPlugin pl) {
		this.taskId = Bukkit.getScheduler().runTaskTimer(pl, this, 20, 20).getTaskId();
	}
	
	@Override
	public void run() {
		Set<ItemStack> items = potionsEffects.keySet();
		
		Bukkit.getOnlinePlayers().stream()
		.filter(p -> items.contains(p.getInventory().getItemInMainHand()))
		.forEach(p -> p.addPotionEffects(potionsEffects.get(p.getInventory().getItemInMainHand())));
	}
	
	public void cancel() {
		Bukkit.getScheduler().cancelTask(taskId);
	}
	
	public void addPotionEffect(ItemStack is, PotionEffect... potionEffect) {
		potionsEffects.put(is, Arrays.asList(potionEffect));
	}
}
