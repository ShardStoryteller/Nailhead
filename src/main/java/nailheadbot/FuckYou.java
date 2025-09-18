package nailheadbot;

import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Random;

public class FuckYou {
    public static String url = "/home/container/resources/images/rdj.png";

    public static String[] responses = {
            "You're mean >:(",
            "Sleep with one eye open tonight",
            "I'm too good for you",
            "Ok boomer now go take your meds",
            "silence liberal",
            "I bet you think you're real funny, don't you?",
            """
I know you have something to say and I know you are eager to say it. So, I will get right to the point: Shut the fuck up.
Nobody wants to hear it. Nobody will ever want to hear it. Nobody cares. And the fact that you thought that someone might care is honestly baffling to me.
I am not just telling you to just shut up, I am telling you to shut the FUCK up, and you need to hear it. This is a public service. I have nothing to gain except from this except from telling you exactly what YOU need to hear. And on that note, let me make this clear. This is not a broad message I am aiming at everyone, this is a message specifically pointed at YOU. That is right, YOU. You know who you are. And I am sick of your shit. We all are. The only good you will ever do for humanity is refusing to participate in it. You could take a vow of silence. You can join a Buddhist monastery. You can even just be a mime. Mimes are fun.
You really need to understand that you should shut the fuck up. Why do you keep speaking? I am genuinely curious. Why do you think you deserve to be heard?
The core of what I am getting at is that you are not a worthwhile person. You are not worth listening to. Everything that you could have said has been said before more elegantly, and more coherently. And it is not that everything has already been said, we still need people to have discourse, in order to say new things, and discover new things about ourselves and humanity, but YOU. You will never be one of these people. So, shut the fuck up.
But, I get it. I understand you want to respond to me, huh? You want to let me know how you feel. Go on. Reply to me. I will read every single word."""
    };

    public static void fuckYou(MessageReceivedEvent event) {
        MessageChannel ch = event.getChannel();
        Random random = new Random();
        int length = responses.length + 2;
        int index = random.nextInt(length);
        if (index == responses.length) {
            EmbedHelper eb = new EmbedHelper(ch, "You should not have done that.", url);
            eb.handleReply("rdj.png", event);
        } else if (index == responses.length+1){
            String IPFormat = "%d.%d.%d.%d";
            String fakeIP = String.format(IPFormat, random.nextInt(248), random.nextInt(248),
                    random.nextInt(248), random.nextInt(248));
            event.getMessage().reply(fakeIP).queue();
        }
        else{
            event.getMessage().reply(responses[index]).queue();
        }
    }
}
