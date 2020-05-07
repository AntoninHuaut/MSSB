package fr.maner.mssb.runnable.itemeffect;

import org.bukkit.inventory.ItemStack;

public class ItemData {

	private ItemStack is;
	private int iteration;
	private int intervalGive;
	private int maxAmount;

	public ItemData(ItemStack is, int intervalGive, int maxAmount) {
		this.is = is;
		this.intervalGive = intervalGive;
		this.maxAmount = maxAmount;
	}

	public void addIteration() {
		iteration++;

		if (iteration > intervalGive) iteration = 0;
	}

	public ItemStack getIs() {
		return is;
	}

	public int getIteration() {
		return iteration;
	}

	public int getIntervalGive() {
		return intervalGive;
	}

	public int getMaxAmount() {
		return maxAmount;
	}
}