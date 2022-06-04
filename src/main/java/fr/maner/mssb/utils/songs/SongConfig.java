package fr.maner.mssb.utils.songs;

import com.google.gson.JsonArray;
import com.xxmicloxx.NoteBlockAPI.model.Playlist;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;
import fr.maner.mssb.utils.JsonConfig;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SongConfig extends JsonConfig {

    private final Map<SongEnum, Playlist> playlist = new HashMap<>();

    public SongConfig(JavaPlugin pl) {
        super(pl, "songs/index");

        for (SongEnum songEnum : SongEnum.values()) {
            playlist.put(songEnum, getSongs(songEnum));
        }
    }

    private Playlist getSongs(SongEnum type) {
        JsonArray array = get(getJsonObject(), type.toString()).getAsJsonArray();
        Song[] songs = new Song[array.size()];

        for (int i = 0; i < array.size(); i++) {
            String songName = array.get(i).getAsString();
            copySongIfNotExist(type, songName);

            songs[i] = NBSDecoder.parse(getSongFile(type, songName));
        }

        return new Playlist(songs);
    }

    private void copySongIfNotExist(SongEnum type, String songName) {
        if (getSongFile(type, songName).exists()) return;

        pl.saveResource(getSongPath(type, songName), false);
    }

    private File getSongFile(SongEnum type, String songName) {
        return new File(pl.getDataFolder() + File.separator + getSongPath(type, songName));
    }

    private String getSongPath(SongEnum type, String songName) {
        return "songs" + File.separator + type + File.separator + songName + ".nbs";
    }

    public Map<SongEnum, Playlist> getPlaylist() {
        return playlist;
    }

    public enum SongEnum {
        INGAME, MENU, OTHERS;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }
}
