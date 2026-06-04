package nailheadbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
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
    public static final String westID = "";
    public static final String fireboardId = "";
    public static final String heartboardId = "";
    public static final String rotboardId = "";
    public static final String westboardId = "";
    public static final String[] nsfwChannelIds = {""};
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
        String userId = event.getUser().getId();

        //Check for X emoji
        if(emojiString.equals("❌")) {
            //Ignore non-bot messages
            if (!event.getMessageAuthorId().equals(botId)) return;
            //If message starts with a ping to the initiating user
            if(originalMessage.getContentRaw().startsWith("<@" + userId + ">")){
                //Delete the message
                event.getChannel().deleteMessageById(event.getMessageId()).queue();
            }
        }

        //If outside Ib's server then return
        if(!event.getGuild().getId().equals(ibServerID)){ return;}

        //If already in board channel then return
        if (event.getChannel().getId().equals(fireboardId)) return;
        if (event.getChannel().getId().equals(heartboardId)) return;
        if (event.getChannel().getId().equals(rotboardId)) return;
        if (event.getChannel().getId().equals(westboardId)) return;

        //Channel objects
        TextChannel fireChannel = (TextChannel) event.getGuild().getGuildChannelById(fireboardId);
        TextChannel heartChannel = (TextChannel) event.getGuild().getGuildChannelById(heartboardId);
        TextChannel rotChannel = (TextChannel) event.getGuild().getGuildChannelById(rotboardId);
        TextChannel westChannel = (TextChannel) event.getGuild().getGuildChannelById(westboardId);

        boolean nsfw = Arrays.asList(nsfwChannelIds).contains(originalMessage.getChannelId());

        //Fireboard
        if(emojiString.equals("\uD83D\uDD25")){
            //Forward to fireboard channel
            checkForReactCritera(event, originalMessage, event.getEmoji(), fireChannel, nsfw);
        }
        //Heartboard
        if(emojiString.equals("\uD83D\uDE0D")) {
            //Forward to heartboard channel
            checkForReactCritera(event, originalMessage, event.getEmoji(), heartChannel, nsfw);
        }
        //Custom emoji placeholder object
        CustomEmoji customEmoji = null;
        //Set data if custom emoji
        if(event.getEmoji().getType() == Emoji.Type.CUSTOM) {
            customEmoji = event.getEmoji().asCustom();
        }
        //Exit if not custom emoji
        if (customEmoji == null) return;
        //Rotboard
        if(customEmoji.getId().equals(rotEyesID)){
            //Forward to rotboard channel
            checkForReactCritera(event, originalMessage, customEmoji, rotChannel, nsfw);
        }
        //Westboard
        if(customEmoji.getId().equals(westID)){
            //Forward to westboard channel
            checkForReactCritera(event, originalMessage, customEmoji, westChannel, true);
        }
    }

    private void checkForReactCritera(MessageReactionAddEvent event, Message originalMessage,
                                      Emoji emoji, TextChannel channel, boolean nsfw) {
        String header = "Message Author: <@" + originalMessage.getAuthor().getId() + ">\n";
        String channelName = "Original Channel: #" + originalMessage.getChannel().getName() + "\n";
        String messageurl = "Original Message: [Link](" + originalMessage.getJumpUrl() + ")\n\n";
        List<User> userList = event.getReaction().retrieveUsers().complete();
        Message message = event.getChannel().retrieveMessageById(event.getMessageId()).complete();

        MessageCreateAction action;
        String reaction_msg;

        //I hate this
        if(emoji.getType() == Emoji.Type.CUSTOM){
            CustomEmoji emoji1 = (CustomEmoji) emoji;
            reaction_msg = "Reaction: " + emoji1.getAsMention() + "\n";
        }
        else{
            reaction_msg = "Reaction: " + emoji.getFormatted() + "\n";
        }

        int count = -1;

        //For all reactions
        for(MessageReaction reaction: message.getReactions()){
            //Match the reaction that was just changed
            if (reaction.getEmoji().equals(event.getEmoji())) {
                //Get the count of that reaction
                count = reaction.getCount();
            }
        }
        //Return if count is less than 5
        if (count < 5) return;

        //Check if bot reacted
        boolean botReacted = false;
        for(User user: userList){
            if (user.getId().equals(botId)){
                botReacted = true;
            }
        }
        //Return if bot reacted
        if (botReacted) return;

        //Always send nsfw into westboard
        if(nsfw){
            TextChannel channel_real = (TextChannel) event.getGuild().getGuildChannelById(westboardId);

            action = channel_real.sendMessage
                    (header + channelName + reaction_msg + messageurl + originalMessage.getContentRaw());
            //Add all attachments from the original message
            forwardToChannel(originalMessage, action, channel_real);
        }
        else{
            action = channel.sendMessage
                    (header + channelName + messageurl + originalMessage.getContentRaw());
            //Add all attachments from the original message
            forwardToChannel(originalMessage, action, channel);
        }
        //Add bot reaction
        originalMessage.addReaction(emoji).queue();
        //Do the thing
        action.queue();
    }

    private void forwardToChannel(Message originalMessage, MessageCreateAction action, TextChannel channel_real) {
        originalMessage.getAttachments().forEach(attachment -> {
            try {
                InputStream stream = new URL(attachment.getProxyUrl()).openStream();
                action.addFiles(FileUpload.fromData(stream, attachment.getFileName()));
            } catch (IOException e) {
                channel_real.sendMessage("Failed to attach an attachment." +
                        " Please contact ShardStoryteller to troubleshoot!").queue();
                e.printStackTrace();
            }
        });
    }
}
