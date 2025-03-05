package nailheadbot.voice;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

public class MusicManager {
    public final AudioPlayer localPlayer;
    public final TrackScheduler scheduler;
    private final AudioPlayerSendHandler sendHandler;

    public MusicManager(AudioPlayerManager manager) {
        localPlayer = manager.createPlayer();
        scheduler = new TrackScheduler(localPlayer);
        localPlayer.addListener(scheduler);
        sendHandler = new AudioPlayerSendHandler(localPlayer);
    }

    public AudioPlayerSendHandler getSendHandler() {
        return sendHandler;
    }
}
