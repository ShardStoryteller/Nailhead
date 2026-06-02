package nailheadbot;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageResponder {

    public static boolean messageParse(MessageReceivedEvent event, String parse){
        String regexMean1 = "((fuck|screw|hate) (you|u|off)? nailhead)";
        Pattern patternMean = Pattern.compile(regexMean1);
        Matcher matcherMean = patternMean.matcher(parse);

        String regexMean2 = "(nailhead (fuck|screw) (you|u|off)\s)";
        Pattern patternMean2 = Pattern.compile(regexMean2);
        Matcher matcherMean2 = patternMean2.matcher(parse);

        String regexMean3 = "(i (fucking)? hate (you|u)? nailhead)";
        Pattern patternMean3 = Pattern.compile(regexMean3);
        Matcher matcherMean3 = patternMean3.matcher(parse);

        String regexMean4 = "(nailhead i (fucking)? hate (yo)?u\s)";
        Pattern patternMean4 = Pattern.compile(regexMean4);
        Matcher matcherMean4 = patternMean4.matcher(parse);

        String regexMean5 = "(nailhead (yo)?u suck)";
        Pattern patternMean5 = Pattern.compile(regexMean5);
        Matcher matcherMean5 = patternMean5.matcher(parse);

        String regexMean6 = "((yo)?u suck nailhead)";
        Pattern patternMean6 = Pattern.compile(regexMean6);
        Matcher matcherMean6 = patternMean6.matcher(parse);

        String regexMean7 = "(nailhead (sucks|kys|should die))";
        Pattern patternMean7 = Pattern.compile(regexMean7);
        Matcher matcherMean7 = patternMean7.matcher(parse);

        String regexMean8 = "((die|kys) nailhead)";
        Pattern patternMean8 = Pattern.compile(regexMean8);
        Matcher matcherMean8 = patternMean8.matcher(parse);

        String regexMean9 = "(nailhead kill (yo)?urself)";
        Pattern patternMean9 = Pattern.compile(regexMean9);
        Matcher matcherMean9 = patternMean9.matcher(parse);

        String regexMean10 = "(kill (yo)?urself nailhead)";
        Pattern patternMean10 = Pattern.compile(regexMean10);
        Matcher matcherMean10 = patternMean10.matcher(parse);

        String regexNice1 = "(i (love|<3) (you|u)? nailhead)";
        Pattern patternNice = Pattern.compile(regexNice1);
        Matcher matcherNice = patternNice.matcher(parse);

        String regexNice2 = "(nailhead i (love|<3) (you|u)\s)";
        Pattern patternNice2 = Pattern.compile(regexNice2);
        Matcher matcherNice2 = patternNice2.matcher(parse);

        String regexNice3 = "(nailhead (ur|your|you're) (awesome|the best))";
        Pattern patternNice3 = Pattern.compile(regexNice3);
        Matcher matcherNice3 = patternNice3.matcher(parse);

        String regexNice4 = "(( ur|your|you're) (awesome|the best) nailhead)";
        Pattern patternNice4 = Pattern.compile(regexNice4);
        Matcher matcherNice4 = patternNice4.matcher(parse);

        String regexBreath1 = "(nailhead (ur|your|you're) breathtaking)";
        Pattern patternBreath1 = Pattern.compile(regexBreath1);
        Matcher matcherBreath1 = patternBreath1.matcher(parse);

        String regexBreath2 = "(( ur|your|you're) breathtaking nailhead)";
        Pattern patternBreath2 = Pattern.compile(regexBreath2);
        Matcher matcherBreath2 = patternBreath2.matcher(parse);

        String regexThanks = "(thank(s| you| u) nailhead)";
        Pattern patternThanks = Pattern.compile(regexThanks);
        Matcher matcherThanks = patternThanks.matcher(parse);

        if (matcherMean.find() | matcherMean2.find() | matcherMean3.find() | matcherMean4.find() |
                matcherMean5.find() | matcherMean6.find() | matcherMean7.find() | matcherMean8.find() |
                matcherMean9.find() | matcherMean10.find()){
            FuckYou.fuckYou(event);
            return true;
        }
        if (matcherNice.find() | matcherNice2.find() | matcherNice3.find() | matcherNice4.find()){
            event.getMessage().addReaction(Emoji.fromUnicode("U+1F60D")).queue();
            return true;
        }
        if(matcherBreath1.find() | matcherBreath2.find()){
            event.getMessage().reply("YOU'RE breathtaking!!").queue();
            return true;
        }
        if(matcherThanks.find()){
            event.getMessage().addReaction(Emoji.fromUnicode("U+1F44D")).queue();
            return true;
        }

        return false;
    }

    public static void pingDetected(MessageReceivedEvent event, String parse){
        String regexMean1 = "((fuck|screw) (you|u|off)\s)";
        Pattern patternMean1 = Pattern.compile(regexMean1);
        Matcher matcherMean1 = patternMean1.matcher(parse);

        String regexMean2 = "(i (fucking)? hate (yo)?u\s)";
        Pattern patternMean2 = Pattern.compile(regexMean2);
        Matcher matcherMean2 = patternMean2.matcher(parse);

        String regexMean3 = "(die\s|kys\s|kill yourself|kill urself)";
        Pattern patternMean3 = Pattern.compile(regexMean3);
        Matcher matcherMean3 = patternMean3.matcher(parse);

        String regexNice1 = "(i (love|<3) (you|u)\s)";
        Pattern patternNice1 = Pattern.compile(regexNice1);
        Matcher matcherNice1 = patternNice1.matcher(parse);

        String regexNice2 = "(( ur|your|you're) (awesome|the best)\s)";
        Pattern patternNice2 = Pattern.compile(regexNice2);
        Matcher matcherNice2 = patternNice2.matcher(parse);

        String regexBreath = "(( ur|your|you're) breathtaking\s)";
        Pattern patternBreath = Pattern.compile(regexBreath);
        Matcher matcherBreath = patternBreath.matcher(parse);

        String regexThanks = "(thank(s| you| u)\s)";
        Pattern patternThanks = Pattern.compile(regexThanks);
        Matcher matcherThanks = patternThanks.matcher(parse);

        String regexTrue = "(is this true)";
        Pattern patternTrue = Pattern.compile(regexTrue);
        Matcher matcherTrue = patternTrue.matcher(parse);

        if(matcherTrue.find()){
            event.getMessage().reply(magic8Ball.run8ball()).queue();
            return;
        }
        if(matcherMean1.find() | matcherMean2.find() | matcherMean3.find()){
            FuckYou.fuckYou(event);
            return;
        }
        if (matcherNice1.find() | matcherNice2.find()){
            event.getMessage().addReaction(Emoji.fromUnicode("U+1F60D")).queue();
            return;
        }
        if(matcherBreath.find()){
            event.getMessage().reply("YOU'RE breathtaking!!").queue();
            return;
        }
        if(matcherThanks.find()){
            event.getMessage().addReaction(Emoji.fromUnicode("U+1F44D")).queue();
        }
    }
}
