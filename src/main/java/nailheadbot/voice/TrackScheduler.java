package nailheadbot.voice;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter {
    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue;

    public TrackScheduler(AudioPlayer player) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
    }

    public void queue(AudioTrack track) {
        if (!this.player.startTrack(track, true)) {
            this.queue.offer(track);
        }
    }

    public void skip(MessageChannel channel) {
        if (player.getPlayingTrack() == null) {
            channel.sendMessage("There's nothing playing right now!").queue();
        } else {
            channel.sendMessage("Skipping the current track").queue();
            nextTrack();
        }
    }

    public void pause() {
        player.setPaused(!player.isPaused());
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
            nextTrack();
            if (queue.isEmpty()) {
                VoiceHelper.leaveWhenEmpty(this);
            }
        }
    }

    public void nextTrack() {
        this.player.startTrack(this.queue.poll(), false);
    }
}
