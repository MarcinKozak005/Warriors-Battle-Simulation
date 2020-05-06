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
        float DMGDealt = Math.min(Math.max((float) new Random().nextGaussian()*stdDMG + meanDMG, minDMG), maxDMG);
        if (myEnemy != null) myEnemy.hp -= DMGDealt;
    }

    private void moveAction()
    {
        setDirectionTo(myEnemy);
        float newX = x + velX;
        float newY = y + velY;
        moveWithoutCollisions(newX,newY,false);
    }

    private void regroupAction()
    {
        setDirectionTo(myRegiment);

        float newX = x + velX;
        float newY = y + velY;

        if (!willOverlapWithAnother(newX,newY,Infantry.infantryBlockSize)) {
            x = newX;
            y = newY;
        }
    }

    private void retreatAction()
    {
        if(getDistanceTo(myEnemy)<4*safeArea) {
            setDirectionTo(myEnemy);
            float newX = x + (-1) * velX;
            float newY = y + (-1) * velY;
            moveWithoutCollisions(newX,newY,true);
        }
        else
        {
            setDirectionToNearestEdge();
            float newX = x + velX;
            float newY = y + velY;
            moveWithoutCollisions(newX,newY,false);
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
    void retreatOrder(Regiment enemyRegiment) {
        ArmyUnit enemy = this.findNearestEnemyIn(enemyRegiment);
        ArmyUnit enemyInSafeArea = getEnemyInSafeArea();

        if(enemyInSafeArea!=null)
        {
            this.myEnemy = enemyInSafeArea;
            this.unitAction = UnitAction.ATTACK;
        }
        else {
            this.myEnemy = enemy;
            this.unitAction = UnitAction.RETREAT;
        }
    }

    @Override
    public void tick()
    {
        if (this.hp <= 0 || this.notInTheBattlefield())
            myRegiment.safeToRemove(this);
        else {
            if (this.unitAction == UnitAction.ATTACK) this.attackAction();
            else if (this.unitAction == UnitAction.MOVE_TO_ENEMY) this.moveAction();
            else if (this.unitAction == UnitAction.REGROUP) this.regroupAction();
            else if (this.unitAction == UnitAction.RETREAT) this.retreatAction();
        }
    }

    @Override
    public void render(Graphics g) {
        if(this.alliance.equals(Alliance.Blue)) g.setColor(Color.BLUE);
        else g.setColor(Color.RED);
        g.fillRect((int)(x - infantryBlockSize/2),(int) (y - infantryBlockSize/2),(int)infantryBlockSize,(int)infantryBlockSize);
    }
}
