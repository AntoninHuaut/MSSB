package fr.maner.mssb.game.data;

import com.xxmicloxx.NoteBlockAPI.model.FadeType;
import com.xxmicloxx.NoteBlockAPI.model.Playlist;
import com.xxmicloxx.NoteBlockAPI.model.RepeatMode;
import com.xxmicloxx.NoteBlockAPI.model.SoundCategory;
import com.xxmicloxx.NoteBlockAPI.model.playmode.MonoMode;
import com.xxmicloxx.NoteBlockAPI.songplayer.Fade;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import fr.maner.mssb.game.GameData;
import fr.maner.mssb.utils.songs.SongConfig.SongEnum;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Random;

public class SoundData {

    private final Random random = new Random();
    private final GameData gameData;
    private RadioSongPlayer rsp;

    public SoundData(GameData gameData) {
        this.gameData = gameData;

        initSong(SongEnum.MENU);
    }

    public void addPlayer(Player p) {
        this.rsp.addPlayer(p);
    }

    public void initSong(SongEnum song) {
        clearSong();

        Playlist playlist = gameData.getGameConfig().getSongConfig().getPlaylist().get(song);

        rsp = new RadioSongPlayer(playlist);
        rsp.setRandom(true);

        rsp.playSong(random.nextInt(playlist.getSongList().size()));

        rsp.setRepeatMode(RepeatMode.ALL);
        rsp.setCategory(SoundCategory.VOICE);
        rsp.setChannelMode(new MonoMode());
        rsp.setPlaying(true);

        setFade(rsp.getFadeIn(), 80);
        setFade(rsp.getFadeOut(), 40);

        Bukkit.getOnlinePlayers().forEach(this::addPlayer);
    }

    private void setFade(Fade fade, int fadeValue) {
        fade.setType(FadeType.LINEAR);
        fade.setFadeDuration(fadeValue);
    }

    private void clearSong() {
        if (rsp == null) return;

        SongEndRun songEndRun = new SongEndRun(rsp);
        int taskId = Bukkit.getScheduler().runTaskTimer(gameData.getPlugin(), songEndRun, 1L, 1L).getTaskId();
        songEndRun.setTaskId(taskId);
    }

    private class SongEndRun implements Runnable {

        private int taskId;
        private RadioSongPlayer oldRsp;

        private int tick;
        private int fadeDuration;

        public SongEndRun(RadioSongPlayer oldRsp) {
            this.oldRsp = oldRsp;
            this.fadeDuration = oldRsp.getFadeOut().getFadeDuration();
            this.tick = this.fadeDuration;
        }

        @Override
        public void run() {
            tick--;

            if (tick == 0) {
                oldRsp.setPlaying(false);
                Bukkit.getScheduler().cancelTask(taskId);
            } else {
                oldRsp.setVolume((byte) ((byte) 100 * tick / fadeDuration));
            }
        }

        public void setTaskId(int taskId) {
            this.taskId = taskId;
        }
    }
}
