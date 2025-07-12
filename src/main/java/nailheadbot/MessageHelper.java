package nailheadbot;

import nailheadbot.database.EditHelper;
import nailheadbot.voice.MusicParse;
import nailheadbot.voice.VoiceHelper;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Random;

public class MessageHelper {
    private static final Logger logger = LoggerFactory.getLogger(MessageHelper.class);
    private static final Random random = new Random();
    public static final String prefix = "n!";
    public static String[] databaseApprovedGuilds = {
            "", ""
    };
    public static String[] anarchyGuilds = {
            "", ""
    };

    public static void handle(MessageReceivedEvent event) {
        Message message = event.getMessage();
        String contentRaw = message.getContentRaw();
        MessageChannel channel = event.getChannel();
        User user = event.getAuthor();
        String username = user.getName();
        String userId = user.getId();
        String guildId = event.getGuild().getId();

        boolean approved = Arrays.asList(databaseApprovedGuilds).contains(guildId);
        boolean anarchy = Arrays.asList(anarchyGuilds).contains(guildId);

        String[] components = contentRaw.split(" ", 3);

        String decider = components[0].substring(prefix.length()).toLowerCase();

        switch (decider) {
            case "help":
                channel.sendMessage("No. (use n!nailhelp)").queue();
                break;
            case "baba":
                channel.sendMessage("booey").queue();
                break;
            case "ping":
                String pingMessage = "";
                String pingIdentifier = switch (components.length) {
                    case 1 -> userId;
                    case 2 -> components[1];
                    default -> {
                        pingMessage = components[2];
                        yield components[1];
                    }
                };
                Thread pt = new PingerThread(event, 1, pingIdentifier, pingMessage);
                pt.start();
                break;
            case "fuckyou":
                String fuckMessage = "fuck you";
                String fuckIdentifier;
                if (components.length > 1 && anarchy) {
                    fuckIdentifier = components[1];
                } else {
                    fuckIdentifier = userId;
                }
                new PingerThread(event, 5, fuckIdentifier, fuckMessage).start();
                break;
            case "dtest":
                if (approved) {
                    channel.sendMessage("True!").queue();
                } else {
                    channel.sendMessage("False!").queue();
                }
                break;
            case "atest":
                if (anarchy) {
                    channel.sendMessage("True!").queue();
                } else {
                    channel.sendMessage("False!").queue();
                }
                break;
            case "nailhelp":
                if (username.equals("scamstoryteller")) {
                    channel.sendMessage("There is no helping you.").queue();
                } else if (username.equals("nailheadreal")) {
                    channel.sendMessage("You don't need my help, king. \uD83D\uDCAA").queue();
                } else {
                    if (components.length == 1 || !approved) {
                        EmbedHelper eb = new EmbedHelper(channel, "Command list (Prefix : " + prefix + ")", getCommands(approved, anarchy), getCmdDescriptions(approved));
                        eb.handleValues();
                    } else {
                        helpParse(components[1], channel);
                    }
                }
                break;
            case "nailhead":
                Nailhead.nailhead(event);
                break;
            case "badass":
                message.addReaction(Emoji.fromUnicode("U+1F60E")).queue();
                break;
            case "roll":
                int maxRoll = 20;
                String rollMessage = null;
                if (components.length > 1) {
                    try {
                        maxRoll = Integer.parseInt(components[1]);
                        if (maxRoll < 2) {
                            rollMessage = "Not a valid number to roll for!";
                        }
                    } catch (NumberFormatException e) {
                        rollMessage = "Not a valid number to roll for!";
                    }
                }
                if (rollMessage == null) {
                    int roll = random.nextInt(maxRoll) + 1;
                    rollMessage = "You rolled " + roll + "!";
                }
                channel.sendMessage(rollMessage).queue();
                break;
//            case "getstats":
//                if (approved && NailheadBot.databaseActive) {
//                    DatabaseHelper_old.getStatHandle(channel, contentRaw);
//                }
//                break;
//            case "add":
//                if (approved && NailheadBot.databaseActive) {
//                    try {
//                        DatabaseHelper_old.addCharHandle(channel, contentRaw, username);
//                    } catch (Exception e) {
//                        channel.sendMessage("Something went wrong when connecting to the database. Sorry!").queue();
//                        logger.error("Failed to connect to the database", e);
//                    }
//                }
//                break;
//            case "statedit":
//                if (approved && NailheadBot.databaseActive) {
//                    DatabaseHelper_old.editHandle(event);
//                }
//                break;
            case "everyone":
                if (anarchy) {
                    Thread et = new PingerThread(event, 1);
                    et.start();
                }
                break;
            case "supereveryone":
                if (anarchy) {
                    channel.sendMessage("lol ok you did this to yourself").queue();
                    MessageChannel[] channels = event.getGuild().getTextChannels().toArray(new TextChannel[0]);
                    Thread st = new PingerThread(channels, 4, " point and laugh at " + username);
                    st.start();
                }
                break;
            case "join":
                VoiceHelper.join(event);
                break;
            case "play":
                VoiceHelper.play(event);
                break;
            case "playawesome":
                VoiceHelper.playSecret(event);
                break;
            case "skip":
                VoiceHelper.skipTrack(event);
                break;
            case "pause":
                VoiceHelper.pause(event);
                break;
            case "\uD83D\uDC80":
                VoiceHelper.joinBoner(event);
                break;
            case "tracklist":
                MusicParse.trackList(channel);
                break;
            case "leave":
                VoiceHelper.leave(event);
                break;
            case "8ball":
                channel.sendMessage(magic8Ball.run8ball()).queue();
                break;
//            case "battle":
//                if(approved && NailheadBot.databaseActive) {
//                    NewBattleHandler.messageParse(event);
//                }
//                break;
//            case "makeathread":
//                makeThread(message);
//                break;
            default:
                logger.warn("Command not found: {}", contentRaw);
                break;
        }
    }

