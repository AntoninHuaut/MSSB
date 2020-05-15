package fr.maner.mssb.game.data;

import org.bukkit.entity.Player;

import com.xxmicloxx.NoteBlockAPI.model.FadeType;
import com.xxmicloxx.NoteBlockAPI.model.RepeatMode;
import com.xxmicloxx.NoteBlockAPI.model.SoundCategory;
import com.xxmicloxx.NoteBlockAPI.songplayer.Fade;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;

import fr.maner.mssb.game.GameData;
import fr.maner.mssb.utils.songs.SongConfig.SongEnum;

public class SoundData {

	private GameData gameData;
	private RadioSongPlayer rsp;

	public SoundData(GameData gameData) {
		this.gameData = gameData;
		
		initSong(SongEnum.MENU);
	}
	
	public void addPlayer(Player p) {
		this.rsp.addPlayer(p);;
	}

	public void initSong(SongEnum song) {
		clearSong();

		rsp = new RadioSongPlayer(gameData.getGameConfig().getSongConfig().getPlaylist().get(song));
		rsp.setRepeatMode(RepeatMode.ALL);
		rsp.setRandom(true);
		rsp.setCategory(SoundCategory.VOICE);
		rsp.setPlaying(true);
		
		Fade fadeIn = rsp.getFadeIn();
		fadeIn.setType(FadeType.LINEAR);
		fadeIn.setFadeDuration(60);
	}

	private void clearSong() {
		if (rsp == null) return;
	}
}
