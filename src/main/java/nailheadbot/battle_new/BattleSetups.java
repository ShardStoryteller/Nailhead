package nailheadbot.battle_new;

import java.util.ArrayList;

public enum BattleSetups {
    TUTORIAL(BattleEnemy.DARK_AB1.getInstance(), BattleEnemy.DARK_AB1.getInstance(), BattleEnemy.DARK_AB1.getInstance()),
    CHRIS_FIGHT_1(BattleEnemy.DARK_CHRIS_1.getInstance()),
    GRACE_FIGHT(BattleEnemy.DARK_GRACE.getInstance()),
    SCARJIA_FIGHT(BattleEnemy.SCARJIA.getInstance()),
    MECCHRIS_FIGHT_1(BattleEnemy.DARK_MECCHRIS_V1.getInstance()),
    KORDOCK_FIGHT(BattleEnemy.DARK_KORDOCK.getInstance()),
    LIGHT_GABBY_FIGHT(BattleEnemy.GABBY_CLONE.getInstance(), BattleEnemy.GABBY_CLONE.getInstance(), BattleEnemy.GABBY_CLONE.getInstance()),
    TWILIGHT_FIGHT(BattleEnemy.TWILIGHT.getInstance()),
    CHRIS_FIGHT_2(BattleEnemy.DARK_CHRIS_1.getInstance(), BattleEnemy.DARK_PROGRAM.getInstance(), BattleEnemy.DARK_QUERY.getInstance()),
    TD_FIGHT(BattleEnemy.DARK_TD.getInstance()),
    ZACHARY_FIGHT(BattleEnemy.DARK_ZACHARY.getInstance()),
    JANE_FIGHT(BattleEnemy.JANE.getInstance(), BattleEnemy.GABBY_CLONE.getInstance(), BattleEnemy.GABBY_CLONE.getInstance()),
    NYX_FIGHT(BattleEnemy.NYX.getInstance()),
    MECCHRIS_FIGHT_2(BattleEnemy.DARK_MECCHRIS_V1.getInstance()),
    CHRIS_FINAL_FIGHT(BattleEnemy.DARK_CHRIS_2.getInstance()),
    FINAL_FIGHT(BattleEnemy.JESSICA.getInstance(), BattleEnemy.TWILIGHT.getInstance()),
    SECRET(BattleEnemy.NAILHEAD.getInstance());

    private final ArrayList<BattleCharacter> enemyList = new ArrayList<>();

    BattleSetups(BattleCharacter enemy){
        this.enemyList.add(enemy);
    }
    BattleSetups(BattleCharacter enemy1, BattleCharacter enemy2){
        this.enemyList.add(enemy1);
        this.enemyList.add(enemy2);
    }
    BattleSetups(BattleCharacter enemy1, BattleCharacter enemy2, BattleCharacter enemy3){
        this.enemyList.add(enemy1);
        this.enemyList.add(enemy2);
        this.enemyList.add(enemy3);
    }

    public ArrayList<BattleCharacter> getEnemyList(){
        return enemyList;
    }
}
