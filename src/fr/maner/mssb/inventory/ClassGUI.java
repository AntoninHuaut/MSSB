package fr.maner.mssb.inventory;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import fr.maner.mssb.entity.EntityClass;
import fr.maner.mssb.entity.EntityManager;
import fr.maner.mssb.factory.ItemFactory;
import fr.maner.mssb.game.GameData;
import fr.maner.mssb.inventory.init.InvClickData;
import fr.maner.mssb.inventory.init.InvGUI;
import fr.maner.mssb.type.state.LobbyState;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class ClassGUI extends InvGUI {

	public ClassGUI(GameData gameData) {
		super(27, "§eMenu de sélection de classe", gameData);
	}

	@Override
	public void initContent() {
		setItem(22, new ItemFactory(Material.BARRIER).setName("&cQuitter votre classe").build(), this::removeClass);

		List<EntityClass> entityList = EntityManager.getInstance().getEntityList(getGameData());

		for (int i = 0; i < entityList.size(); i++) {
			EntityClass entityClass = entityList.get(i);
			setItem(i, entityClass.getItemDisplay(), p -> selectClass(p, entityClass));
		}
	}

	private void removeClass(InvClickData clickData) {
		Player p = clickData.getPlayer();

		if(EntityManager.getInstance().removeClassPlayer(p.getUniqueId())) {
			p.sendMessage(" §6» §eVous avez quitté votre classe");
			p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.75F, 1F);
			updateUserWithClass();
		}
		else {
			p.sendMessage(" §6» §cVous n'avez pas de classe");
			p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_HIT, 0.75F, 1F);
		}
	}

	private void selectClass(InvClickData clickData, EntityClass entityClass) {
		Player p = clickData.getPlayer();
		EntityClass currentClass = EntityManager.getInstance().getClassPlayer(p.getUniqueId());

		if (entityClass.equals(currentClass)) {
			p.sendMessage(" §6» §cVous avez déjà cette classe");
			p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_HIT, 0.75F, 1F);
			return;
		}

		EntityManager.getInstance().setClassPlayer(p.getUniqueId(), entityClass);
		p.sendMessage(String.format(" §6» §eVous avez choisi la classe %s%s", entityClass.getColor(), entityClass.getName()));
		p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.75F, 1F);
		p.closeInventory();

		for (Player other : Bukkit.getOnlinePlayers())
			other.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(String.format("§9%s §3a choisi la classe §9%s", p.getName(), entityClass.getName())));

		updateUserWithClass();
	}

	public void updateUserWithClass() {
		if(getGameData().getState().hasGameStart()) return;

		((LobbyState) getGameData().getState()).updateUserWithClass();
	}
}