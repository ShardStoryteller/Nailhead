package nailheadbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.entities.emoji.CustomEmoji;

import java.io.File;
import java.util.Scanner;

public class NailheadBot extends ListenerAdapter {
    //Static strings (EMPTY BEFORE PUSHING TO GITHUB)
    public static final String prefix = "n!";
    public static final String testServerID = "";
    public static final String ibServerID = "";
    public static final String ibBotChannelId = "";
    public static final String ibMinecraftChannelId = "";
    public static final String token = "";
    public static final String botId = "";
    public static final String mcBotId = "";
    public static final String rotEyesID = "";
    public static final String heartboardId = "";
    public static final String rotboardId = "";
    public static final String rotEyesMarkdown = "";
    public static boolean debugMode = false;

    public static void main(String[] args) {
        //Console input
        Scanner scanner = new Scanner(System.in);

        //JDA object with all required specs
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

        //Read console input
        while(scanner.hasNext()) {
            String cmd = scanner.nextLine();

            ///Command: "Say"
            ///Bot sends a message in a specified channel
            ///Format: Say [channelId] [message]
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

    //Bot scans all messages
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        //No functionality outside test server when in debug mode
        if (debugMode && !event.getGuild().getId().equals(testServerID)) return;

        //don't reply to other bots that aren't mineshraft
        if (event.getAuthor().isBot() && !event.getAuthor().getId().equals(mcBotId)) return;

        //exit method if message is a link
        if(LinkCleaner.messageTrackedNew(event)) return;

        String message = event.getMessage().getContentRaw();

        //minecraft bot integration
        if(event.getAuthor().getId().equals(mcBotId)){
            //if message is a user message
            if(message.startsWith("`<")){
                //remove usertag
                message = message.substring(message.indexOf(' ')+1);
            }
        }

        String parse = message.toLowerCase();

        //exit method if message pings the bot
        if (event.getMessage().getMentions().isMentioned(event.getJDA().getSelfUser())){
            MessageResponder.pingDetected(event, parse);
            return;
        }

        //exit method if message contains any special text
        if (MessageResponder.messageParse(event, parse)) return;

        //if outside ib server and message is command
        if(!event.getGuild().getId().equals(ibServerID) && message.startsWith(prefix)){
            MessageHelper.handle(event, message);
            return;
        }
        //if in ib server AND in one of the bot channels AND message is command
        if(event.getGuild().getId().equals(ibServerID)&&message.startsWith(prefix)&&
                (event.getChannel().getId().equals(ibBotChannelId)||event.getChannel().getId().equals(ibMinecraftChannelId))){
            MessageHelper.handle(event, message);
        }
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event){
        //No functionality outside test server when in debug mode
        if (debugMode && !event.getGuild().getId().equals(testServerID)) return;

        //Ignore bot reactions
        if (event.getUser().isBot()) return;

        Message originalMessage = event.getChannel().retrieveMessageById(event.getMessageId()).complete();
        String emojiString = event.getReaction().getEmoji().getAsReactionCode();
        String messageContent = originalMessage.getContentRaw();
        String userId = event.getUser().getId();

        //Check for X emoji
        if(emojiString.equals("❌")) {
            //Ignore non-bot messages
            if (!event.getMessageAuthorId().equals(botId)) return;
            //If message starts with a ping to the initiating user
            if(messageContent.startsWith("<@" + userId + ">")){
                //Delete the message
                event.getChannel().deleteMessageById(event.getMessageId()).queue();
            }
        }

        ///here be dragons
//
//        //If outside Ib's server then return
//        if(!event.getGuild().getId().equals(ibServerID)){ return;}
//
//        //If in heartboard or rotboard channel then return
//        if (event.getChannel().getId().equals(heartboardId)) return;
//        if (event.getChannel().getId().equals(rotboardId)) return;
//
//        String header = "Message Author: <@" + userId + "> \n\n";
//
//        //Heartboard
//        TextChannel heartChannel = (TextChannel) event.getGuild().getGuildChannelById(heartboardId);
//        if(emojiString.equals("\uD83D\uDE0D")){
//            event.getChannel().retrieveMessageById(event.getMessageId()).queue(message -> {
//                for (MessageReaction reaction : message.getReactions()){
//                    if(reaction.getEmoji().equals(event.getEmoji())){
//                        int count = reaction.getCount();
//                        if (count >= 3){
//
//
//
//                        }
//                    }
//                }
//            });
//
//
//            if (event.getReaction().getCount() == 3){
//                //Emoji as Emoji
//                Emoji emoji = Emoji.fromUnicode("U+1F60D");
//                //Reaction
//                MessageReaction reaction = event.getChannel().retrieveMessageById(event.getMessageId()).complete().getReaction(emoji);
//                reaction.retrieveUsers().queue(users -> {
//                    boolean botReacted = users.stream().anyMatch(user -> user.getIdLong() == Long.parseLong(botId));
//                    if (!botReacted) {
//                        originalMessage.addReaction(Emoji.fromUnicode("U+1F60D")).queue();
//
//                        MessageCreateAction action = heartChannel.sendMessage(header + messageContent);
//
//                        originalMessage.getAttachments().forEach(attachment -> {
//                            action.addFiles(FileUpload.fromData(new File(attachment.getProxyUrl())));
//                        });
//
//                        action.queue();
//                    }
//                });
//            }
//        }
//        //Rotboard
//        TextChannel rotChannel = (TextChannel) event.getGuild().getGuildChannelById(rotboardId);
//        if(event.getEmoji().getType() == Emoji.Type.CUSTOM){
//            CustomEmoji customEmoji = event.getEmoji().asCustom();
//
//            if(customEmoji.getId().equals(rotEyesID)){
//                event.getChannel().retrieveMessageById(event.getMessageId()).queue(message -> {
//                   for (MessageReaction reaction : message.getReactions()){
//                       if(reaction.getEmoji().equals(event.getEmoji())){
//                           int count = reaction.getCount();
//                           if (count >= 3){
//
//
//                           }
//
//
//
//
//                       }
//                   }
//                });
//
//
//
//                if (.getReactions().getCount() == 3){
//                    //Reaction
//                    MessageReaction reaction = event.getChannel().retrieveMessageById(event.getMessageId()).complete().getReaction(customEmoji);
//                    reaction.retrieveUsers().queue(users -> {
//                        boolean botReacted = users.stream().anyMatch(user -> user.getIdLong() == Long.parseLong(botId));
//                        if (!botReacted) {
//                            Emoji rot = Emoji.fromFormatted(rotEyesMarkdown);
//                            originalMessage.addReaction(rot).queue();
//
//                            MessageCreateAction action = rotChannel.sendMessage(header + messageContent);
//
//                            originalMessage.getAttachments().forEach(attachment -> {
//                                action.addFiles(FileUpload.fromData(new File(attachment.getProxyUrl())));
//                            });
//
//                            action.queue();
//                        }
//                    });
//                }
//            }
//        }
    }
}
