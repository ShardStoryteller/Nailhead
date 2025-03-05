//package nailheadbot.battle_old;
//
//import nailheadbot.battle_new.Rolls;
//import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Random;
//
//public class BattleCharacter implements Rolls {
//    // character's unique id
//    private final int charID;
//    // discord user's unique id
//    private final int userID;
//    // character's name
//    private final String name;
//    // character's pronouns
//    // 0 = He/She/They
//    // 1 = Him/Her/Them
//    // 2 = His/Her/Their
//    // 3 = His/Hers/Theirs
//    // 4 = He's/She's/They're
//    private final String[] pronouns;
//    // character's maximum hp
//    private final int maxHP;
//    // character's maximum sp
//    private final int maxSP;
//    // character's default attack value
//    private final int defaultATK;
//    // character's default defense value
//    private final int defaultDEF;
//    // character's default speed value
//    private final int defaultSPEED;
//    // character's friendship index
//    private final int[] friendshipIDX;
//    // character's modifiers from soul trait
//    private final int[] traits;
//    // list of enemies the character has been attacked by this battle and the revenge against them
//    private final HashMap<Integer, Integer> attackHash;
//    // list of statuses affecting the character
//    private final ArrayList<BattleStatusInstance> activeStatuses;
//
//    // character's current attack value
//    private int currATK;
//    // character's current defense value
//    private int currDEF;
//    // chracter's current speed value
//    private int currSPEED;
//    // character's current hp
//    private int currHP;
//    // character's current sp
//    private int currSP;
//    // whether the character is downed and cannot act
//    private boolean isDowned;
//    // whether the character is currently able to heal
//    private boolean canHeal;
//
//    // random generator
//    private final Random random = new Random();
//
//    public BattleCharacter(String name, String[] pronouns, int[] stats, int[] friendshipIDX, int[] traits) {
//        this.name = name;
//        this.pronouns = pronouns;
//        charID = stats[0];
//        userID = stats[1];
//        defaultATK = stats[2];
//        defaultDEF = stats[3];
//        defaultSPEED = stats[4];
//        maxHP = stats[5];
//        maxSP = stats[6];
//
//        currATK = defaultATK;
//        currDEF = defaultDEF;
//        currSPEED = defaultSPEED;
//        currHP = maxHP;
//        currSP = maxSP;
//
//        this.friendshipIDX = friendshipIDX;
//        this.traits = traits;
//        attackHash = new HashMap<>();
//        activeStatuses = new ArrayList<>();
//
//        isDowned = false;
//        canHeal = true;
//    }
//
//    public BattleCharacter(String name, String[] pronouns, int[] stats, int[] friendshipIDX, int[] currStats, int[] traits, boolean isDowned, boolean canHeal) {
//        this(name, pronouns, stats, friendshipIDX, traits);
//        currATK = currStats[0];
//        currDEF = currStats[1];
//        currSPEED = currStats[2];
//        currHP = currStats[3];
//        currSP = currStats[4];
//        this.isDowned = isDowned;
//        this.canHeal = canHeal;
//    }
//
//    public int attacked(int attackPower, int spdRoll, int attackerID) {
//        // damage taken
//        int damage = -1;
//
//        // if nat20 speed
//        if (spdRoll == 20) {
//            damage = (int) Math.round((float) attackPower * 1.5 / currDEF);
//        }
//        // if speed roll is above current speed
//        if (spdRoll == 20) {
//            damage = (int) Math.round((float) attackPower * 1.5 / currDEF);
//        }
//        // if speed roll is equal to current speed
//        if (spdRoll == currSPEED) {
//            damage = Math.round((float) (attackPower) / 2 / currDEF);
//        }
//        // apply damage
//        damage(damage);
//
//        //if not a self attack
//        if (attackerID != this.charID) {
//            //if the attacker exists in the map
//            if(attackHash.containsKey(attackerID)) {
//                //increase the revenge power
//                int power = attackHash.get(attackerID) + damage;
//                //store in the map
//                attackHash.put(attackerID, power);
//            }
//            //if the attacker does not exist in the map
//            else{
//                //add the attacker to the map
//                attackHash.put(attackerID, damage);
//            }
//        }
//        // return the damage number
//        return damage;
//    }
//
//    //outgoing attack method
//    public String attack(BattleCharacter target, int attackPower, int spdRoll) {
//        //calculate if the attack is a self hit
//        boolean selfHit = target.equals(this);
//
//        //karma modifier
//        if (traits[3] != 0) {
//            //get the revenge value for the target
//            double revenge = (double) (traits[3] * attackHash.get(target.getCharID())) /60;
//
//
//
//
//        }
//
//        //get the result damage
//        int resultDamage = target.attacked(attackPower, spdRoll, this.getCharID());
//
//
//
//        if (resultDamage == -1) {
//            return "The attack missed.";
//        }
//        if (selfHit) {
//            if (resultDamage == 0) {
//                return this.getName() + " whiffed the attack!";
//            } else {
//                return target.getName() + " tripped and hurt " + this.getPronoun(4) + " for " + resultDamage + " damage!";
//            }
//        }
//        if (resultDamage == 0) {
//            return "The attack was not strong enough to damage " + target.getName() + "!";
//        }
//        if (spdRoll == 20) {
//            return "CRITICAL HIT! " + target.getName() + " took " + resultDamage + " damage!";
//        }
//        return target.getName() + " took " + resultDamage + " damage!";
//    }
//
//    public void meleeAttack(MessageChannel channel, BattleCharacter target, int atkPower) {
//        int attackRoll = roll(20);
//        int spdRoll = roll(20);
//
//        //account for nat20 attackroll
//        if (attackRoll == 20) {
//            spdRoll = 20;
//        }
//
//        String result;
//
//        //account for nat0
//        if (attackRoll == 0) {
//            //hurt self
//            int selfDamage = (roll(4) + 1);
//            result = attack(this, selfDamage, spdRoll);
//        } else {
//            result = attack(target, this.currATK * attackRoll * atkPower, spdRoll);
//        }
//
//        channel.sendMessage(result).queue();
//    }
//
//    public void rangedAttack(MessageChannel channel, BattleCharacter target, int atkPower) {
//        int attackRoll = roll(20);
//        int spdRoll = roll(20) * 4 / 5;
//
//        //account for nat20 attackroll
//        if (attackRoll == 20) {
//            spdRoll = 20;
//        }
//
//        String result = attack(target, this.currATK * attackRoll * atkPower, spdRoll);
//
//        channel.sendMessage(result).queue();
//    }
//
//    public void multiAttack(MessageChannel channel, BattleCharacter[] targets, int atkPower, boolean ranged) {
//        for (BattleCharacter target : targets) {
//            if (ranged) {
//                rangedAttack(channel, target, atkPower);
//            } else {
//                meleeAttack(channel, target, atkPower);
//            }
//        }
//    }
//
//    public void soulAttack(MessageChannel channel, BattleCharacter target, int atkPower) {
//        //calculate attack power roll
//        int attackRoll = roll(20);
//        //calculate attack hit roll
//        int spdRoll = roll(20);
//
//        //account for nat20 attackroll
//        if (attackRoll == 20) {
//            spdRoll = 20;
//        }
//
//        //attack the target and store the resulting string
//        String result = attack(target, this.currATK * attackRoll * atkPower, spdRoll);
//
//        //send message to the channel
//        channel.sendMessage(result).queue();
//    }
//
//    public void healHandle(MessageChannel channel, BattleCharacter target, float healPower) {
//        //if the target can currently be healed
//        if(target.canHeal()){
//            //calculate the heal amount based on heal power
//            float healAmt = roll(6) * healPower;
//            //heal the user the correct heal amount
//            target.heal((int)healAmt);
//            //send message to the channel
//            channel.sendMessage(target.getName() + " has been healed " + (int)healAmt + " HP!").queue();
//        }
//        //if the target can't currently be healed
//        else{
//            //send message to the channel
//            channel.sendMessage(target.getName() + " is not currently able to be healed!").queue();
//        }
//    }
//
//    public int defend() {
//        int defendRoll = roll(6);
//
//        status(1, 6);
//
//
//        return defendRoll;
//    }
//
//
//    public int getUserID() {
//        return userID;
//    }
//
//    public int getCharID() {
//        return charID;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public String getPronoun(int index) {
//        return pronouns[index];
//    }
//
//    public int getMaxHP() {
//        return maxHP;
//    }
//
//    public int getMaxSP() {
//        return maxSP;
//    }
//
//    public int getATK() {
//        return currATK;
//    }
//
//    public int getDEF() {
//        return currDEF;
//    }
//
//    public int getSPEED() {
//        return currSPEED;
//    }
//
//    public int getHP() {
//        return currHP;
//    }
//
//    public int getSP() {
//        return currSP;
//    }
//
//    public int getDefaultATK() {
//        return defaultATK;
//    }
//
//    public int getDefaultDEF() {
//        return defaultDEF;
//    }
//
//    public int getDefaultSPEED() {
//        return defaultSPEED;
//    }
//
//    public void damage(int damage) {
//        currHP -= damage;
//        if (currHP < 0) {
//            currHP = 0;
//        }
//    }
//
//    public void heal(int heal) {
//        currHP += heal;
//        if (currHP > maxHP) {
//            currHP = maxHP;
//        }
//    }
//
//    public void spendSP(int points) {
//        currSP -= points;
//    }
//
//    public void recoverSP(int points) {
//        currSP += points;
//        if (currSP > maxSP) {
//            currSP = maxSP;
//        }
//    }
//
//    public void status(int stat, int value) {
//        switch (stat) {
//            //attack
//            case 0:
//                //modify current attack by given value
//                currATK += value;
//                //if attack goes below 1
//                if (currATK < 1) {
//                    // set attack to 1
//                    currATK = 1;
//                }
//                break;
//            //defense
//            case 1:
//                //modify current defense by given value
//                currDEF += value;
//                //if defense goes below 0
//                if (currDEF < 0) {
//                    //set defense to 0
//                    currDEF = 0;
//                }
//                break;
//            //speed
//            case 2:
//                //modify current speed by given value
//                currSPEED += value;
//                //if speed goes below 0
//                if (currSPEED < 0) {
//                    //set speed to 0
//                    currSPEED = 0;
//                }
//                break;
//            //paralysis
//            case 3:
//                //if user is not out of hp
//                if(currHP > 0){
//                    //set user as downed value
//                    isDowned = (value == 1);
//                }
//                //otherwise
//                else{
//                    //set user as downed
//                    isDowned = true;
//                }
//                break;
//            //healblock
//            case 4:
//                //set user as healable
//                canHeal = (value == 1);
//                break;
//            default:
//                //something goes here idk
//
//                break;
//        }
//    }
//
//    //get the revenge
//    public int getRevenge(int charID){
//        if(attackHash.containsKey(charID)){
//            return attackHash.get(charID);
//        }
//        return 0;
//    }
//
//    public int getFriendship(int charID) {
//        if (charID == this.charID) {
//            //stupid fuck. doing a team attack with yourself. idiot.
//            return Integer.MIN_VALUE;
//        }
//        try {
//            return friendshipIDX[charID];
//        } catch (IndexOutOfBoundsException e) {
//            return Integer.MIN_VALUE;
//        }
//    }
//
//    public int getTraitModifier(int tIDX) {
//        return traits[tIDX];
//    }
//
//    public boolean isDowned() {
//        return isDowned;
//    }
//
//    public boolean canHeal(){
//        return canHeal;
//    }
//}
