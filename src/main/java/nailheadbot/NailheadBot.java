package nailheadbot;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

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

        if(message.contains("?si=")){
            messageTracked(event, "?si=");
        }
        if(message.contains("?utm_source=")){
            messageTracked(event, "?utm_source=");
        }
    }

    public void messageTracked(MessageReceivedEvent event, String tag) {
        //get raw message text
        String msgText = event.getMessage().getContentRaw();

        //Find the index of the tag section
        int tagLocation = msgText.indexOf(tag);

        //Find index of ? if exists
        int addonLocation = msgText.indexOf('&', tagLocation);

        //Split string into bits
        String urlStart = msgText.substring(0, tagLocation);
        String urlEnd = "";
        if(addonLocation > -1){
            urlStart = msgText.substring(0, tagLocation+1);
            urlEnd = msgText.substring(addonLocation+1);
        }

        //Add string together
        String compiledString = urlStart + urlEnd;

        event.getMessage().reply("Hey there! Looks like you might have just sent a link " +
                "with a source identifier attached (the bit that starts with '" + tag + "')! " +
                "I've posted a version without this tag (or at least attempted to)! " + compiledString
                ).queue();
    }
}
