package nailheadbot.battle_new;

import nailheadbot.database.DatabaseHelper_Placeholder;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;

public class Battle implements Rolls{
    // Discord users participating in the battle
    private final ArrayList<DiscordUser> battleUsers;
    // Controlled characters in the battle
    private final ArrayList<BattleCharacter> characterList;
    // Int to normalize speed
    private final int turn_stabilizer = 60;
    // Int to regulate enemy turns
    private final int enemy_turn_space = 3;
    // Controlled characters waiting for their turn
    private final ArrayList<BattleCharacter> turnChars;
    // AI characters waiting for their turn
    private final ArrayList<BattleCharacter> turnEnemies;

    // AI characters in the battle
    private ArrayList<BattleCharacter> enemyList;
    // The channel the battle is occurring in (thread if possible)
    private ThreadChannel threadChannel;
    // The backup channel
    private MessageChannel channel;
    // The battle's ID
    private String battleID;
    // The character currently taking a turn
    private BattleCharacter currentCharacter;
    // The move currently in progress


    private int charTurnNum;
    private int enemyTurnNum;
    private int turnsToEnemy;

    private boolean waitingForAction;

    public Battle() {
        this.battleUsers = new ArrayList<>();
        this.characterList = new ArrayList<>();
        this.enemyList = new ArrayList<>();
        this.turnChars = new ArrayList<>();
        this.turnEnemies = new ArrayList<>();
        charTurnNum = 0;
        enemyTurnNum = 0;
        turnsToEnemy = enemy_turn_space;
        waitingForAction = false;
        currentCharacter = null;
//        moveInProgress = null;
    }

    /// This method is called at the start of the battle to start everything
    public void runBattle(){
        // While the battle is still active
        while(NewBattleHandler.battleActive(this)) {
            // If not waiting for an action
            if (!waitingForAction) {
                // Do next turn
                doTurn();
            }
        }
        // Send completion message
        channel.sendMessage("Battle over!").queue();
    }

    /// This method handles the run of a single turn, determining if it's a
    /// controlled character or AI enemy turn
    public void doTurn(){
        //TODO: timed battle events


        // If it's an enemy turn
        if(turnsToEnemy == 0){
            // Do an enemy turn
            enemyTurn();
            // Reset turns to enemy
            turnsToEnemy = enemy_turn_space;
        }
        else{
            // Do a character turn
            characterTurn();
            // Decrease turns to enemy
            turnsToEnemy--;
        }
    }

    /// This method handles the turn for active user characters
    public void characterTurn(){
        // If battle is still active
        if(NewBattleHandler.battleActive(this)){
            // If no characters are waiting for their turn
            if(turnChars.isEmpty()){
                // Find the next set of characters who can take a turn
                addTurnCharacters();
            }
            // Get a random character from the waiting list
            currentCharacter = (BattleCharacter) select(turnChars);
            // Remove that character from the waiting list
            turnChars.remove(currentCharacter);
            // Send message notifying of character's turn
            channel.sendMessage(currentCharacter + "'s turn!").queue();
            // Mark waiting for action
            waitingForAction = true;
        }
    }

    /// This method handles the turn for AI controlled characters
    public void enemyTurn(){
        // If battle is still active
        if(NewBattleHandler.battleActive(this)){
            // If no enemies are waiting for their turn
            if(turnEnemies.isEmpty()) {
                // Find the next set of enemies who can take a turn
                addTurnEnemies();
            }
            // Get a random enemy from the waiting list
            currentCharacter = (BattleCharacter) select(turnEnemies);
            // Remove that character from the waiting list
            turnEnemies.remove(currentCharacter);
            // Send message notifying of character's turn
            channel.sendMessage(currentCharacter + "'s turn!").queue();
            // Pass to enemy turn method
            enemyTakeTurn();
        }
    }

    /// This method handles an enemy taking their turn
    public void enemyTakeTurn(){



    }

    /// This method handles a user inputting a battle related message
    public void processBattleMessage(MessageReceivedEvent event){
        // If the battle is waiting for an action
        if(waitingForAction){
            // Store the user speaking
            DiscordUser messageAuthor = getUserByName(event.getAuthor().getName());
            // If message was sent in the proper channel
            if(event.getChannel().equals(channel)){
                // If the user is participating in the battle
                if(messageAuthor == null){
                    channel.sendMessage("You aren't participating in this battle!").queue();
                }
                // If it is not the user's turn to act
                else if(messageAuthor.getID() != currentCharacter.getUserID()){
                    channel.sendMessage("It's not your turn to act!").queue();
                }
                // If there is a move in progress
                else if (false){

                }
                else{
                    // Pull message contents from string
                    String message = event.getMessage().getContentRaw();
                    // Choose action based on message
                    choose(message);
                }
            }
        }
    }

    /// This method handles an incoming generic attack declaration
    public void attack(String message){
        //Split message contents
        String[] contents = message.split(" ");
        //Get target character
        BattleCharacter attackTarget = getEnemyByName(contents[2]);
        //If target is not found
        if (attackTarget == null || attackTarget.isDowned()){
            // Send invalidation message
            channel.sendMessage(contents[2] + " is not a valid attack target!").queue();
        }
        else{
            // Character attacks
            channel.sendMessage(currentCharacter.attack(attackTarget)).queue();
            // No longer waiting for turn
            waitingForAction = false;
        }
    }

    /// This method handles an incoming generic defend declaration
    public void defend(){
        // Current character defends
        currentCharacter.addStatus(new StatusInstance(currentCharacter, BattleStatus.DEFENDING));
        // Send confirmation message
        channel.sendMessage(currentCharacter + " is now defending!").queue();
        // No longer waiting for turn
        waitingForAction = false;
    }

