package nailheadbot;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkCleaner {

    public static boolean linkParse(String message, MessageReceivedEvent event){
        //if message is a link
        if(message.contains("http")){
            //if message contains a data tracker
            if(message.contains("?si=")){
                messageTracked(event, "?si=", message.contains("||"));
                return true;
            }
            if(message.contains("&si=")){
                messageTracked(event, "&si=", message.contains("||"));
                return true;
            }
            if(message.contains("?utm_source=")) {
                messageTracked(event, "?utm_source=", message.contains("||"));
                return true;
            }
            if(message.contains("x.com")||message.contains("twitter.com")){
                if(message.contains("?t=")){
                    messageTracked(event, "?t=", message.contains("||"));
                    return true;
                }
                if(message.contains("?s=")){
                    messageTracked(event, "?s=", message.contains("||"));
                    return true;
                }
            }
            if(message.contains("/x.com")||message.contains("/twitter.com")){
                messageTwitter(event);
                return true;
            }
        }
        return false;
    }

    public static void messageTwitter(MessageReceivedEvent event){
        //get the channel of the message
        MessageChannel channel = event.getChannel();
        //save message
        Message message = event.getMessage();
        //get raw message text
        String msgText = message.getContentRaw();
        //save message sender ID
        String UID = event.getAuthor().getId();

        // Begin building message string
        StringBuilder messageString = new StringBuilder("<@" + UID + ">" + " Cleaned your link(s) up a bit for ya, buddy!\n\n");

        //Replace with fixupx
        msgText = msgText.replaceAll("/(x|twitter)\\.com", "/fixupx.com");

        //Append the new message text
        messageString.append(msgText);

        //Delete the original message
        message.delete().queue();

        channel.sendMessage(messageString.toString()).queue();
    }

    public static void messageTracked(MessageReceivedEvent event, String tag, boolean spoiler){
        //get the channel of the message
        MessageChannel channel = event.getChannel();
        //save message
        Message message = event.getMessage();
        //get raw message text
        String msgText = message.getContentRaw();
        //save message sender ID
        String UID = event.getAuthor().getId();

        // Begin building message string
        StringBuilder messageString = new StringBuilder("<@" + UID + ">" + " Cleaned your link(s) up a bit for ya, buddy!\n\n");

        //Replace with fixupx
        msgText = msgText.replaceAll("/(x|twitter)\\.com", "/fixupx.com");

        //link detection regex
        String regex = "(https?://[^\\s\"'>]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(msgText);

        //list of all urls in message
        List<String> urls = new ArrayList<>();

        //add all detected links to list
        while (matcher.find()) {
            urls.add(matcher.group());
        }

        //For each url in the list
        for (String url : urls) {
            //Find the index of the tag section
            int tagLocation = url.indexOf(tag);

            //Find index of ? if exists
            int addonLocation = url.indexOf('&', tagLocation+2);

            //Split string into bits
            String urlStart = url.substring(0, tagLocation);
            String urlEnd = "";

            if(addonLocation > -1){
                urlStart = url.substring(0, tagLocation+1);
                urlEnd = url.substring(addonLocation+1);
            }

            messageString.append(" ");

            if(spoiler){
                messageString.append("|| ");
            }

            messageString.append(urlStart);

            //If link is NOT a twitter link
            if(!url.toLowerCase().contains("x.com")&&!url.toLowerCase().contains("twitter.com")){
                //Append url end
                messageString.append(urlEnd);
            }

            if(spoiler){
                messageString.append(" ||");
            }
        }

        //Delete the original message
        message.delete().queue();

        //Send replacement message
        channel.sendMessage(messageString.toString()).queue();
    }
}
