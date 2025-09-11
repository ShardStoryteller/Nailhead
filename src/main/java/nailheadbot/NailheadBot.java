package nailheadbot;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NailheadBot extends ListenerAdapter {
    public static final String prefix = "n!";

    public static void main(String[] args) {
        String token = "";

        JDABuilder.createDefault(token,
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.MESSAGE_CONTENT,
                        GatewayIntent.GUILD_MEMBERS,
                        GatewayIntent.GUILD_VOICE_STATES,
                        GatewayIntent.GUILD_WEBHOOKS,
                        GatewayIntent.GUILD_MESSAGE_REACTIONS,
                        GatewayIntent.SCHEDULED_EVENTS,
                        GatewayIntent.GUILD_EXPRESSIONS,
                        GatewayIntent.AUTO_MODERATION_CONFIGURATION,
                        GatewayIntent.AUTO_MODERATION_EXECUTION)
                .setChunkingFilter(ChunkingFilter.ALL)
                .addEventListeners(new NailheadBot())
                .setActivity(Activity.customStatus("Use n!nailhelp"))
                .enableCache(CacheFlag.VOICE_STATE).build();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        String message = event.getMessage().getContentRaw();

        if (message.startsWith(prefix)) {
            MessageHelper.handle(event);
        }

        if(message.contains("http")&&message.contains("?si=")) {
            messageTracked(event, "?si=");
        }
        if(message.contains("http")&&message.contains("?utm_source=")){
            messageTracked(event, "?utm_source=");
        }
    }

    public void messageTracked(MessageReceivedEvent event, String tag) {
        //get raw message text
        String msgText = event.getMessage().getContentRaw();

        String regex = "(https?://[^\\s\"'>]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(msgText);

        List<String> urls = new ArrayList<>();

        while (matcher.find()) {
            urls.add(matcher.group());
        }

        // Begin building message string
        StringBuilder messageString = new StringBuilder("Hey there! Looks like you might have just sent one " +
                "or more link(s) with a source identifier attached! " +
                "I've gone ahead and removed any tags for you!");

        //For each url in the list
        for (String url : urls) {
            //Find the index of the tag section
            int tagLocation = url.indexOf(tag);

            //Find index of ? if exists
            int addonLocation = url.indexOf('&', tagLocation);

            //Split string into bits
            String urlStart = url.substring(0, tagLocation);
            String urlEnd = "";

            if(addonLocation > -1){
                urlStart = url.substring(0, tagLocation+1);
                urlEnd = url.substring(addonLocation+1);
            }

            messageString.append(" ").append(urlStart).append(urlEnd);
        }

        event.getMessage().reply(messageString.toString()).queue();
    }
}
