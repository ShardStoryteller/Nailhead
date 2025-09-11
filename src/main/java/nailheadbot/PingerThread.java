package nailheadbot;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.concurrent.Task;

import java.util.List;

public class PingerThread extends Thread {
    MessageChannel[] channels;
    MessageReceivedEvent event;
    int iterations;
    String message;
    String messageBase = "<@%s>";
    String identifier;

    public PingerThread(MessageReceivedEvent event, int iterations, String identifier, String message) {
        this.event = event;
        this.iterations = iterations;
        this.message = " " + message;
        this.identifier = identifier;
        channels = new MessageChannel[1];
        channels[0] = event.getChannel();
    }

    public void run() {
        String outMessage = "";
        if (identifier == null || messageBase == null) {
            outMessage = "@everyone";
        } else {
            //check if identifier is numeric
            try {
                outMessage = String.format(messageBase, identifier);
            } catch (NumberFormatException ex) {
                //find all members
                Task<List<Member>> list = event.getGuild().retrieveMembersByPrefix(identifier, 100);
                List<Member> members = list.get();
                for (Member member : members) {
                    outMessage = String.format(messageBase, member.getId());
                }
            }
        }
        if (outMessage.isEmpty()) {
            outMessage = String.format(messageBase, event.getAuthor().getId());
        }

        for (int i = 0; i < iterations; i++) {
            for (MessageChannel channel : channels) {
                channel.sendMessage(outMessage + message).queue();
            }
        }
    }
}

