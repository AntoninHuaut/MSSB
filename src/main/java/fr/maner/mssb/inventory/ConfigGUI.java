package fr.maner.mssb.inventory;

import fr.maner.mssb.entity.EntityManager;
import fr.maner.mssb.factory.ItemFactory;
import fr.maner.mssb.game.GameData;
import fr.maner.mssb.inventory.init.InvClickData;
import fr.maner.mssb.inventory.init.InvGUI;
import fr.maner.mssb.type.end.GameEnd;
import fr.maner.mssb.type.end.KillEnd;
import fr.maner.mssb.type.end.LifeEnd;
import fr.maner.mssb.type.end.TimeLimitEnd;
import fr.maner.mssb.type.game.GameType;
import fr.maner.mssb.type.game.KBMode;
import fr.maner.mssb.type.game.NormalMode;
import fr.maner.mssb.type.state.LobbyState;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Collection;

public class ConfigGUI extends InvGUI {

    public ConfigGUI(GameData gameData) {
        super(27, "§eMenu de configuration", gameData);

        ConfigGUIEnum.GTYPE_KB.setAction(clickData -> setGType(clickData, new NormalMode()));
        ConfigGUIEnum.KB_MULTIPLIER.setAction(this::setKBMultiplier);
        ConfigGUIEnum.REGEN_KB.setAction(this::setRegenKB);

        ConfigGUIEnum.GTYPE_NORMAL.setAction(clickData -> setGType(clickData, new KBMode()));
        ConfigGUIEnum.REGEN_HEALTH.setAction(this::setRegenHealth);

        ConfigGUIEnum.GEND_LIFE.setAction(clickData -> setGEnd(clickData, new KillEnd()));
        ConfigGUIEnum.NB_LIFE.setAction(this::setNbLife);

        ConfigGUIEnum.GEND_KILL.setAction(clickData -> setGEnd(clickData, new TimeLimitEnd()));
        ConfigGUIEnum.NB_KILL.setAction(this::setNbKill);

        ConfigGUIEnum.GEND_TIME_LIMIT.setAction(clickData -> setGEnd(clickData, new LifeEnd()));
        ConfigGUIEnum.TIME_LIMIT.setAction(this::setTimeLimit);

        ConfigGUIEnum.START_GAME.setAction(this::openMapSelectorGUI);

        initContent();
    }

    @Override
    public void initContent() {
        setGameTypeItems();
        setGameEndItems();

        updateUserWithClass();
    }

    public void updateUserWithClass() {
        Collection<? extends Player> playersList = Bukkit.getOnlinePlayers();
        int totalPlayers = playersList.size();
        int playersWithClass = (int) playersList.stream().filter(p -> EntityManager.getInstance().getClassPlayer(p.getUniqueId()) != null).count();

        new ItemFactory(ConfigGUIEnum.START_GAME.getItem()).setLore(String.format("&eJoueur ayant choisi leur classe : &6%d&7/&6%d", playersWithClass, totalPlayers)).build();
        setItem(ConfigGUIEnum.START_GAME);
    }

    private void openMapSelectorGUI(InvClickData action) {
        if (getGameData().getState().hasGameStart()) return;

        ((LobbyState) getGameData().getState()).openMapSelectorGUI(action.getPlayer());
    }

    //

    private void setGameTypeItems() {
        setKBMultiplier(null);
        setRegenKB(null);
        setRegenHealth(null);

        if (getGameData().getGameConfig().getGameType().isKBMode()) {
            setItem(ConfigGUIEnum.GTYPE_KB);
            setItem(ConfigGUIEnum.REGEN_KB);
        } else {
            setItem(ConfigGUIEnum.KB_MULTIPLIER.getSlot(), null);
            setItem(ConfigGUIEnum.GTYPE_NORMAL);
            setItem(ConfigGUIEnum.REGEN_HEALTH);
        }
    }

    private void setGameEndItems() {
        setNbLife(null);
        setNbKill(null);
        setTimeLimit(null);

        if (getGameData().getGameConfig().getGameEnd().isKillEnd()) {
            setItem(ConfigGUIEnum.GEND_KILL);
            setItem(ConfigGUIEnum.NB_KILL);
        } else if (getGameData().getGameConfig().getGameEnd().isLifeEnd()) {
            setItem(ConfigGUIEnum.GEND_LIFE);
            setItem(ConfigGUIEnum.NB_LIFE);
        } else if (getGameData().getGameConfig().getGameEnd().isTimeLimitEnd()) {
            setItem(ConfigGUIEnum.GEND_TIME_LIMIT);
            setItem(ConfigGUIEnum.TIME_LIMIT);
        }
    }

    private void setGType(InvClickData clickData, GameType gameType) {
        getGameData().getGameConfig().setGameType(gameType);
        setGameTypeItems();

        clickData.getPlayer().playSound(clickData.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.75F, 1F);
    }

