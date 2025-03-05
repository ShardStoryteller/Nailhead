package nailheadbot.voice;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class PlayerManager {
    private static final Logger logger = LoggerFactory.getLogger(PlayerManager.class);
    private static PlayerManager INSTANCE;

    private final Map<Long, MusicManager> managers;
    private final AudioPlayerManager audioPlayerManager;

    public PlayerManager() {
        managers = new HashMap<>();
        audioPlayerManager = new DefaultAudioPlayerManager();

        AudioSourceManagers.registerRemoteSources(audioPlayerManager);
        AudioSourceManagers.registerLocalSource(audioPlayerManager);
    }

    public MusicManager getMusicManager(Guild guild) {
        return this.managers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            final MusicManager guildMusicManager = new MusicManager(audioPlayerManager);

            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());

            return guildMusicManager;
        });
    }

    public void loadAndPlay(MessageReceivedEvent event, String url) {
        final MusicManager musicManager = this.getMusicManager(event.getGuild());

        this.audioPlayerManager.loadItemOrdered(musicManager, url, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                musicManager.scheduler.queue(audioTrack);
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {

            }

            @Override
            public void noMatches() {
                logger.warn("No matches!");
            }

            @Override
            public void loadFailed(FriendlyException ex) {
                logger.warn("Load failed!", ex);
            }
        });
    }

    public void loadAndPlaySilent(MessageReceivedEvent event, String url) {
        logger.info("Silently playing a track");
        final MusicManager musicManager = this.getMusicManager(event.getGuild());

        this.audioPlayerManager.loadItemOrdered(musicManager, url, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                musicManager.scheduler.queue(audioTrack);
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {

            }

            @Override
            public void noMatches() {
                logger.warn("No matches!");
            }

            @Override
            public void loadFailed(FriendlyException ex) {
                logger.warn("Load failed!", ex);
            }
        });
    }

    public void skipTrack(MessageReceivedEvent event) {
        final MusicManager musicManager = this.getMusicManager(event.getGuild());
        MessageChannel channel = event.getChannel();

        musicManager.scheduler.skip(channel);
    }

    public void pause(MessageReceivedEvent event) {
        final MusicManager musicManager = this.getMusicManager(event.getGuild());

        musicManager.scheduler.pause();
    }

    public static PlayerManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }
        return INSTANCE;
    }
}
