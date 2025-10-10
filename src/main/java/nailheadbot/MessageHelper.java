package nailheadbot;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Random;

public class MessageHelper {
    private static final Random random = new Random();
    public static final String prefix = "n!";

    public static void handle(MessageReceivedEvent event, String message) {
        MessageChannel channel = event.getChannel();
        User user = event.getAuthor();
        String username = user.getName();
        String userId = user.getId();

        String[] components = message.split(" ", 3);

        String decider = components[0].substring(prefix.length()).toLowerCase();

        switch (decider) {
            case "help":
                channel.sendMessage("No. (use n!nailhelp)").queue();
                break;
            case "nailhelp":
                if (username.equals("shardstoryteller")) {
                    channel.sendMessage("There is no helping you.").queue();
                } else if (username.equals("nailheadreal")) {
                    channel.sendMessage("You don't need my help, king. \uD83D\uDCAA").queue();
                } else {
                    EmbedHelper eb = new EmbedHelper(channel, "Command list (Prefix : " + prefix + ")", getCommands(), getCmdDescriptions());
                    eb.handleValues();
                }
                break;
            case "baba":
                channel.sendMessage("booey").queue();
                break;
            case "ping":
                //blank string
                String pingMessage = "";
                //detects if user is being pinged
                String pingIdentifier = switch (components.length) {
                    //if no user specified, ping message sender
                    case 1 -> userId;
                    //if user specified, ping specified user
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
                if (components.length > 1) {
                    fuckIdentifier = components[1];
                } else {
                    fuckIdentifier = userId;
                }
                new PingerThread(event, 5, fuckIdentifier, fuckMessage).start();
                break;
            case "nailhead":
                Nailhead.nailhead(channel);
                break;
            case "badass":
                event.getMessage().addReaction(Emoji.fromUnicode("U+1F60E")).queue();
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
            case "8ball":
                channel.sendMessage(magic8Ball.run8ball()).queue();
                break;
            case "join":
                VoiceHelper.join(event);
                break;
            case "leave":
                VoiceHelper.leave(event);
                break;
//            case "dailynailhead":
//                //TODO: Daily Nailhead signup functionality
//                break;
            default:
                channel.sendMessage("That's not a command I can use!").queue();
                break;
        }
    }

    public static String[] getCommands() {
        String[] output = new String[8];
        output[0] = "nailhelp";
        output[1] = "baba";
        output[2] = "ping [user] [message]";
        output[3] = "badass";
        output[4] = "fuckyou [user]";
        output[5] = "nailhead";
        output[6] = "roll [sides]";
        output[7] = "8ball";
        //output[8] = "dailynailhead";
        return output;
    }

    public static String[] getCmdDescriptions() {
        String[] output = new String[8];
        output[0] = "returns this table lmao";
        output[1] = "booey";
        output[2] = "get pinged idiot";
        output[3] = "\uD83D\uDE0E";
        output[4] = "no fuck you bitch";
        output[5] = "nailhead";
        output[6] = "rolls a specified sided die, default 20";
        output[7] = "i tell you the future or something";
        //output[8] = "signs this channel up for the Daily Nailhead";
        return output;
    }
}