    public static String[] getCommands(boolean approval, boolean anarchy) {
        String[] output;
        if (approval && NailheadBot.databaseActive) {
            output = new String[18];
            output[15] = "add [nickname]";
            output[16] = "getstats [nickname]";
            output[17] = "statedit [nickname] [stat] [value]";
        } else {
            output = new String[15];
        }
        output[0] = "nailhelp";
        output[1] = "baba";
        output[2] = "ping [user] [message]";
        output[3] = "badass";
        output[4] = "fuckyou";
        if (anarchy) {
            output[4] = "fuckyou [user]";
        }
        output[5] = "nailhead";
        output[6] = "roll [sides]";
        output[7] = "everyone";
        output[8] = "join";
        output[9] = "leave";
        output[10] = "play [title]";
        output[11] = "pause";
        output[12] = "skip";
        output[13] = "tracklist";
        output[14] = "8ball";

        return output;
    }

    public static String[] getCmdDescriptions(boolean approval) {
        String[] output;
        if (approval && NailheadBot.databaseActive) {
            output = new String[18];
            output[15] = "adds a character to the stored database";
            output[16] = "get all the stats of a character";
            output[17] = "edit a stat for a character";
        } else {
            output = new String[15];
        }
        output[0] = "returns this table lmao";
        output[1] = "booey";
        output[2] = "get pinged idiot";
        output[3] = "\uD83D\uDE0E";
        output[4] = "no fuck you bitch";
        output[5] = "nailhead";
        output[6] = "rolls a specified sided die, default 20";
        output[7] = "I will ping everyone on your behalf";
        output[8] = "I join your vc";
        output[9] = "aight imma head out";
        output[10] = "start playing the specified track";
        output[11] = "hold on pause the music";
        output[12] = "this track sucks let's hear the next one";
        output[13] = "here's every song I can play";
        output[14] = "i tell you the future or something";

        return output;
    }

//    public static void makeThread(Message message) {
//        ThreadChannelAction _makeThread = message.createThreadChannel("ZOO WEE MAMA");
//        final ThreadChannel[] localChannel = {null};
//        _makeThread.queue(
//                thread->{
//                    thread.sendMessage("ZOO WEE MAMA!!").queue();
//                    localChannel[0] = thread;
//                },
//                error -> {
//                    logger.warn("oopsies the thread didn't get saved right");
//                }
//        );
//    }

    public static void helpParse(String str, MessageChannel channel) {
        switch (str.toLowerCase()) {
            case "statedit":
                StringBuilder message = new StringBuilder("Valid stats to edit are: ");
                for (int i = 0; i < EditHelper.editableStats.length; i++) {
                    if (i == EditHelper.editableStats.length - 1) {
                        message.append("and ").append(EditHelper.editableStats[i]);
                    } else {
                        message.append(EditHelper.editableStats[i]).append(", ");
                    }
                }
                channel.sendMessage(message.toString()).queue();
                break;
            default:
                channel.sendMessage("That's not a command I can help you with!").queue();
                break;
        }
    }
}
