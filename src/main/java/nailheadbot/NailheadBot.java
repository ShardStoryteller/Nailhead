package nailheadbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.messages.MessageSnapshot;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.entities.emoji.CustomEmoji;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

public class NailheadBot extends ListenerAdapter {
    //Static strings (EMPTY BEFORE PUSHING TO GITHUB)
    public static final String PREFIX = "n!";
    public static final String TOKEN = "";
    public static final String BOT_ID = "";
    public static final String MC_BOT_ID = "";
    public static final String TEST_SERVER_ID = "";
    public static final String IB_SERVER_ID = "";
    public static final String IB_BOT_CHANNEL_ID = "";
    public static final String IB_MINECRAFT_CHANNEL_ID = "";
    public static final String[] BOARD_CHANNEL_IDS = {""};
    public static final String[] BOARD_SERVER_IDS = {""};

    public static final Map<String, Integer> REACT_QUOTA_MAP = new HashMap<>();
    public static final Map<String, String> EMOJI_CHANNEL_MAP = new HashMap<>();
    public static final Map<String, String> CUSTOM_EMOJI_CHANNEL_MAP = new HashMap<>();
    public static final Map<String, String> NSFW_BOARD_MAP = new HashMap<>();
    public static final Map<String, String> CUSTOM_EMOJI_GUILD_MAP = new HashMap<>();
    public static final ArrayList<String> THE_LIST = new ArrayList<>();

    public static final boolean DEBUG_MODE = false;

