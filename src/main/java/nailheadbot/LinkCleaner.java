package nailheadbot;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class LinkCleaner {

    public static boolean messageTrackedNew(MessageReceivedEvent event){
        //get the channel of the message
        MessageChannel channel = event.getChannel();
        //save message
        Message message = event.getMessage();
        //get raw message text
        String msgText = message.getContentRaw();
        //save message sender ID
        String UID = event.getAuthor().getId();

        //Replace with fixupx
        msgText = msgText.replaceAll("/(x|twitter)\\.com", "/fixupx.com");

        //Replace with no trackers
        msgText = msgText.replaceAll("(https?://(fixupx|vxtwitter|fxtwitter|fixvx)\\.com/\\S+)\\u003F\\S+(\\s.*)?", "$1$3");
        msgText = msgText.replaceAll("(https?://\\S+&)(si|utm_source)=\\S+&", "$1");
        msgText = msgText.replaceAll("(https?://\\S+)&(si|utm_source)=\\S+(\\s.*)?", "$1$3");
        msgText = msgText.replaceAll("(https?://\\S+\\u003F)(si|utm_source)=\\S+&", "$1");
        msgText = msgText.replaceAll("(https?://\\S+)\\u003F(si|utm_source)=\\S+(\\s.*)?", "$1$3");


        //If no changes then exit the method
        if(msgText.equals(message.getContentRaw())) return false;

        //Create new message text
        String messageText = "<@" + UID + "> Cleaned your link(s) up a bit for ya, buddy!\n\n" + msgText;

        //Delete the original message
        message.delete().queue();

        if(messageText.length() > 2000)
        {
            //Send replacement message
            channel.sendMessage("<@" + UID + "> " + msgText.trim()).queue();
        }
        else
        {
            //Send replacement message
            channel.sendMessage(messageText.trim()).queue();
        }

        return true;
    }
}
