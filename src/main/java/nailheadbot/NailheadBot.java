package nailheadbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.util.Scanner;

public class NailheadBot extends ListenerAdapter {
    public static final String prefix = "n!";
    public static final String testServerID = "";
    public static final String ibServerID = "";
    public static final String ibBotChannelId = "";
    public static final String ibMinecraftChannelId = "";
    public static final String token = "";
    public static final String botId = "";
    public static final String mcBotId = "";
    public static boolean debugMode = false;

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
        //No functionality outside test server when in debug mode
        if (debugMode && !event.getGuild().getId().equals(testServerID)) return;

        //don't reply to other bots that aren't mineshraft
        if (event.getAuthor().isBot() && !event.getAuthor().getId().equals(mcBotId)) return;

        //exit method if message is a link
        if(LinkCleaner.messageTrackedNew(event)) return;

        //store message as string
        String message = event.getMessage().getContentRaw();

        //if message is from mineshraft bot
        if(event.getAuthor().getId().equals(mcBotId)){
            //if message is a user message
            if(message.startsWith("`<")){
                //update message to remove usertag
                message = message.substring(message.indexOf(' ')+1);
            }
        }

        //store lowercase string to parse
        String parse = message.toLowerCase();

        //exit method if message pings the bot
        if (event.getMessage().getMentions().isMentioned(event.getJDA().getSelfUser())){
            MessageResponder.pingDetected(event, parse);
            return;
        }

        //exit method if message contains the special text
        if (MessageResponder.messageParse(event, parse)) return;

        //if not in ib server and message is command
        if(!event.getGuild().getId().equals(ibServerID) && message.startsWith(prefix)){
            MessageHelper.handle(event, message);
            return;
        }
        //if in ib server and in one of the bot channels and message is command
        if(event.getGuild().getId().equals(ibServerID)&&message.startsWith(prefix)&&
                (event.getChannel().getId().equals(ibBotChannelId)||event.getChannel().getId().equals(ibMinecraftChannelId))){
            MessageHelper.handle(event, message);
        }
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event){
        //No functionality outside test server when in debug mode
        if (debugMode && !event.getGuild().getId().equals(testServerID)) return;

        if (event.getUser().isBot()) return; //Ignore bot reactions
        if (!event.getMessageAuthorId().equals(botId)) return; //Ignore non-bot messages

        String emoji = event.getReaction().getEmoji().getAsReactionCode();
        String messageContent = event.getChannel().retrieveMessageById(event.getMessageId()).complete().getContentRaw();
        String user = event.getUser().getId();

        //Check for emoji
        if(emoji.equals("❌")) {

            //If message starts with a ping to the user
            if(messageContent.startsWith("<@" + user + ">")){
                //Delete the message
                event.getChannel().deleteMessageById(event.getMessageId()).queue();
            }
        }
    }
}
