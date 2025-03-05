//package nailheadbot.battle_old;
//
//import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
//import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
//
//import java.util.ArrayList;
//import java.util.Random;
//
//public class BattleHelper {
//    // Discord users participating in the battle
//    private final BattleUser[] battleUsers;
//    // Storyteller-aligned characters in the battle
//    private final BattleCharacter[] characterList;
//    // Anti-Storyteller characters in the battle
//    private final BattleCharacter[] enemyList;
//    // List of all characters currently waiting for their turn
//    private final ArrayList<BattleCharacter> waitingForTurn;
//    // List of all currently active statuses
//    private final ArrayList<BattleStatusInstance> activeStatuses;
//    // The channel the battle is occurring in
//    private final MessageChannel channel;
//    // Random generator
//    private final Random random = new Random();
//
//
//    // Whether the battle is currently ongoing
//    private boolean battleActive;
//    // Whether the system is waiting for a user to choose a character's action
//    private boolean waitingForAction;
//    // The current move in progress, if there is one
//    private final BattleMove moveInProgress;
//    // The character currently taking their turn
//    private BattleCharacter turnChar;
//
//    public BattleHelper(MessageChannel channel, BattleUser[] battleUsers, BattleCharacter[] characterList, BattleCharacter[] enemyList) {
//        this.channel = channel;
//        this.battleUsers = battleUsers;
//        this.characterList = characterList;
//        this.enemyList = enemyList;
//        waitingForTurn = new ArrayList<>();
//        activeStatuses = new ArrayList<>();
//        battleActive = false;
//        waitingForAction = false;
//        moveInProgress = null;
//        turnChar = null;
//    }
//
//    public void battle(MessageReceivedEvent event) {
//        //if battle is triggered in the proper channel
//        if (event.getChannel().equals(channel)) {
//            //if the battle is already active
//            if (battleActive) {
//                channel.sendMessage("This battle is already active!").queue();
//            } else {
//                //mark battle as active
//                battleActive = true;
//                //begin first round
//                round();
//                //while the battle is active
//                while (battleActive) {
//                    //begin next round
//                    round();
//                }
//            }
//        }
//    }
//
//    public void round() {
//        //maximum speed
//        int turnSpd = 20;
//        //while the battle is active and turnSpd is 0 or above
//        while(battleActive&&turnSpd>=0){
//            //if not waiting for action
//            if(!waitingForAction){
//                //next character takes a turn
//                turn(turnSpd);
//                //character takes their turn and returns their speed
//                turnSpd = turnChar.getSPEED();
//            }
//        }
//    }
//
//    public void turn(int spd){
//        //get the next character in the turn order
//        turnChar = nextTurn(spd);
//        //send message notifying of character's turn
//        channel.sendMessage(turnChar.getName() + "'s turn!").queue();
//        //for every active status
//        for (BattleStatusInstance status : activeStatuses) {
//            //if the status's target is the current turn character
//            if (status.getTarget().equals(turnChar)){
//                //status onturn effects
//                status.onTurn(channel);
//            }
//            //if the status's caster is the current turn character
//            if (status.getUser().equals(turnChar)) {
//                //count down the turns for status to end
//                status.countDown(channel);
//            }
//        }
//        //if the character is an enemy
//        if(isEnemy(turnChar)){
//            //enemy takes a turn
//            enemyTurn();
//        }
//        else{
//            //mark as waiting for action
//            waitingForAction = true;
//        }
//    }
//
//    public void processBattleMessage(MessageReceivedEvent event) {
//        //Store the username of the user speaking
//        BattleUser battleUser = getUserByName(event.getAuthor().getName());
//
//        //If sent in the proper channel
//        if (event.getChannel().equals(channel)) {
//            //If the user is not in the battle
//            if (battleUser == null) {
//                channel.sendMessage("You aren't participating in this battle!").queue();
//            }
//            //If it is not the user's turn to act
//            else if (battleUser.getID() != turnChar.getUserID()) {
//                channel.sendMessage("It's not your turn to act!").queue();
//            }
//            //If there is a move in progress
//            else if (moveInProgress != null) {
//                //Process as a follow-up message
//                processFollowupMessage(event);
//            }
//            else
//            {
//                String message = event.getMessage().getContentRaw();
//                choose(message);
//            }
//        }
//    }
//
//    public void processFollowupMessage(MessageReceivedEvent event) {
//        String message = event.getMessage().getContentRaw();
//        String[] contents = message.split(" ");
//
//
//    }
//
//    public void choose(String message) {
//        //Split message contents
//        String[] contents = message.split(" ");
//        //Separate identifier
//        String identifier = contents[0].substring(2);
//        //Choose based on identifier
//        switch (identifier.toLowerCase()) {
//            case "attack":
//                //attack
//                BattleCharacter attackTarget = getCharacterByName(contents[1]);
//                boolean isRanged = contents[2].equalsIgnoreCase("ranged");
//                if (attackTarget.isDowned()) {
//                    channel.sendMessage(attackTarget.getName() + " is downed and cannot be targeted by an attack!").queue();
//                } else {
//                    if (isRanged) {
//                        turnChar.rangedAttack(channel, attackTarget, turnChar.getATK());
//                    } else {
//                        turnChar.meleeAttack(channel, attackTarget, turnChar.getATK());
//                    }
//                    waitingForAction = false;
//                }
//                break;
//            case "defend":
//                //defend
//
//                break;
//            case "magic":
//                handleMagic(message);
//                break;
//            case "special":
//                handleSpecial(message);
//                break;
//            case "talk":
//                //talk
//
//                break;
//            case "skip":
//                channel.sendMessage(turnChar.getName() + " skips " + turnChar.getPronoun(2) + "turn!").queue();
//                waitingForAction = false;
//                break;
//            default:
//                channel.sendMessage("Not a valid action!").queue();
//                break;
//        }
//    }
//
//    public void handleMagic(String message) {
//
//    }
//
//    public void handleSpecial(String message) {
//
//    }
//
//    public void followUp(String message) {
//
//    }
//
//    public void enemyTurn() {
//
//
//    }
//
//    public BattleCharacter nextTurn(int spdCount) {
//        BattleCharacter returnChar;
//
//        //if characters are currently waiting for their turn
//        if (!waitingForTurn.isEmpty()) {
//            //get a random character in the waiting index
//            int index = random.nextInt(waitingForTurn.size());
//            //store that character as the character to take the next turn
//            returnChar = waitingForTurn.get(index);
//            //remove that character from the index
//            waitingForTurn.remove(index);
//            //if that character is downed
//            if (!returnChar.isDowned()) {
//                //send a message indicating the character's turn was skipped
//                channel.sendMessage(returnChar.getName() + " is downed and cannot take " + returnChar.getPronoun(2) + " turn!").queue();
//                //run the method again
//                returnChar = nextTurn(spdCount);
//            }
//        }
//        //if there were no characters waiting
//        else {
//            //for every active user
//            for (BattleUser battleUser : battleUsers) {
//                //get their currently active character
//                BattleCharacter character = battleUser.getActiveCharacter();
//                //if that character's speed is equal to the current speed to check and the character is not downed
//                if (character.getSPEED() == spdCount) {
//                    //add that character to the waiting list
//                    waitingForTurn.add(character);
//                }
//            }
//            //for every active enemy
//            for (BattleCharacter character : enemyList) {
//                //if that character's speed is equal to the current speed to check
//                if (character.getSPEED() == spdCount) {
//                    //add that character to the waiting list
//                    waitingForTurn.add(character);
//                }
//            }
//            //run the method again with 1 less speed
//            returnChar = nextTurn(spdCount - 1);
//        }
//        return returnChar;
//    }
//
//    public void removeStatus(BattleStatusInstance status) {
//        activeStatuses.remove(status);
//    }
//
//    public BattleUser getUserByName(String username) {
//        for (BattleUser battleUser : battleUsers) {
//            if (battleUser.getUsername().equals(username)) {
//                return battleUser;
//            }
//        }
//        return null;
//    }
//
//    public BattleCharacter getCharacterByName(String characterName) {
//        for (BattleCharacter battleCharacter : characterList) {
//            if (battleCharacter.getName().equals(characterName)) {
//                return battleCharacter;
//            }
//        }
//        return null;
//    }
//
//    public boolean isEnemy(BattleCharacter character) {
//        for (BattleCharacter battleCharacter : enemyList) {
//            if (character.equals(battleCharacter)) {
//                return true;
//            }
//        }
//        return false;
//    }
//}
