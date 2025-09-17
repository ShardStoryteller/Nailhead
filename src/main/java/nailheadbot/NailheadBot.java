package nailheadbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NailheadBot extends ListenerAdapter {
    public static final String prefix = "n!";
    public static final String ibServerID = "";
    public static final String ibBotChannelId = "";
    public static final String token = "";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        JDA jda = JDABuilder.createDefault(token,
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

        while(scanner.hasNext()) {
            String cmd = scanner.nextLine();

            if (cmd.startsWith("say ")){
                String[] components = cmd.split(" ",3);

                String channelID = components[1];
                String message = components[2];
                TextChannel channel = jda.getTextChannelById(channelID);
                if(channel != null){
                    channel.sendMessage(message).queue();
                }
                else{
                    System.out.println("ERROR: channel not found");
                }
            }
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        String message = event.getMessage().getContentRaw();

        if (event.getMessage().getMentions().isMentioned(event.getJDA().getSelfUser())){
            if(message.toLowerCase().contains("you're breathtaking")){
                event.getMessage().reply("YOU'RE breathtaking!!").queue();
            }
            if(message.toLowerCase().contains("i love you")){
                event.getMessage().addReaction(Emoji.fromUnicode("U+1F60D")).queue();
            }
            if(message.toLowerCase().contains("fuck you") || message.toLowerCase().contains("you suck")){
                fuckYou.fuckYou(event);
            }
            if(message.toLowerCase().contains("thank you") || message.toLowerCase().contains("thanks")){
                event.getMessage().addReaction(Emoji.fromUnicode("U+1F44D")).queue();
            }
        }
        if (message.equalsIgnoreCase("fuck you nailhead") || message.equalsIgnoreCase("nailhead you suck")){
            fuckYou.fuckYou(event);
        }
        if(message.equalsIgnoreCase("i love you nailhead")){
            event.getMessage().addReaction(Emoji.fromUnicode("U+1F60D")).queue();
        }
        if(message.toLowerCase().contains("thank you nailhead") || message.toLowerCase().contains("thanks nailhead")){
            event.getMessage().addReaction(Emoji.fromUnicode("U+1F44D")).queue();
        }

        //if not in ib server
        if(!event.getGuild().getId().equals(ibServerID) && message.startsWith(prefix)){
            MessageHelper.handle(event);
        }

        //if in ib server and in the bot channel
        if(event.getGuild().getId().equals(ibServerID)&&event.getChannel().getId().equals(ibBotChannelId)&&message.startsWith(prefix)){
            MessageHelper.handle(event);
        }

        //if message is a link
        if(message.contains("http")){
            //if message contains a data tracker
            if(message.contains("?si=")){
                messageTracked(event, "?si=", message.contains("||"));
            }
            //copy for utm_source
            if(message.contains("?utm_source=")) {
                messageTracked(event, "?utm_source=", message.contains("||"));
            }
            if(message.contains("&si=")){
                messageTracked(event, "&si=", message.contains("||"));
            }
            if(message.contains("x.com")||message.contains("twitter.com")){
                if(message.contains("?t=")){
                    messageTracked(event, "?t=", message.contains("||"));
                }
                if(message.contains("&t=")){
                    messageTracked(event, "&t=", message.contains("||"));
                }
            }
        }
    }

    public void messageTracked(MessageReceivedEvent event, String tag, boolean spoiler){
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

        if(spoiler)
        {
            messageString.append(" It also seems like one or more link(s) may have been spoilermarked. " +
                    "I've gone ahead and spoilermarked them all for you as well!");
        }

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

        event.getMessage().reply(messageString.toString()).queue();
    }
}
