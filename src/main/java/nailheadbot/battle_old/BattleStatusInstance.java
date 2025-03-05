//package nailheadbot.battle_old;
//
//import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
//
//public class BattleStatusInstance {
//    //Character that used the status-causing move
//    private final BattleCharacter user;
//    //Character affected by the status-causing move
//    private final BattleCharacter target;
//    //Status being instantiated
//    private final BattleStatus status;
//    //Modification to atk stat
//    private final int atkMod;
//    //Modification to def stat
//    private final int defMod;
//    //Modification to spd stat
//    private final int speedMod;
//    //Health or damage applied per turn
//    private int healMod;
//    //Whether target gets paralyzed
//    private final boolean paralyzes;
//    //Whether target is allowed to heal
//    private final boolean cantHealMod;
//    //Whether this status swap's the character's alignment
//    private final boolean swapsAlignment;
//    //Whether this status
//
//    //Elemental type of the move
//    private final String damageType;
//    //How many turns the status lasts
//    private int turnsToEnd;
//
//    public BattleStatusInstance(BattleCharacter user, BattleCharacter target, int[] statMods, int turnsToEnd, String type) {
//        this.user = user;
//        this.target = target;
//        this.atkMod = statMods[0];
//        this.defMod = statMods[1];
//        this.speedMod = statMods[2];
//        this.healMod = statMods[3];
//        this.paralyzes = statMods[4] == 1;
//        this.cantHealMod = statMods[5] == 1;
//        this.swapsAlignment = statMods[6] == 1;
//        this.turnsToEnd = turnsToEnd;
//        switch(type){
//            case "fire":
//                this.damageType = "burn";
//                break;
//            case "dark":
//                this.damageType = "corruption";
//                break;
//            case "ice":
//                this.damageType = "frostbite";
//                break;
//            case "electric":
//                this.damageType = "shock";
//                break;
//            default:
//                this.damageType = "poison";
//                break;
//        }
//    }
//
//    public void effectMessage(MessageChannel channel, String stat, boolean ending){
//        // full message to send to the channel
//        String message = target.getName();
//        // whether the stat is increasing or decreasing, if applicable
//        boolean increasing = false;
//        // the numerical change of the stat, if applicable
//        int statChange = 0;
//
//        // determine which stat is being affected
//        switch (stat){
//            case "attack":
//                // if the move initially increases attack
//                // OR the move is ending AND the move initially decreases attack
//                increasing = (atkMod > 0) || (atkMod < 0 && ending);
//                // store the stat change
//                statChange = Math.abs(atkMod);
//                break;
//            case "defense":
//                // if the move initially increases defense
//                // OR the move is ending AND the move initially decreases defense
//                increasing = (defMod > 0) || (defMod < 0 && ending);
//                // store the stat change
//                statChange = Math.abs(defMod);
//                break;
//            case "speed":
//                // if the move initially increases speed
//                // OR the move is ending AND the move initially decreases speed
//                increasing = (speedMod > 0) || (speedMod < 0 && ending);
//                // store the stat change
//                statChange = Math.abs(speedMod);
//                break;
//            case "healing":
//                // if the move is regeneration
//                if (healMod > 0) {
//                    // if the move is ending
//                    if(ending) {
//                        // send ending message
//                        channel.sendMessage(message + "'s regeneration has ended!").queue();
//                    }
//                    // if move is starting
//                    else {
//                        // send starting message
//                        channel.sendMessage(message + " is now regenerating " + healMod +
//                                " hp per turn!").queue();
//                    }
//                }
//                // if move is not regeneration
//                else {
//                    // if the move is ending
//                    if(ending) {
//                        // send ending message
//                        channel.sendMessage(message + "'s " + damageType + " has ended!").queue();
//                    }
//                    // if move is starting
//                    else {
//                        // send starting message
//                        channel.sendMessage(message + " is now taking " + -healMod + " " + damageType +
//                                " damage per turn!").queue();
//                    }
//                }
//                // exit the method
//                return;
//            case "paralysis":
//                // get paralysis type
//                String paralysisType = switch (damageType) {
//                    case "frostbite" -> "frozen";
//                    case "corruption", "shock", "poison" -> "paralyzed";
//                    default -> "asleep";
//                };
//
//                if(ending){
//                    message += " is no longer " + paralysisType + " and can move once again!";
//                }
//                else{
//                    message += " is now " + paralysisType + " and cannot move!";
//                }
//
//
//                // add the paralysis message to the message
//                if(damageType.equals("frostbite")) {
//                    // send message
//                    channel.sendMessage(message + " is now frozen and cannot move!").queue();
//                }
//                else{
//                    // send message
//                    channel.sendMessage(message + " is now paralyzed and cannot move!").queue();
//
//                }
//
//
//                // exit the method
//                return;
//            case "healblock":
//                // add the heal block message to the message
//                message += (ending) ? " is no longer prevented from healing!" : " is now prevented from healing!";
//                // send the message to the channel
//                channel.sendMessage(message).queue();
//                // exit the method
//                return;
//            case "hypnosis":
//                // add the hypnosis message to the message
//                message += (ending) ? " 's mind feels fuzzy...they've defected to the other side!" :
//                        "'s mind is clear! " + target.getPronoun(4) + " back on "
//                                + target.getPronoun(2).toLowerCase() + " original team!";
//                // send the message to the channel
//                channel.sendMessage(message).queue();
//                // exit the method
//                return;
//        }
//
//        // if still in method, then must be atk def or speed
//        // determine string for increasing
//        String inc = (increasing) ? "increased" : "decreased";
//        // send message
//        channel.sendMessage(target.getName() + "'s " + stat + " has " + inc + " by " + statChange + "!").queue();
//    }
//
//    public void activateEffects(MessageChannel channel) {
//        //send message
//        channel.sendMessage(user.getName() + " has left a status on " + target.getName()).queue();
//
//        // if the status affects attack
//        if(atkMod != 0){
//            // add a status to the target
//            target.status(0, atkMod);
//            // send message
//            effectMessage(channel, "attack", false);
//        }
//        // if the status affects defense
//        if(defMod != 0){
//            // add a status to the target
//            target.status(1, defMod);
//            // send message
//            effectMessage(channel, "defense", false);
//        }
//        // if the status affects speed
//        if(speedMod != 0){
//            // add a status to the target
//            target.status(2, speedMod);
//            // send message
//            effectMessage(channel, "speed", false);
//        }
//        // if the target regens or damages
//        if(healMod != 0){
//            // send message
//            effectMessage(channel, "healing", false);
//        }
//        // if the target gets paralyzed and is not already paralyzed
//        if(paralyzes){
//            // add a status to the target
//            target.status(3, 1);
//            // send message
//            effectMessage(channel, "paralysis", false);
//        }
//        // if the target is prevented from healing and is not already prevented from healing
//        if(cantHealMod){
//            // add a status to the target
//            target.status(4, 1);
//            // send message
//            effectMessage(channel, "healblock", false);
//        }
//        // if the target's alignment is swapped
//        if(swapsAlignment){
//            //INSERT METHOD TO SWAP ALIGNMENT
//
//            // send message
//            effectMessage(channel, "hypnosis", false);
//        }
//    }
//
//    public void onTurn(MessageChannel channel){
//        //if heals
//        if(healMod > 0){
//            //run regen
//            target.heal(healMod);
//            channel.sendMessage(target.getName() + " has regenerated " + healMod + " health!").queue();
//        }
//        //if damages
//        if(healMod < 0){
//            //run damage
//            target.damage(-healMod);
//            channel.sendMessage(target.getName() + " has taken " + -healMod + " " + damageType + " damage!").queue();
//        }
//    }
//
//    public void endNow(MessageChannel channel) {
//        channel.sendMessage(user.getName() + "'s status on " + target.getName() + " has ended!").queue();
//
//        if(atkMod != 0){
//            effectMessage(channel, "attack", true);
//            target.status(0, -atkMod);
//        }
//        if(defMod != 0){
//            effectMessage(channel, "defense", true);
//            target.status(1, -defMod);
//        }
//        if(speedMod != 0){
//            effectMessage(channel, "speed", true);
//            target.status(2, -speedMod);
//        }
//        if(healMod != 0){
//            effectMessage(channel, "healing", true);
//        }
//        if(paralyzes){
//            effectMessage(channel, "paralysis", true);
//            target.status(3, 0);
//        }
//        if(cantHealMod){
//            effectMessage(channel, "healblock", true);
//            target.status(4, 0);
//        }
//        if(swapsAlignment){
//            effectMessage(channel, "hypnosis", true);
//            //INSERT METHOD TO SWAP ALIGNMENT
//
//
//
//        }
//    }
//
//    public BattleCharacter getUser() {
//        return user;
//    }
//
//    public BattleCharacter getTarget(){
//        return target;
//    }
//
//    public int getTurnsToEnd() {
//        return turnsToEnd;
//    }
//
//    public void countDown(MessageChannel channel) {
//        //decrease the turns to end
//        turnsToEnd--;
//        //if the status's duration is up
//        if (turnsToEnd == 0) {
//            //end the status
//            endNow(channel);
//        }
//    }
//
//    public void endEarly() {
//        turnsToEnd = 0;
//    }
//}