    private void setGEnd(InvClickData clickData, GameEnd gameEnd) {
        getGameData().getGameConfig().setGameEnd(gameEnd);
        setGameEndItems();

        clickData.getPlayer().playSound(clickData.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.75F, 1F);
    }

    //

    private void setKBMultiplier(InvClickData clickData) {
        GameType gameType = getGameType();
        if (!gameType.isKBMode()) return;
        KBMode kbMode = (KBMode) gameType;

        if (clickData != null) kbMode.addKbMultiplier(getDoubleByClick(clickData));

        new ItemFactory(ConfigGUIEnum.KB_MULTIPLIER.getItem()).setName(String.format("&6[&b× &e%.1f&6] &3Multplicateur de dégats", kbMode.getKbMultiplier())).build();
        setItem(ConfigGUIEnum.KB_MULTIPLIER);
    }

    private void setRegenKB(InvClickData clickData) {
        GameType gameType = getGameType();
        if (!gameType.isKBMode()) return;
        KBMode kbMode = (KBMode) gameType;

        if (clickData != null) kbMode.addRegenKb(getIntByClick(clickData));

        new ItemFactory(ConfigGUIEnum.REGEN_KB.getItem()).setName(String.format("&6[&c- &e%d&6] &3Réduction KB/sec", kbMode.getRegenKb())).build();
        setItem(ConfigGUIEnum.REGEN_KB);
    }

    private void setRegenHealth(InvClickData clickData) {
        GameType gameType = getGameType();
        if (gameType.isKBMode()) return;
        NormalMode normalMode = (NormalMode) gameType;

        if (clickData != null) normalMode.addRegenHealth(getDoubleByClick(clickData));

        new ItemFactory(ConfigGUIEnum.REGEN_HEALTH.getItem()).setName(String.format("&6[&a+ &e%.1f&6] &3Régen pv/sec", normalMode.getRegenHealth())).build();
        setItem(ConfigGUIEnum.REGEN_HEALTH);
    }

    //

    private void setTimeLimit(InvClickData clickData) {
        GameEnd gameEnd = getGameEnd();
        if (!gameEnd.isTimeLimitEnd()) return;
        TimeLimitEnd timeLimitEnd = (TimeLimitEnd) gameEnd;

        if (clickData != null) timeLimitEnd.addTimeLimit(getDoubleByClick(clickData));

        new ItemFactory(ConfigGUIEnum.TIME_LIMIT.getItem()).setName(String.format("&6[&e%.1f&6] &3Limitation de temps (minute)", timeLimitEnd.getTimeLimit())).build();
        setItem(ConfigGUIEnum.TIME_LIMIT);
    }

    private void setNbKill(InvClickData clickData) {
        GameEnd gameEnd = getGameEnd();
        if (!gameEnd.isKillEnd()) return;
        KillEnd killEnd = (KillEnd) gameEnd;

        if (clickData != null) killEnd.addKill(getIntByClick(clickData));

        new ItemFactory(ConfigGUIEnum.NB_KILL.getItem()).setName(String.format("&6[&e%d&6] &3Nombre de kill(s)", killEnd.getNBKill())).build();
        setItem(ConfigGUIEnum.NB_KILL);
    }

    private void setNbLife(InvClickData clickData) {
        GameEnd gameEnd = getGameEnd();
        if (!gameEnd.isLifeEnd()) return;
        LifeEnd lifeEnd = (LifeEnd) gameEnd;

        if (clickData != null) lifeEnd.addLife(getIntByClick(clickData));

        new ItemFactory(ConfigGUIEnum.NB_LIFE.getItem()).setName(String.format("&6[&e%d&6] &3Nombre de vie(s)", lifeEnd.getNBLife())).build();
        setItem(ConfigGUIEnum.NB_LIFE);
    }

    private double getDoubleByClick(InvClickData clickData) {
        double doubleValue;
        if (clickData.isLeftClick()) doubleValue = clickData.isShiftClick() ? 0.5 : 1.0;
        else if (clickData.isRightClick()) doubleValue = clickData.isShiftClick() ? -0.5 : -1.0;
        else doubleValue = 0.1;

        clickData.getPlayer().playSound(clickData.getPlayer().getLocation(), Sound.BLOCK_ANVIL_HIT, 0.75F, 1F);

        return doubleValue;
    }

    private int getIntByClick(InvClickData clickData) {
        int intValue = 0;
        if (clickData.isLeftClick()) intValue = clickData.isShiftClick() ? 1 : 5;
        else if (clickData.isRightClick()) intValue = clickData.isShiftClick() ? -1 : -5;

        clickData.getPlayer().playSound(clickData.getPlayer().getLocation(), Sound.BLOCK_ANVIL_HIT, 0.75F, 1F);

        return intValue;
    }

    private void setItem(ConfigGUIEnum configEnum) {
        setItem(configEnum.getSlot(), configEnum.getItem(), configEnum.getAction());
    }

    private GameType getGameType() {
        return getGameData().getGameConfig().getGameType();
    }

    private GameEnd getGameEnd() {
        return getGameData().getGameConfig().getGameEnd();
    }
}