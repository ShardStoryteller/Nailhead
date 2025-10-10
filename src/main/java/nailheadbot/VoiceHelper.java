package nailheadbot;

import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class VoiceHelper {
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
            channel.sendMessage("Something went wrong. Sorry!").queue();
        }
    }
}