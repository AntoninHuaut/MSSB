package fr.maner.mssb.factory.item;

import fr.maner.mssb.factory.ItemFactory;
import fr.maner.mssb.game.data.IGPlayerData;
import fr.maner.mssb.type.state.InGameState;
import fr.maner.mssb.utils.ConvertDate;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.*;
import java.util.Map.Entry;

public class BookFactory extends ItemFactory {

    private final String title;
    private final String author;

    private List<BaseComponent[]> pages;

    public BookFactory(String title, String author) {
        super(Material.WRITTEN_BOOK);

        this.title = ChatColor.translateAlternateColorCodes('&', title);
        this.author = ChatColor.translateAlternateColorCodes('&', author);
        this.pages = new ArrayList<>();
    }

    public void setPages(BaseComponent[]... pages) {
        this.pages = new ArrayList<>(Arrays.asList(pages));
    }

    public void addPage(BaseComponent[]... pages) {
        this.pages.addAll(Arrays.asList(pages));
    }

    public ItemStack build() {
        BookMeta meta = (BookMeta) item.getItemMeta();
        meta.setAuthor(author);
        meta.setTitle(title);

        meta.spigot().setPages(pages);

        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack buildResumeBook(InGameState inGameState) {
        long duration = System.currentTimeMillis() - inGameState.getStartTime();
        Map<UUID, IGPlayerData> playersData = inGameState.getPlayersIGData();

        Optional<Integer> optKills = playersData.values().stream().map(IGPlayerData::getKill).reduce(Integer::sum);
        int nbKills = optKills.orElse(0);

        BookFactory bookFactory = new BookFactory("&eRécapitulatif", "&eMSSB Team");

        bookFactory.addPage(new ComponentBuilder("\n     Récapitulatif :").color(ChatColor.DARK_RED)
                .append("\n\n  Durée : ").color(ChatColor.DARK_AQUA).append(ConvertDate.millisToShortDHMS(duration))
                .color(ChatColor.GOLD).append("\n  Kills : ").color(ChatColor.DARK_AQUA).append("" + nbKills)
                .color(ChatColor.GOLD).append("\n\n\n\nPage :").color(ChatColor.DARK_RED).append("\n\n  - 2 :")
                .color(ChatColor.DARK_AQUA).append(" TOP Kills").color(ChatColor.GOLD).append("\n  - 3 :")
                .color(ChatColor.DARK_AQUA).append(" TOP Survivants").color(ChatColor.GOLD).create());

        ComponentBuilder topKills = new ComponentBuilder("        TOP Kills : \n").color(ChatColor.DARK_RED);
        List<Entry<UUID, IGPlayerData>> topKillsEntry = playersData.entrySet().stream().sorted(BookFactory::sortByKill).toList();
        for (int i = 0; i < 10 && i < topKillsEntry.size(); i++) {
            Entry<UUID, IGPlayerData> entry = topKillsEntry.get(i);
            topKills.append("\n " + Bukkit.getOfflinePlayer(entry.getKey()).getName() + " : ").color(ChatColor.DARK_AQUA)
                    .append("" + entry.getValue().getKill()).color(ChatColor.GOLD);
        }
        bookFactory.addPage(topKills.create());

        ComponentBuilder topDeaths = new ComponentBuilder("   TOP Survivants : \n").color(ChatColor.DARK_RED);
        topDeaths.append("TOP mort décroissante").color(ChatColor.DARK_GRAY).italic(true).append("").italic(false);

        List<Entry<UUID, IGPlayerData>> topDeathsEntry = playersData.entrySet().stream().sorted(BookFactory::sortByLessDeath).toList();
        for (int i = 0; i < 10 && i < topDeathsEntry.size(); i++) {
            Entry<UUID, IGPlayerData> entry = topDeathsEntry.get(i);
            topDeaths.append("\n " + Bukkit.getOfflinePlayer(entry.getKey()).getName() + " : ").color(ChatColor.DARK_AQUA)
                    .append("" + entry.getValue().getDeath()).color(ChatColor.GOLD);
        }
        bookFactory.addPage(topDeaths.create());

        return bookFactory.build();
    }

    public static int sortByKill(Entry<UUID, IGPlayerData> e1, Entry<UUID, IGPlayerData> e2) {
        int diff = e2.getValue().getKill() - e1.getValue().getKill();
        if (diff == 0)
            return e1.getValue().getDeath() - e2.getValue().getDeath();

        return diff;
    }

    public static int sortByLessDeath(Entry<UUID, IGPlayerData> e1, Entry<UUID, IGPlayerData> e2) {
        int diff = e1.getValue().getDeath() - e2.getValue().getDeath();
        if (diff == 0)
            return e2.getValue().getKill() - e1.getValue().getKill();

        return diff;
    }

}