    /// This method decides which battle action occurs based on the user inputted message
    public void choose(String message) {
        //Split message contents
        String[] contents = message.split(" ");
        //Separate identifier
        String identifier = contents[1];
        //Choose based on identifier
        switch (identifier.toLowerCase()) {
            case "attack":
                //Get target character
                BattleCharacter attackTarget = getEnemyByName(contents[2]);
                //If target is not found
                if (attackTarget ==  null){
                    channel.sendMessage(contents[2] + " is not a valid attack target!").queue();
                }
                else{
                    currentCharacter.attack(attackTarget);
                }
                break;
            case "defend":
                //defend
                defend();
                break;
            case "move":
                // character moves to a different location

                break;
            case "special":
                // handle special move

                break;
            case "switch":
                //


                break;
            case "skip":
                channel.sendMessage(currentCharacter + " skips " + currentCharacter.getGender().getPossessive() + "turn!").queue();
                waitingForAction = false;
                break;
            default:
                channel.sendMessage("Not a valid action!").queue();
                break;
        }
    }

    /// This method adds eligible characters to the current turn queue
    public void addTurnCharacters(){
        // For every character in the battle
        for(BattleCharacter character : characterList){
            // If the character's speed works
            if(charTurnNum % (turn_stabilizer-character.getCurrSPD()) == 0){
                // Add the character to the list of characters to take their turn
                turnChars.add(character);
            }
        }
        // If turncharacters list is still empty
        if(turnChars.isEmpty()){
            // Increment the turn number
            charTurnNum++;
            // Run the method again
            addTurnCharacters();
        }
    }

    /// This method adds eligible enemies to the current turn queue
    public void addTurnEnemies(){
        // For every enemy in the battle
        for(BattleCharacter enemy : enemyList){
            // If the enemy's speed works
            if(enemyTurnNum % (turn_stabilizer-enemy.getCurrSPD()) == 0){
                // Add the character to the list of characters to take their turn
                turnEnemies.add(enemy);
            }
        }
        // If turnenemies list is still empty
        if(turnEnemies.isEmpty()){
            // Increment the turn number
            enemyTurnNum++;
            // Run the method again
            addTurnEnemies();
        }
    }

    /// This method adds a BattleCharacter object to the battle's character list
    public void addCharacter(MessageReceivedEvent event){
        String charName = event.getMessage().getContentRaw().split(" ")[2];
        BattleCharacter addChar = DatabaseHelper_Placeholder.getCharacter(charName);

        if(addChar == null){
            // Character does not exist
            event.getChannel().sendMessage(charName + " not found in character roster!").queue();
        }
        else if(characterList.contains(addChar)) {
            // Character already in list
            event.getChannel().sendMessage(charName + " is already participating!").queue();
        }
        else{
            // Add character to list
            characterList.add(addChar);
            // Add user
            addUser(event, addChar);
            // Send confirmation message
            event.getChannel().sendMessage(charName + " has entered the battlefield!").queue();
        }
    }

    /// This method adds a DiscordUser object to the battle
    public void addUser(MessageReceivedEvent event, BattleCharacter character){
        String userName = event.getAuthor().getName();
        boolean newUser = false;

        for(DiscordUser user : battleUsers){
            if(user.getUsername().equals(userName)){
                newUser = true;
            }
        }
        if(newUser){

        }
    }

    /// This method sets the channel using a thread channel
    public void setChannel(ThreadChannel channel){
        this.channel = channel;
        this.threadChannel = channel;
    }
    /// This method sets the channel using a generic message channel
    public void setChannel(MessageChannel channel){
        this.channel = channel;
    }
    /// This method gets the thread channel, if it exists
    public ThreadChannel getThreadChannel(){
        return threadChannel;
    }
    /// This method gets the messagechannel version of the battle channel
    public MessageChannel getChannel(){
        return channel;
    }
    /// This method replaces the battle's enemy list with a new array of enemies
    public void setEnemyList(ArrayList<BattleCharacter> enemyList){
        this.enemyList = enemyList;
    }
    /// This method stores the battleID string
    public void setBattleID(String battleID){
        this.battleID = battleID;
    }
    /// This method returns the battleID string
    public String getBattleID(){
        return battleID;
    }
    /// This method adds a single enemy to the battle using its BattleEnemy enum
    public void addEnemy(BattleEnemy enemy){
        enemyList.add(enemy.getInstance());
    }
    /// This method returns true if a character is already participating in the battle
    public boolean characterEntered(int charID){
        for(BattleCharacter character : characterList){
            if(character.getCharID() == charID){
                return true;
            }
        }
        return false;
    }
    /// This method returns the appropriate DiscordUser given a username
    public DiscordUser getUserByName(String username) {
        for (DiscordUser battleUser : battleUsers) {
            if (battleUser.getUsername().equals(username)) {
                return battleUser;
            }
        }
        return null;
    }
    /// This method returns a BattleCharacter given the character's name as a string
    public BattleCharacter getCharacterByName(String characterName) {
        for (BattleCharacter battleCharacter : characterList) {
            if (battleCharacter.toString().equals(characterName)) {
                return battleCharacter;
            }
        }
        return null;
    }
    /// This method returns a BattleCharacter given the character's name as a string
    public BattleCharacter getEnemyByName(String characterName) {
        for (BattleCharacter battleCharacter : enemyList) {
            if (battleCharacter.toString().equals(characterName)) {
                return battleCharacter;
            }
        }
        return null;
    }
    /// This method checks if two battle characters are different alignments
    public boolean enemies(BattleCharacter char1, BattleCharacter char2){
        return characterList.contains(char1) != characterList.contains(char2);
    }
}
