package nailheadbot.voice;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VoiceHelper {
    private static final Logger logger = LoggerFactory.getLogger(VoiceHelper.class);
    public static Guild ref;

    public static void join(MessageReceivedEvent event) {
        MessageChannel channel = event.getChannel();
        try {
            Member self = event.getGuild().getSelfMember();
            GuildVoiceState voiceState = self.getVoiceState();
            Member eventUser = event.getMember();
            GuildVoiceState userVoiceState = eventUser.getVoiceState();

            if (voiceState.inAudioChannel()) {
                channel.sendMessage("I'm already in a vc!").queue();
            } else if (!userVoiceState.inAudioChannel()) {
                channel.sendMessage("I can't tell what vc you are in!").queue();
            } else {
                AudioManager audioManager = event.getGuild().getAudioManager();
                VoiceChannel vc = userVoiceState.getChannel().asVoiceChannel();

                audioManager.openAudioConnection(vc);
            }
        } catch (Exception ex) {
            logger.error("Failed to join voice channel", ex);
            channel.sendMessage("Something went wrong. Sorry!").queue();
        }
    }

    public static void leave(MessageReceivedEvent event) {
        MessageChannel channel = event.getChannel();
        try {
            Member self = event.getGuild().getSelfMember();
            GuildVoiceState voiceState = self.getVoiceState();

            if (!voiceState.inAudioChannel()) {
                channel.sendMessage("I'm not in a vc!").queue();
            } else {
                AudioManager audioManager = event.getGuild().getAudioManager();

                audioManager.closeAudioConnection();
            }
        } catch (Exception ex) {
            logger.error("Failed to leave voice channel", ex);
            channel.sendMessage("Something went wrong. Sorry!").queue();
        }
    }

    public static void play(MessageReceivedEvent event) {
        MessageChannel channel = event.getChannel();
        String request = event.getMessage().getContentRaw().split(" ", 2)[1];
        String url = MusicParse.getURL(request);
        try {
            Member self = event.getGuild().getSelfMember();
            GuildVoiceState voiceState = self.getVoiceState();
            Member eventUser = event.getMember();
            GuildVoiceState userVoiceState = eventUser.getVoiceState();

            if (!voiceState.inAudioChannel()) {
                channel.sendMessage("I'm not in a vc!").queue();
            } else if (!userVoiceState.inAudioChannel()) {
                channel.sendMessage("You need to be in a vc!").queue();
            } else if (!userVoiceState.getChannel().equals(voiceState.getChannel())) {
                channel.sendMessage("You need to be in the same voice channel as me!").queue();
            } else if (url == null) {
                channel.sendMessage("Invalid track name!").queue();
            } else {
                channel.sendMessage("Playing " + request).queue();
                logger.info("Playing a track at URL: {}", url);
                PlayerManager.getInstance().loadAndPlay(event, url);
            }
        } catch (Exception ex) {
            logger.error("Failed to play track", ex);
            channel.sendMessage("Something went wrong. Sorry!").queue();
        }
    }

    public static void playSecret(MessageReceivedEvent event) {
        MessageChannel channel = event.getChannel();
        String request = event.getMessage().getContentRaw().split(" ", 2)[1];
        String url = MusicParse.getSecretURL(request);
        try {
            Member self = event.getGuild().getSelfMember();
            GuildVoiceState voiceState = self.getVoiceState();
            Member eventUser = event.getMember();
            GuildVoiceState userVoiceState = eventUser.getVoiceState();

            if (!voiceState.inAudioChannel() || !userVoiceState.inAudioChannel() || !userVoiceState.getChannel().equals(voiceState.getChannel()) || url==null) {
                channel.sendMessage("idk what you're talking about bro").queue();
            } else {
                channel.sendMessage("Playing the super cool secret version of " + request).queue();
                logger.info("Playing a super cool track at URL: {}", url);
                PlayerManager.getInstance().loadAndPlay(event, url);
            }
        } catch (Exception ex) {
            logger.error("Failed to play track", ex);
            channel.sendMessage("Something went wrong. Sorry!").queue();
        }
    }

    public static void skipTrack(MessageReceivedEvent event) {
        MessageChannel channel = event.getChannel();
        Member self = event.getGuild().getSelfMember();
        GuildVoiceState voiceState = self.getVoiceState();
        Member eventUser = event.getMember();
        GuildVoiceState userVoiceState = eventUser.getVoiceState();

        if (!voiceState.inAudioChannel()) {
            channel.sendMessage("I'm not in a vc!").queue();
        } else if (!userVoiceState.inAudioChannel()) {
            channel.sendMessage("You need to be in a vc!").queue();
        } else if (!userVoiceState.getChannel().equals(voiceState.getChannel())) {
            channel.sendMessage("You need to be in the same voice channel as me!").queue();
        } else {
            PlayerManager.getInstance().skipTrack(event);
        }
    }

    public static void pause(MessageReceivedEvent event) {
        MessageChannel channel = event.getChannel();
        Member self = event.getGuild().getSelfMember();
        GuildVoiceState voiceState = self.getVoiceState();
        Member eventUser = event.getMember();
        GuildVoiceState userVoiceState = eventUser.getVoiceState();

        if (!voiceState.inAudioChannel()) {
            channel.sendMessage("I'm not in a vc!").queue();
        } else if (!userVoiceState.inAudioChannel()) {
            channel.sendMessage("You need to be in a vc!").queue();
        } else if (!userVoiceState.getChannel().equals(voiceState.getChannel())) {
            channel.sendMessage("You need to be in the same voice channel as me!").queue();
        } else {
            PlayerManager.getInstance().pause(event);
        }
    }

    public static void joinBoner(MessageReceivedEvent event) {
        join(event);
        playBoner(event);
        markToLeave(event);
    }

    public static void playBoner(MessageReceivedEvent event) {
        try {
            logger.info("Boner incoming");
            PlayerManager.getInstance().loadAndPlaySilent(event, "resources/audio/BAD TO THE BONE.mp3");
        } catch (Exception e) {
            logger.error("Failed to play boner", e);
        }
    }

    public static void markToLeave(MessageReceivedEvent event) {
        ref = event.getGuild();
    }

    public static void leaveWhenEmpty(TrackScheduler scheduler) {
        TrackScheduler temp = PlayerManager.getInstance().getMusicManager(ref).scheduler;
        if (scheduler.equals(temp)) {
            AudioManager audioManager = ref.getAudioManager();
            audioManager.closeAudioConnection();
            ref = null;
        } else {
            logger.info("False");
        }
    }
}
