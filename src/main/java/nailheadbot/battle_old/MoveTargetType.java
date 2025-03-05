//package nailheadbot.battle_old;
//
//public enum MoveTargetType {
//    ALL(true, true, true, true),
//    ALL_DISCRIMINATE(true, true, true, false),
//    ALLY(true, true, false, false),
//    ALLY_NOTSELF(false, true, false, false),
//    CAN_HURT_ALLY(false, true, false, true),
//    ENEMY(false, false, true, false),
//    ENEMY_RECOIL(false, false, true, false);
//
//    private final boolean useableOnSelf;
//    private final boolean targetsAllies;
//    private final boolean targetsEnemies;
//    private final boolean hurtsAllies;
////    private final boolean attacksEnemy;
////    private final boolean attacksAlly;
////    private final boolean boostsAllyAttack;
////    private final boolean boostsAllyDefense;
////    private final boolean boostsAllySpeed;
////    private final boolean regensAlly;
////    private final boolean lowersEnemyAttack;
////    private final boolean lowersEnemyDefense;
////    private final boolean lowersEnemySpeed;
////    private final boolean poisonsAlly;
////    private final boolean defensePoint;
////    private final boolean healPoint;
//
//    MoveTargetType(
//            boolean useableOnSelf, boolean targetsAllies, boolean targetsEnemies, boolean hurtsAllies
//    ) {
//        this.useableOnSelf = useableOnSelf;
//        this.targetsAllies = targetsAllies;
//        this.targetsEnemies = targetsEnemies;
//        this.hurtsAllies = hurtsAllies;
////        this.attacksEnemy = attacksEnemy;
////        this.defensePoint = defense;
////        this.healPoint = heal;
//    }
//
//    public boolean getAttackPoint(float power){
//
//
//
//
//        return (power > 0 && targetsEnemies);
//    }
//
//    public boolean getDefensePoint(){
//        return defensePoint;
//    }
//
//    public boolean getHealPoint() {
//        return healPoint;
//    }
//
//    public boolean healsAllies(float power){
//        // true if targets allies, doesn't hurt allies, and has power above 0
//        return targetsAllies && !hurtsAllies && (power > 0);
//    }
//
//    public boolean canUseOn(BattleHelper battle, BattleCharacter user, BattleCharacter[] targets){
//        // for each target
//        for (BattleCharacter target : targets){
//            // if the move targets allies, and the user and target are opposite alignment
//            if(targetsAllies && battle.isEnemy(user) != battle.isEnemy(target)){
//                // return false
//                return false;
//            }
//            // if the move targets enemies, and the user and target are identical alignment
//            if(targetsEnemies && battle.isEnemy(user) == battle.isEnemy(target)){
//                // return false
//                return false;
//            }
//            // if the move can't be used on self, and user and target are identical
//            if(!useableOnSelf && user.getCharID() == target.getCharID()){
//                // return false
//                return false;
//            }
//        }
//        return true;
//    }
//}
