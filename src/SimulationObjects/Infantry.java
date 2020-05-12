package SimulationObjects;

import Enums.Alliance;
import Enums.UnitAction;

import java.awt.*;

public class Infantry extends ArmyUnit {

    public static final double infantryBlockSize = 5;

    public Infantry(double x, double y) {
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

    @Override
    protected void attackAction() {
        this.dealDMGToEnemy();
    }

    @Override
    protected void moveAction()
    {
        setDirectionTo(myEnemy);
        double newX = x + velX;
        double newY = y + velY;
        moveWithoutCollisions(newX,newY,myEnemy,false);
    }

    @Override
    protected void regroupAction()
    {
        setDirectionTo(myRegiment);
        double newX = x + velX;
        double newY = y + velY;
        moveWithoutCollisions(newX,newY,myRegiment,false);
    }

    @Override
    protected void retreatAction()
    {
        if(getDistanceTo(myEnemy)<4*safeArea) {
            setDirectionTo(myEnemy);
            double newX = x + (-1) * velX;
            double newY = y + (-1) * velY;
            moveWithoutCollisions(newX,newY,myEnemy,true);
        }
        else
        {
            setDirectionToNearestEdge();
            double newX = x + velX;
            double newY = y + velY;
            moveWithoutCollisions(newX,newY,myEnemy,false);
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
