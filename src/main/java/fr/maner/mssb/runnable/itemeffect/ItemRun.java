package fr.maner.mssb.runnable.itemeffect;

import fr.maner.mssb.entity.EntityManager;
import fr.maner.mssb.entity.list.playable.PlayableEntity;
import fr.maner.mssb.game.GameData;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

import java.util.*;

public class ItemRun implements Runnable {

    private final Map<ItemStack, List<PotionEffect>> potionsEffects = new HashMap<>();
    private final Map<PlayableEntity, List<ItemData>> itemStackGive = new HashMap<>();

    private final GameData gameData;
    private final int taskId;

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
            if (!itemStackGive.containsKey(playable)) return;

            itemStackGive.get(playable).forEach(ItemData::addIteration);
        });

        Bukkit.getOnlinePlayers().forEach(p -> {
            PlayableEntity playable = EntityManager.getInstance().getPlayableClassPlayer(p.getUniqueId());
            if (playable == null || !itemStackGive.containsKey(playable)) return;

            for (ItemData itemData : itemStackGive.get(playable))
                if (itemData.getIteration() == 0) {
                    if (itemData.getMaxAmount() > 0 && p.getInventory().containsAtLeast(itemData.getIs(), itemData.getMaxAmount()))
                        continue;

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
        itemStackGive.put(playable, itemDatas);
    }
}
