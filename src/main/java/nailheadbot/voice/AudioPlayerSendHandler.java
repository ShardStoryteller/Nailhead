package nailheadbot.voice;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;
import net.dv8tion.jda.api.audio.AudioSendHandler;

import java.nio.ByteBuffer;

public class AudioPlayerSendHandler implements AudioSendHandler {
    private final AudioPlayer localPlayer;
    private final ByteBuffer buff;
    private final MutableAudioFrame frame;

    public AudioPlayerSendHandler(AudioPlayer audioPlayer) {
        localPlayer = audioPlayer;
        buff = ByteBuffer.allocate(1024);
        frame = new MutableAudioFrame();
        frame.setBuffer(buff);
    }

    @Override
    public boolean canProvide() {
        return this.localPlayer.provide(frame);
    }

    @Override
    public ByteBuffer provide20MsAudio() {
        return buff.flip();
    }

    @Override
    public boolean isOpus() {
        return true;
    }


}