    public static void main(String[] args) {
        THE_LIST.add("Aleksh");

        //Console input
        Scanner scanner = new Scanner(System.in);

        //Build necessary hashmaps


        //JDA object with all required specs
        JDABuilder jdaBuilder = JDABuilder.createDefault(TOKEN,
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.MESSAGE_CONTENT,
                        GatewayIntent.GUILD_MEMBERS,
                        GatewayIntent.GUILD_VOICE_STATES,
                        GatewayIntent.GUILD_WEBHOOKS,
                        GatewayIntent.GUILD_MESSAGE_REACTIONS,
                        GatewayIntent.SCHEDULED_EVENTS,
                        GatewayIntent.GUILD_EXPRESSIONS,
                        GatewayIntent.AUTO_MODERATION_CONFIGURATION,
                        GatewayIntent.AUTO_MODERATION_EXECUTION);

        jdaBuilder.setChunkingFilter(ChunkingFilter.ALL);
        jdaBuilder.addEventListeners(new NailheadBot());

        if(DEBUG_MODE){
            jdaBuilder.setActivity(Activity.customStatus("Undergoing maintenance!"));
            jdaBuilder.setStatus(OnlineStatus.DO_NOT_DISTURB);
        }
        else{
            jdaBuilder.setActivity(Activity.customStatus("Bot online, use n!nailhelp"));
            jdaBuilder.setStatus(OnlineStatus.ONLINE);
        }

        JDA jda = jdaBuilder.build();

        //Read console input
        while(scanner.hasNext()) {
            String cmd = scanner.nextLine();

            ///Command: "Say"
            ///Bot sends a message in a specified channel
            ///Format: Say [channelId] [message]
            if (cmd.startsWith("say ")){
                String[] components = cmd.split(" ",3);
                TextChannel channel = jda.getTextChannelById(components[1]);
                if(channel != null){
                    channel.sendMessage(components[2]).queue();
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
        if (DEBUG_MODE && !event.getGuild().getId().equals(TEST_SERVER_ID)) return;

        //don't reply to other bots that aren't mineshraft
        if (event.getAuthor().isBot() && !event.getAuthor().getId().equals(MC_BOT_ID)) return;

        //exit method if message is a link
        if(LinkCleaner.messageTrackedNew(event)) return;

        String message = event.getMessage().getContentRaw();

        //minecraft bot integration
        if(event.getAuthor().getId().equals(MC_BOT_ID)){
            //if message is a user message
            if(message.startsWith("`<")){
                //remove usertag
                message = message.substring(message.indexOf(' ')+1);
            }
        }

        //exit method if message pings the bot
        if (event.getMessage().getMentions().isMentioned(event.getJDA().getSelfUser())){
            //only if not everyone ping
            if(!event.getMessage().getMentions().mentionsEveryone())
                MessageResponder.pingDetected(event, message.toLowerCase());
            return;
        }

        //exit method if message contains any special text
        if (MessageResponder.messageParse(event, message.toLowerCase())) return;

        //if outside ib server and message is command
        if(!event.getGuild().getId().equals(IB_SERVER_ID) && message.startsWith(PREFIX)){
            MessageHelper.handle(event, message);
            return;
        }
        //if in one of the bot channels and message is command
        if(message.startsWith(PREFIX)&& (event.getChannel().getId().equals(IB_BOT_CHANNEL_ID)||
                        event.getChannel().getId().equals(IB_MINECRAFT_CHANNEL_ID))){
            MessageHelper.handle(event, message);
        }
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event){
        //No functionality outside test server when in debug mode
        if (DEBUG_MODE && !event.getGuild().getId().equals(TEST_SERVER_ID)) return;
        //Ignore bot reactions
        if (event.getUser().isBot()) return;

        Message originalMessage = event.getChannel().retrieveMessageById(event.getMessageId()).complete();
        String emojiString = event.getReaction().getEmoji().getAsReactionCode();
        //Custom emoji placeholder object
        CustomEmoji customEmoji = null;

        //Check for X emoji
        if(emojiString.equals("❌")) {
            //Ignore non-bot messages
            if (!event.getMessageAuthorId().equals(BOT_ID)) return;
            //If message starts with a ping to the initiating user
            if(originalMessage.getContentRaw().startsWith("<@" + event.getUser().getId() + ">")){
                //Delete the message
                event.getChannel().deleteMessageById(event.getMessageId()).queue();
            }
        }

        //If outside board supported servers then return
        if(!Arrays.asList(BOARD_SERVER_IDS).contains(event.getGuild().getId())) return;

        //If already in board channel then return
        if(Arrays.asList(BOARD_CHANNEL_IDS).contains(event.getChannel().getId())) return;

        //Set data if custom emoji
        if(event.getEmoji().getType() == Emoji.Type.CUSTOM) {
            customEmoji = event.getEmoji().asCustom();
        }
        //Handle for custom emoji
        if (customEmoji != null) {
            //Return if custom emoji not in list of supported emojis
            if(!CUSTOM_EMOJI_CHANNEL_MAP.containsKey(customEmoji.getId())) return;
            //Return if the emote is not from the same guild as the message
            if(!CUSTOM_EMOJI_GUILD_MAP.get(customEmoji.getId()).equals(event.getGuild().getId())) return;
            //Check for the criteria
            checkForReactCriteria(event);
            return;
        }
        //Handle for normal emoji
        //Return if the emote server pair is not in the list
        if(!EMOJI_CHANNEL_MAP.containsKey(emojiString + " " + originalMessage.getGuildId())) return;
        //Check for the criteria
        checkForReactCriteria(event);
    }

    private void checkForReactCriteria(MessageReactionAddEvent event) {
        //redundant debug mode check for safety
        if(DEBUG_MODE && !event.getGuild().getId().equals(TEST_SERVER_ID)) return;

        Message message = event.getChannel().retrieveMessageById(event.getMessageId()).complete();
        List<MessageSnapshot> forwards = message.getMessageSnapshots();
        String MessageContent = forwards.isEmpty() ? message.getContentRaw() : forwards.getFirst().getContentRaw();
        String header = "Message Author: <@" + message.getAuthor().getId() + ">\n";
        String channelName = "Original Channel: #" + message.getChannel().getName() + "\n";
        String messageurl = "Original Message: [Link](" + message.getJumpUrl() + ")\n\n";

        TextChannel channel;
        MessageCreateAction action;
        String reaction_msg;
        String pinMessage;

        //Remove everyone and here pings
        MessageContent = MessageContent
                .replace("@everyone","everyone")
                .replace("@here", "here");

        //Handle custom emoji reaction
        if(event.getEmoji().getType() == Emoji.Type.CUSTOM) {
            CustomEmoji customEmoji = event.getEmoji().asCustom();
            channel = (TextChannel) event.getGuild().getGuildChannelById(
                    CUSTOM_EMOJI_CHANNEL_MAP.get(customEmoji.getId()));
            reaction_msg = "Reaction: " + customEmoji.getAsMention() + "\n";
        }
        //Handle regular emoji reaction
        else{
            channel = (TextChannel) event.getGuild().getGuildChannelById(
                    EMOJI_CHANNEL_MAP.get(event.getReaction().getEmoji().getFormatted() + " " + message.getGuildId()));
            reaction_msg = "Reaction: " + event.getEmoji().getFormatted() + "\n";
        }

        //If channel is not a thread
        if(event.getChannelType() == ChannelType.TEXT){
            if(event.getChannel().asTextChannel().isNSFW()){
                channel = (TextChannel) event.getGuild().getGuildChannelById(
                        NSFW_BOARD_MAP.get(message.getGuildId()));
            }
        }
        //If channel is a thread
        if(event.getChannelType() == ChannelType.GUILD_PUBLIC_THREAD |
        event.getChannelType() == ChannelType.GUILD_PRIVATE_THREAD |
        event.getChannelType() == ChannelType.GUILD_NEWS_THREAD){
            ///Integer tracks state of parent channel
            ///-1: Invalid state (forum parent channel)
            ///0: Parent is not an NSFW channel
            ///1: Parent is an NSFW channel
            int channelConfig = 0;
            try{
                if(event.getChannel().asThreadChannel().getParentChannel().asTextChannel().isNSFW()){
                    channelConfig = 1;
                }
            }
            catch(IllegalStateException E){
                channelConfig = -1;
            }

            //Switch statement to handle
            switch (channelConfig){
                case 1:
                    channel = (TextChannel) event.getGuild().getGuildChannelById(
                            NSFW_BOARD_MAP.get(message.getGuildId()));
                    break;
                case 0:
                    break;
                case -1:
                    try{
                        if(event.getChannel().asThreadChannel().getParentChannel().asForumChannel().isNSFW()){
                            channel = (TextChannel) event.getGuild().getGuildChannelById(
                                    NSFW_BOARD_MAP.get(message.getGuildId()));
                        }
                    }
                    catch(IllegalStateException E){
                        System.out.println("An Illegal state has been reached: Invalid Forum Detection on channel " + event.getChannel().getName());
                        return;
                    }
                    break;
                default:
                    System.out.println("An Illegal state has been reached: channelConfig " + channelConfig + " on channel " + event.getChannel().getName());
                    return;
            }
        }

        int count = -1;

        //For all reactions
        for(MessageReaction reaction: message.getReactions()){
            //Match the reaction that was just changed
            if (reaction.getEmoji().equals(event.getEmoji())) {
                //Get the count of that reaction
                count = reaction.getCount();
                //Exit the for loop
                break;
            }
        }
        //Return if count is less than the server react quota
        if (count < REACT_QUOTA_MAP.get(message.getGuildId())) return;

        //Return if bot has already reacted
        for(User user: event.getReaction().retrieveUsers().complete()){
            if (user.getId().equals(BOT_ID)){
                return;
            }
        }

        //Add bot reaction
        message.addReaction(event.getEmoji()).queue();

        pinMessage = header + channelName + reaction_msg + messageurl + MessageContent;
        while(pinMessage.length() > 2000){
            if(pinMessage.startsWith(header + channelName + reaction_msg + messageurl)){
                channel.sendMessage(header + channelName + reaction_msg + messageurl).queue();
                pinMessage = MessageContent;
            }
            else{
                channel.sendMessage(pinMessage.substring(0,2000)).queue();
                pinMessage = pinMessage.substring(2000);
            }
        }

        //Forward to channel
        action = channel.sendMessage(pinMessage);
        //Add all attachments from the original message
        addMessageAttachments(message, action, channel);
        //Add forwarded message attachments
        if(!forwards.isEmpty()){
            for (MessageSnapshot forward : forwards){
                addSnapshotAttachments(forward, action, channel);
            }
        }
        //Do the thing
        action.queue();
    }

    private void addMessageAttachments(Message originalMessage, MessageCreateAction action, TextChannel channel_real) {
        addAttachments(action, channel_real, originalMessage.getAttachments());
    }
    private void addSnapshotAttachments(MessageSnapshot originalMessage,
                                        MessageCreateAction action, TextChannel channel_real) {
        addAttachments(action, channel_real, originalMessage.getAttachments());
    }

    private void addAttachments(MessageCreateAction action, TextChannel channel_real,
                                List<Message.Attachment> attachments) {
        if(attachments.isEmpty()) return;
        attachments.forEach(attachment -> {
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
