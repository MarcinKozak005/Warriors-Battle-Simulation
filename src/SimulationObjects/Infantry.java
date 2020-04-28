package SimulationObjects;

import Enums.Alliance;
import Enums.UnitAction;

import java.awt.*;
import java.util.Random;

public class Infantry extends ArmyUnit {

    public static final float infantryBlockSize = 5;

    public Infantry(float x, float y) {
        super(x, y);
        this.hp = 100;
        this.minDMG = 0;
        this.maxDMG = 25;
        this.meanDMG = 12;
        this.stdDMG = 6;
        this.attackRange = infantryBlockSize+infantryBlockSize/2;
        this.maxVelocity = 1;
        this.safeArea = this.attackRange;
    }

    private void attackAction() {
        this.dealDMGToEnemy();
    }

    private void dealDMGToEnemy() {
        // dealt damage calculated as a random number from normal distribution (mean = meanDMG, std = stdDMG),
        // damage has to be greater or equal minDMG and can't exceed maxDMG
        float DMGDealt = Math.min(Math.max((float) new Random().nextGaussian()*stdDMG + meanDMG, minDMG), maxDMG);
        if (myEnemy != null) {
            myEnemy.hp -= DMGDealt;
            //System.out.println(this + " dealt: "+ DMGDealt+" to: "+ myEnemy);
        }
    }

    private void moveAction()
    {
        setDirectionTo(myEnemy);

        float newX = x + velX;
        float newY = y + velY;

        if (!willOverlapWithAnother(newX, newY)) {
            x = newX;
            y = newY;
        }
        else {
            setAlternativeDirectionTo(myEnemy);
            newX = x + velX;
            newY = y + velY;
            // second and third conditions are patching the bug with soldiers appearing VERY far away from their regiment
            if (!willOverlapWithAnother(newX, newY)&& Math.abs(this.x - newX) < 20 && Math.abs(this.y - newY) < 20) {
                x = newX;
                y = newY;
            } else {
                // not sure about usage of this one maybe myEnemy = this.findNearestEnemyIn(myEnemy.myRegiment); would be a better fix
                myEnemy = getEnemyInSafeArea();
            }
        }
    }

    private void regroupAction()
    {
        // Praktycznie identyczne jak SimulationObjects.Infantry.moveAction()
        setDirectionTo(myRegiment);

        float newX = x + velX;
        float newY = y + velY;

        long matches = myRegiment.armyUnitList.stream().filter(n -> (Math.sqrt(Math.pow(newX - n.x, 2) + Math.pow(newY - n.y,2)) < Infantry.infantryBlockSize)).count();

        if (matches <= 1) {
            x = newX;
            y = newY;
        }
    }

    @Override
    void attackOrder(Regiment regimentToAttack) {
        ArmyUnit enemy = this.findNearestEnemyIn(regimentToAttack);
        ArmyUnit enemyInSafeArea = getEnemyInSafeArea();

        if(enemyInSafeArea!=null)
        {
            this.myEnemy = enemyInSafeArea;
            this.unitAction = UnitAction.ATTACK;
        }
        else if (enemy == null) this.unitAction = null;
        else if(this.getDistanceTo(enemy) < this.attackRange)
        {
            this.myEnemy = enemy;
            this.unitAction = UnitAction.ATTACK;
        }
        else{
            this.myEnemy = enemy;
            this.unitAction = UnitAction.MOVE_TO_ENEMY;
        }
    }

    @Override
    void moveToAttackOrder(Regiment regimentToAttack)
    {
        ArmyUnit enemy = this.findNearestEnemyIn(regimentToAttack);
        ArmyUnit enemyInSafeArea = getEnemyInSafeArea();

        if(enemyInSafeArea!=null)
        {
            this.myEnemy = enemyInSafeArea;
            this.unitAction = UnitAction.ATTACK;
        }
        else if (enemy == null) this.unitAction = null;
        else if(this.getDistanceTo(enemy) < this.attackRange)
        {
            this.myEnemy = enemy;
            this.unitAction = UnitAction.ATTACK;
        }
        else if(this.getDistanceTo(this.myRegiment) >= Regiment.regimentCenterRadius )
        {
            this.unitAction = UnitAction.REGROUP;
        }
        else {
            this.myEnemy = enemy;
            this.unitAction = UnitAction.MOVE_TO_ENEMY;
        }
    }

    @Override
    void regroupOrder(){
        ArmyUnit enemy = this.findNearestEnemyIn(this.myRegiment.enemyRegiment);
        ArmyUnit enemyInSafeArea = getEnemyInSafeArea();

        if(enemyInSafeArea!=null)
        {
            this.myEnemy = enemyInSafeArea;
            this.unitAction = UnitAction.ATTACK;
        }
        else if (enemy == null) this.unitAction = null;
        else if(this.getDistanceTo(enemy) < this.attackRange)
        {
            this.myEnemy = enemy;
            this.unitAction = UnitAction.ATTACK;
        }
        else if(this.getDistanceTo(this.myRegiment) <= Regiment.regimentRegroupRadius )
        {
            this.myEnemy = enemy;
            this.unitAction = UnitAction.MOVE_TO_ENEMY;
        }
        else {
            this.unitAction = UnitAction.REGROUP;
        }
    }

    @Override
    public void tick()
    {
        if (this.hp <= 0)
            myRegiment.safeToRemove(this);
        else {
            if (this.unitAction == UnitAction.ATTACK) this.attackAction();
            else if (this.unitAction == UnitAction.MOVE_TO_ENEMY) this.moveAction();
            else if (this.unitAction == UnitAction.REGROUP) this.regroupAction();
        }
    }

    @Override
    public void render(Graphics g) {
        if(this.alliance.equals(Alliance.Blue)) g.setColor(Color.BLUE);
        else g.setColor(Color.RED);
        g.fillRect((int)(x - infantryBlockSize/2),(int) (y - infantryBlockSize/2),(int)infantryBlockSize,(int)infantryBlockSize);
    }
}
