package nailheadbot;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;

public class EventTracker {

    public ArrayList<Event> eventList = new ArrayList<Event>();



    public void messageParse(MessageReceivedEvent event) {
        //Parses a command message



    }




    public void createEvent(User creator, Guild guild, MessageChannel channel, String roleName){
       //Creates a new event




    }

    public void notifyEvent(){




    }



}
