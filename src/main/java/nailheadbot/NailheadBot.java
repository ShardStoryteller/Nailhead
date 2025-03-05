package nailheadbot;

import nailheadbot.voice.MusicParse;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NailheadBot extends ListenerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(NailheadBot.class);
    public static boolean databaseActive;
    public static final String prefix = "n!";

    public static void main(String[] args) {
        databaseActive = true;
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

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException ex) {
            logger.warn("The database failed to activate! {}", ex.getMessage());
            databaseActive = false;
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        String message = event.getMessage().getContentRaw();

        if (message.startsWith(prefix)) {
            MessageHelper.handle(event);
        }
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        //if reaction is by bot exit method
        if (event.getUser().isBot()) return;
        //if reaction is on a non-bot message exit method
        if (!event.getMessageAuthorId().equals(event.getJDA().getSelfUser().getId())) return;

        Emoji emote = event.getEmoji();
        Message message = event.retrieveMessage().complete();
        EmbedBuilder embedBuilder = new EmbedBuilder();

        String footer = message.getEmbeds().getFirst().getFooter().getText();
        String p = footer.substring(footer.indexOf(" ") + 1, footer.indexOf("/"));
        String q = footer.substring(footer.indexOf("/") + 1);
        int pageIndex = Integer.parseInt(p);
        int pageLength = Integer.parseInt(q);

        int pageNum = -1;
        if (emote.equals(Emoji.fromUnicode("⏪"))) {
            pageNum = 1;
        }
        if (emote.equals(Emoji.fromUnicode("◀"))) {
            pageNum = pageIndex - 1;
            if (pageNum < 1) {
                pageNum = 1;
            }
        }
        if (emote.equals(Emoji.fromUnicode("▶"))) {
            pageNum = pageIndex + 1;
            if (pageNum > pageLength) {
                pageNum = pageLength;
            }
        }
        if (emote.equals(Emoji.fromUnicode("⏩"))) {
            pageNum = pageLength;
        }

        String title = message.getEmbeds().getFirst().getTitle();

        embedBuilder.setTitle(title);
        embedBuilder.setDescription(message.getEmbeds().getFirst().getDescription());
        embedBuilder.setFooter("Page " + pageNum + "/" + pageLength);
        embedBuilder.setColor(message.getEmbeds().getFirst().getColor());

        if (title.equals("Track List")) {
            String[] values = MusicParse.getTrackList();
            for (int i = 10 * (pageNum - 1); i < 10 * pageNum; i++) {
                try {
                    embedBuilder.addField(values[i], "", false);
                } catch (ArrayIndexOutOfBoundsException x) {
                    //DO NOTHING
                }

            }
        }

        message.editMessageEmbeds(embedBuilder.build()).queue();
    }
}
