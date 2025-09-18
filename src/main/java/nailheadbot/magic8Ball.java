package nailheadbot;

import java.util.Random;

public class magic8Ball {
    public static final Random rand = new Random();
    public static String[] responses = {
            "yuh-huh",
            "correctamundo!",
            "so true bestie!!!",
            "survey says...*EXTREMELY LOUD CORRECT DING*",
            "YES! YES! YES!",
            "hell to the yeah",
            "idk probably",
            "yeah that seems about right",
            "nuh-uh",
            "nah not happening",
            "so false worstie!!!",
            "survey says...*EXTREMELY LOUD INCORRECT BUZZER*",
            "nope.",
            "seems sketchy to me chief",
            "who tf knows bruh",
            "huh? whuh huh?",
            "what do you think wise guy?",
            "ask me that shit again some other time",
            "i got no clue but did you know that the mitochondria is the powerhouse of the cell"
    };

    public static String run8ball() {
        int index = rand.nextInt(responses.length);
        return responses[index];
    }
}
