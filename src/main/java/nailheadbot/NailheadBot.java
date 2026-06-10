package nailheadbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
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

        String parse = message.toLowerCase();

        //exit method if message pings the bot
        if (event.getMessage().getMentions().isMentioned(event.getJDA().getSelfUser())){
            MessageResponder.pingDetected(event, parse);
            return;
        }

        //exit method if message contains any special text
        if (MessageResponder.messageParse(event, parse)) return;

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
        String userId = event.getUser().getId();

        //Check for X emoji
        if(emojiString.equals("❌")) {
            //Ignore non-bot messages
            if (!event.getMessageAuthorId().equals(BOT_ID)) return;
            //If message starts with a ping to the initiating user
            if(originalMessage.getContentRaw().startsWith("<@" + userId + ">")){
                //Delete the message
                event.getChannel().deleteMessageById(event.getMessageId()).queue();
            }
        }

        //If outside board supported servers then return
        if(!Arrays.asList(BOARD_SERVER_IDS).contains(event.getGuild().getId())) return;

        //If already in board channel then return
        if(Arrays.asList(BOARD_CHANNEL_IDS).contains(event.getChannel().getId())) return;

        boolean nsfw = originalMessage.getChannel().asTextChannel().isNSFW();

        //Custom emoji placeholder object
        CustomEmoji customEmoji = null;
        //Set data if custom emoji
        if(event.getEmoji().getType() == Emoji.Type.CUSTOM) {
            customEmoji = event.getEmoji().asCustom();
        }
        //Handle for custom emoji
        if (customEmoji != null) {
            //Return if the emote is not from the same guild as the message
            if(!CUSTOM_EMOJI_GUILD_MAP.get(customEmoji.getId()).equals(event.getGuild().getId())) return;

            //Return if custom emoji not in list of supported emojis
            if(!CUSTOM_EMOJI_CHANNEL_MAP.containsKey(customEmoji.getId())) return;
            //Check for the criteria
            checkForReactCritera(event, originalMessage, customEmoji, nsfw);
            return;
        }
        //Handle for normal emoji
        //Return if the emote server pair is not in the list
        if(!EMOJI_CHANNEL_MAP.containsKey(emojiString + " " + originalMessage.getGuildId())) return;
        //Check for the criteria
        checkForReactCritera(event, originalMessage, event.getEmoji(), nsfw);
    }

    private void checkForReactCritera(MessageReactionAddEvent event, Message originalMessage,
                                      Emoji emoji, boolean nsfw) {
        //redundant debug mode check for safety
        if(DEBUG_MODE && !event.getGuild().getId().equals(TEST_SERVER_ID)) return;

        String header = "Message Author: <@" + originalMessage.getAuthor().getId() + ">\n";
        String channelName = "Original Channel: #" + originalMessage.getChannel().getName() + "\n";
        String messageurl = "Original Message: [Link](" + originalMessage.getJumpUrl() + ")\n\n";
        List<User> userList = event.getReaction().retrieveUsers().complete();
        Message message = event.getChannel().retrieveMessageById(event.getMessageId()).complete();

        TextChannel channel;
        MessageCreateAction action;
        String reaction_msg;

        //Always forward nsfw to nsfw
        if(nsfw){
            channel = (TextChannel) event.getGuild().getGuildChannelById(
                    NSFW_BOARD_MAP.get(originalMessage.getGuildId()));
        }
        else{
            //Handle custom emoji reaction
            if(event.getEmoji().getType() == Emoji.Type.CUSTOM) {
                CustomEmoji customEmoji = event.getEmoji().asCustom();
                channel = (TextChannel) event.getGuild().getGuildChannelById(
                        CUSTOM_EMOJI_CHANNEL_MAP.get(customEmoji.getId()));
            }
            //Handle regular emoji reaction
            else{
                channel = (TextChannel) event.getGuild().getGuildChannelById(
                        EMOJI_CHANNEL_MAP.get(event.getReaction().getEmoji().getFormatted() + " " + originalMessage.getGuildId()));
            }
        }

        //I hate this
        if(emoji.getType() == Emoji.Type.CUSTOM && nsfw){
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
                //Exit the for loop
                break;
            }
        }
        //Return if count is less than the server react quota
        if (count < REACT_QUOTA_MAP.get(originalMessage.getGuildId())) return;

        //Return if bot has already reacted
        for(User user: userList){
            if (user.getId().equals(BOT_ID)){
                return;
            }
        }

        //Add bot reaction
        originalMessage.addReaction(emoji).queue();

        //Forward to channel
        action = channel.sendMessage
                (header + channelName + reaction_msg + messageurl + originalMessage.getContentRaw());
        //Add all attachments from the original message
        forwardToChannel(originalMessage, action, channel);
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
