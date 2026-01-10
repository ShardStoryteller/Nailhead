package nailheadbot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class DecideHelper {
    public static final Random rand = new Random();
    public static String response(String message){
        //Split the message string
        String[] components = message.split(" ");

        //String build to return
        StringBuilder builder = new StringBuilder("Items decided:");

        //If too small
        if(components.length < 3){
            return "Format: n!decide [# items] [item1] [item2] ...";
        }

        //Store number to return as int
        int returns;
        try{
            returns = Integer.parseInt(components[1]);
        }
        catch(NumberFormatException e){
            return "Format: n!decide [# items] [item1] [item2] ...";
        }

        //Ensure enough items are listed
        if(components.length < returns + 2){
            return "Not enough items to decide between!";
        }

        //Put all items into an arraylist
        ArrayList<String> items = new ArrayList<>();
        items.addAll(Arrays.asList(components).subList(2, components.length));

        //For the requested number of returns
        for(int i = 0; i < returns; i++){
            //Get random element from list
            int element = rand.nextInt(items.size());
            //Add element to return string
            builder.append(" ").append(items.get(element));
            //Remove from arraylist
            items.remove(element);
        }

        //Return the message
        return builder.toString();
    }
}
