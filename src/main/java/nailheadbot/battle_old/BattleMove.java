//package nailheadbot.battle_old;
//
//import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
//
//import java.util.Random;
//
//public class BattleMove {
//    //Name of the move
//    final String moveName;
//    //Which characters the move targets
//    final MoveTargetType targetType;
//    //Special move properties
//    final MoveSpecial[] special;
//    //Power of the move
//    final float power;
//    //Falloff with distance
//    final float falloff;
//    //Minimum distance to use the move on target
//    final float minDistance;
//    //Chance of each status
//    final float[] statusChances;
//    //Maximum amount of targets
//    final int maxTargets;
//    //Base soul point cost of the move
//    final int basePointCost;
//    //Turns of cooldown after use
//    final int cooldown;
//    //Elemental type(s) of the move
//    final Element[] element;
//    //Statuses the move can give
//    final BattleStatusInstance[] statuses;
//    //Whether the move is guaranteed to hit targets
//    final boolean homing;
//
//    private final Random random = new Random();
//
//    public BattleMove(String moveName, MoveTargetType targetType, int maxTargets, int basePointCost,
//                      Element[] element, float power, float falloff, BattleStatusInstance[] statuses,
//                      float[] statusChances, float minDistance, boolean homing, int cooldown,
//                      MoveSpecial[] special) {
//        this.moveName = moveName;
//        this.targetType = targetType;
//        this.maxTargets = maxTargets;
//        this.basePointCost = basePointCost;
//        this.element = element;
//        this.power = power;
//        this.falloff = falloff;
//        this.statuses = statuses;
//        this.statusChances = statusChances;
//        this.minDistance = minDistance;
//        this.homing = homing;
//        this.cooldown = cooldown;
//        this.special = special;
//    }
//
//    public boolean execute(MessageChannel channel, BattleHelper battle, BattleCharacter user, BattleCharacter[] targets) {
//        //calculate point cost
//        int pointCost = calculatePointCost(user);
//
//        if (pointCost > user.getSP()) {
//            channel.sendMessage("You don't have enough Soul Points to use that move!").queue();
//            return false;
//        }
//        if (targets.length > maxTargets) {
//            channel.sendMessage("That move can't target that many people!").queue();
//            return false;
//        }
//
//        //for each target
//        for(BattleCharacter target : targets) {
//            //if target is an ally
//            if(battle.isEnemy(user) == battle.isEnemy(target)){
//                //if the move targets allies, can't hurt allies, and has power above 0
//                if(targetType.healsAllies(power)){
//                    //heal target
//                    user.healHandle(channel, target, power);
//
//                }
//            }
//            //if target is an enemy
//            else{
//
//
//            }
//        }
//
//
//
//        //if healing move
//        if(heals){
//            //healing move logic
//            for(BattleCharacter target : targets){
//                user.healHandle(channel,target, power);
//                //handle the heal modifiers
//                if(ifSuccess()) {
//                    BattleStatusInstance healStatus = new BattleStatusInstance(user, target, modifiers, effectDuration, type);
//                    healStatus.activateEffects(channel);
//                }
//            }
//        }
//        else {
//            //attack move logic
//            for (BattleCharacter target : targets) {
//                user.soulAttack(channel, target, power);
//                //handle the attack modifiers
//                if (ifSuccess()) {
//                    BattleStatusInstance atkStatus = new BattleStatusInstance(user, target, modifiers, effectDuration, type);
//                    atkStatus.activateEffects(channel);
//                }
//            }
//        }
//
//        //spend points
//        user.spendSP(pointCost);
//        return true;
//    }
//
//    public int calculatePointCost(BattleCharacter user){
//        int pointCost = basePointCost;
//
//        // attack point change
//        if(targetType.getAttackPoint(power)){
//            pointCost += user.getTraitModifier(0);
//        }
//
//        // defense point change
//        if(targetType.getDefensePoint()){
//            pointCost += user.getTraitModifier(1);
//        }
//
//        // heal point change
//        if(targetType.getHealPoint()){
//            pointCost += user.getTraitModifier(2);
//        }
//        return pointCost;
//    }
//
//    public boolean ifSuccess() {
//        if (rollToSucceed > 0) {
//            int rollValue = random.nextInt(rollToSucceed) + 1;
//            return (rollValue >= successThreshold);
//        }
//        return true;
//    }
//}
