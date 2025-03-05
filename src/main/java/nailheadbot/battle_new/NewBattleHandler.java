package nailheadbot.battle_new;

import nailheadbot.database.DatabaseHelper_Placeholder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.ThreadChannelAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class NewBattleHandler {
    private static final Logger logger = LoggerFactory.getLogger(NewBattleHandler.class);
    // List of all active battles
    private static final List<Battle> battles = new ArrayList<>();

    /// This message takes the contents of the user's message and performs the associated actions
    public static void messageParse(MessageReceivedEvent event) {
        String identifier = event.getMessage().getContentRaw().split(" ")[1];

        // If message is trying to start a battle
        if (identifier.equalsIgnoreCase("start")) {
            // Initiate the battle
            initiateBattle(event);
        } else {
            // Get the battle in the given channel
            Battle activeBattle = getBattleFromChannel(event.getChannel());
            // If there is no battle
            if (activeBattle == null) {
                // No battle active
                event.getChannel().sendMessage("There is no battle happening in this channel, you silly billy!").queue();
            } else {
                // Parse command
                switch (identifier.toLowerCase()) {
                    case "end":
                        // Get the user who initiated the battle
                        String battleInitiator = event.getAuthor().getName();
                        // If battle not made by ScamStoryteller
                        if (!battleInitiator.equals("ScamStoryteller")) {
                            // User lacks permission to end the battle
                            event.getChannel().sendMessage("You don't have permission to end this battle, you silly billy!").queue();
                        } else {
                            // End the battle
                            endBattle(getBattleFromChannel(event.getChannel()));
                        }
                        break;



                }
            }
        }
    }

    /// Initiates a battle from a discord message
    public static void initiateBattle(MessageReceivedEvent event){
        // Initiate first setup
        firstSetup();
        // If channel is a thread
        if(event.getMessage().isFromType(ChannelType.GUILD_PUBLIC_THREAD)
                || event.getMessage().isFromType(ChannelType.GUILD_PRIVATE_THREAD)
                || event.getMessage().isFromType(ChannelType.GUILD_NEWS_THREAD)){
            // Display message
            event.getChannel().sendMessage("You can't start a battle from a thread!").queue();
            // Exit method
            return;
        }
        // Get the user who initiated the battle
        String battleInitiator = event.getAuthor().getName();
        // If battle not made by ScamStoryteller
        if(!battleInitiator.equals("ScamStoryteller")){
            // Battle can't be started
            event.getChannel().sendMessage("You don't have permission to start a battle, you silly billy!").queue();
            // Exit method
            return;
        }
        // Get battle ID from the message
        String battleID = event.getMessage().getContentRaw().split(" ")[1];
        // For every active battle
        for(Battle battle : battles){
            // If the battle ID of that battle equals the battle ID of the new battle
            if(battle.getBattleID().equals(battleID)){
                // Message Channel object
                MessageChannel battleChannel = null;
                // If the battle is in a thread
                if(battle.getThreadChannel() != null){
                    // Store the thread's parent channel
                    battleChannel = battle.getThreadChannel().getParentMessageChannel().asTextChannel();
                }
                else{
                    // Store the battle's channel
                    battleChannel = battle.getChannel();
                }
                // If the stored channel matches the message's channel
                if(event.getChannel().equals(battleChannel)){
                    // Send message stating battle is already active
                    event.getChannel().sendMessage("That battle is already active!").queue();
                    // Exit method
                    return;
                }
            }
        }
        // Create a new battle
        Battle battle = new Battle();
        // Add the new battle to the list of active battles
        battles.add(battle);
        // Set up the battle enemies and send message to the channel
        event.getChannel().sendMessage(enemySetup(battle, battleID)).queue();
        // If the battle is active
        if(battleActive(battle)){
            // Create the battle thread
            makeThread(event.getMessage(), battleID, battle);
        }
    }

    /// Returns a list of all currently active battles
    public static List<Battle> getActiveBattles(){
        return battles;
    }

    /// Returns a battle when given the active channel
    public static Battle getBattleFromChannel(MessageChannel channel){
        for(Battle battle : battles){
            if(battle.getChannel().equals(channel)){
                return battle;
            }
        }
        return null;
    }

    /// Check if a specific battle is currently active
    public static boolean battleActive(Battle battle){
        return battles.contains(battle);
    }

    /// Ends an active battle and removes it from the battle list
    public static void endBattle(Battle battle){
        battles.remove(battle);
    }



    /// This method creates a thread for the battle
    public static void makeThread(Message message, String battleID, Battle battle) {
        ThreadChannelAction _makeThread = message.createThreadChannel(battleID + " battle");
        _makeThread.queue(
                thread->{
                    thread.sendMessage("Battle is starting! Add your characters with (n!battle add [name])!").queue();
                    battle.setChannel(thread);
                },
                error -> {
                    logger.warn("oopsies the thread didn't get saved right");
                    battle.setChannel(message.getChannel());
                }
        );
    }

    /// Sets up enemies and battle ID for a given battle
    public static String enemySetup(Battle battle, String battleID){
        ArrayList<BattleCharacter> battleList = new ArrayList<>();

        switch(battleID.toLowerCase()){
            case "tutorial":
                battleList = BattleSetups.TUTORIAL.getEnemyList();
                break;
            case "chris":
                battleList = BattleSetups.CHRIS_FIGHT_1.getEnemyList();
                break;
            case "grace":
                battleList = BattleSetups.GRACE_FIGHT.getEnemyList();
                break;
            case "scarjia":
                battleList = BattleSetups.SCARJIA_FIGHT.getEnemyList();
                break;
            default:
                break;
        }
        if(!battleList.isEmpty()){
            battle.setEnemyList(battleList);
            battle.setBattleID(battleID);
            return "Preparing battle: " + battleID;
        }
        else{
            endBattle(battle);
            return "Sorry! " + battleID + " battle does not exist!";
        }
    }

    /// Ensures the database is set up with characters
    public static void firstSetup(){
        if(!DatabaseHelper_Placeholder.initiated()) {
            DatabaseHelper_Placeholder.addChars();
        }
    }


}
