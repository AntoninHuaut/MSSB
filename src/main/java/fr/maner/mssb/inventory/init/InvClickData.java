package fr.maner.mssb.inventory.init;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InvClickData {

    private final Player player;
    private final boolean isLeftClick;
    private final boolean isRightClick;
    private final boolean isShiftClick;

    public InvClickData(InventoryClickEvent e) {
        this.player = (Player) e.getWhoClicked();
        this.isLeftClick = e.isLeftClick();
        this.isRightClick = e.isRightClick();
        this.isShiftClick = e.isShiftClick();
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isLeftClick() {
        return isLeftClick;
    }

    public boolean isRightClick() {
        return isRightClick;
    }

    public boolean isShiftClick() {
        return isShiftClick;
    }

    @Override
    public String toString() {
        return "InvClickData [player=" + player + ", isLeftClick=" + isLeftClick + ", isRightClick=" + isRightClick
                + ", isShiftClick=" + isShiftClick + "]";
    }
}
