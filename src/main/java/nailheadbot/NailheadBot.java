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
        //don't reply to bots
        if (event.getAuthor().isBot()) return;

        //store message as all lowercase
        String message = event.getMessage().getContentRaw();

        //exit method if message is a link
        if(linkParse(message, event)) return;

        //store lowercase string to parse
        String parse = message.toLowerCase();

        //exit method if message pings the bot
        if (event.getMessage().getMentions().isMentioned(event.getJDA().getSelfUser())){
            pingDetected(event, parse);
            return;
        }

        //exit method if message contains the special text
        if (messageParse(event, parse)) return;

        //if not in ib server and message is command
        if(!event.getGuild().getId().equals(ibServerID) && message.startsWith(prefix)){
            MessageHelper.handle(event);
            return;
        }
        //if in ib server and in the bot channel and message is command
        if(event.getGuild().getId().equals(ibServerID)&&event.getChannel().getId().equals(ibBotChannelId)&&message.startsWith(prefix)){
            MessageHelper.handle(event);
        }
    }

    public boolean messageParse(MessageReceivedEvent event, String parse){
        if (parse.contains("fuck you nailhead") || parse.contains("nailhead you suck") ||
                parse.contains("fuck u nailhead") || parse.contains("nailhead u suck") ||
                parse.contains("fuck off nailhead") || parse.contains("nailhead fuck off") ||
                parse.contains("nailhead fuck u") || parse.contains("u suck nailhead") ||
                parse.contains("nailhead fuck you") || parse.contains("you suck nailhead") ||
                parse.contains("nailhead i hate you") || parse.contains("nailhead i hate u") ||
                parse.contains("nailhead i fucking hate you") || parse.contains("nailhead i fucking hate u") ||
                parse.contains("i hate you nailhead") || parse.contains("i hate u nailhead") ||
                parse.contains("i fucking hate you nailhead") || parse.contains("i fucking hate u nailhead") ||
                parse.contains("i hate nailhead") || parse.contains("i fucking hate nailhead") ||
                parse.contains("nailhead kys") || parse.contains("kys nailhead") ||
                parse.contains("nailhead kill yourself") || parse.contains("kill yourself nailhead") ||
                parse.contains("nailhead kill urself") || parse.contains("kill urself nailhead") ||
                parse.contains("die nailhead")){
            FuckYou.fuckYou(event);
            return true;
        }
        if(parse.contains("i love you nailhead") || parse.contains("nailhead i love you") ||
                parse.contains("i love u nailhead") || parse.contains("nailhead i love u") ||
                parse.contains("you're awesome nailhead") || parse.contains("nailhead you're awesome") ||
                parse.contains("ur awesome nailhead") || parse.contains("nailhead ur awesome") ||
                parse.contains("your awesome nailhead") || parse.contains("nailhead your awesome") ||
                parse.contains("i love nailhead") || parse.contains("i <3 nailhead")){
            event.getMessage().addReaction(Emoji.fromUnicode("U+1F60D")).queue();
            return true;
        }
        if(parse.contains("nailhead you're breathtaking") || parse.contains("you're breathtaking nailhead") ||
                parse.contains("nailhead ur breathtaking") || parse.contains("ur breathtaking nailhead") ||
                parse.contains("nailhead your breathtaking") || parse.contains("your breathtaking nailhead")){
            event.getMessage().reply("YOU'RE breathtaking!!").queue();
            return true;
        }
        if(parse.contains("thank you nailhead") || parse.contains("thanks nailhead") ||
                parse.contains("thank u nailhead")){
            event.getMessage().addReaction(Emoji.fromUnicode("U+1F44D")).queue();
            return true;
        }
        return false;
    }

    public void pingDetected(MessageReceivedEvent event, String parse){
        if(parse.contains("fuck you") || parse.contains("you suck") ||
                parse.contains("fuck u") || parse.contains("u suck") ||
                parse.contains("fuck off") || parse.contains("i hate u") ||
                parse.contains("i hate you") || parse.contains("i fucking hate you") ||
                parse.contains("i fucking hate u") || parse.contains("kill yourself") ||
                parse.contains(" kys ") || parse.contains("kill urself")){
            FuckYou.fuckYou(event);
            return;
        }
        if(parse.contains("i love you") || parse.contains("you're awesome") ||
                parse.contains("ur awesome") || parse.contains("your awesome") ||
                parse.contains("i love u")){
            event.getMessage().addReaction(Emoji.fromUnicode("U+1F60D")).queue();
            return;
        }
        if(parse.contains("you're breathtaking") || parse.contains("ur breathtaking") ||
                parse.contains("your breathtaking")){
            event.getMessage().reply("YOU'RE breathtaking!!").queue();
            return;
        }
        if(parse.contains("thank you") || parse.contains("thanks") ||
                parse.contains("thank u")){
            event.getMessage().addReaction(Emoji.fromUnicode("U+1F44D")).queue();
        }
    }

    public boolean linkParse(String message, MessageReceivedEvent event){
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
                if(message.contains("&t=")){
                    messageTracked(event, "&t=", message.contains("||"));
                    return true;
                }
            }
        }
        return false;
    }

    public void messageTracked(MessageReceivedEvent event, String tag, boolean spoiler){
        //get raw message text
        String msgText = event.getMessage().getContentRaw();

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
