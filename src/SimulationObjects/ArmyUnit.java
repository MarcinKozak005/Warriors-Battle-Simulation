package SimulationObjects;

import Enums.UnitAction;

import java.awt.*;
import java.util.Optional;
import java.util.Random;

public abstract class ArmyUnit extends SimulationObject
{
    protected float hp;
    protected float minDMG;
    protected float maxDMG;
    protected float meanDMG;
    protected float stdDMG;
    protected float attackRange;
    protected Regiment myRegiment;
    protected ArmyUnit myEnemy;
    protected UnitAction unitAction;
    protected float safeArea; // attack enemy in this area regardless of the enemy's regiment

    public ArmyUnit(float x, float y) {
        super(x, y);
    }

    abstract void attackOrder(Regiment regimentToAttack);
    abstract void moveToAttackOrder(Regiment regimentToAttack);
    abstract void regroupOrder();
    abstract void retreatOrder(Regiment enemyRegiment);

    protected boolean willOverlapWithAnother(float newX, float newY, float blockSize) {
        long matches = myRegiment.armyUnitList.stream().filter(n -> (Math.sqrt(Math.pow(newX - n.x, 2) + Math.pow(newY - n.y,2)) < blockSize)).count();
        return (matches > 1);
    }

    public void moveWithoutCollisions(float newX, float newY, boolean retreat)
    {
        if (!willOverlapWithAnother(newX, newY, Infantry.infantryBlockSize)) {
            x = newX;
            y = newY;
        }
        else {
            setAlternativeDirectionTo(myEnemy);

            newX = x + velX*(retreat?-1:1);
            newY = y + velY*(retreat?-1:1);
            if (!willOverlapWithAnother(newX, newY, Infantry.infantryBlockSize)) {
                x = newX;
                y = newY;
            }
        }
    }

    protected void setAlternativeDirectionTo(SimulationObject simulationObject) {
        float diagonalDistance = (float) this.getDistanceTo(simulationObject);
        float distanceX = this.x - simulationObject.x;
        float distanceY = this.y - simulationObject.y;

        float a, b, newDistanceX, newDistanceY;

        if (new Random().nextBoolean()) {
            b = (float) (Math.pow(distanceY, 2) - distanceX*distanceY - Math.pow(diagonalDistance, 2)*Math.sin(Math.PI/2))/distanceX * (-1);
            newDistanceX = distanceY;
            newDistanceY = distanceY - (Double.isNaN(b)?0:b);
        } else {
            a = (float) (Math.pow(distanceX, 2) - distanceX*distanceY - Math.pow(diagonalDistance, 2)*Math.sin(Math.PI/2))/distanceY * (-1);
            newDistanceX = distanceX - (Double.isNaN(a)?0:a);
            newDistanceY = distanceX;
        }

        float newDiagonalDistance = (float) Math.sqrt(Math.pow(newDistanceX,2)+Math.pow(newDistanceY,2));
        this.velX = (-1) * newDistanceX * this.maxVelocity / newDiagonalDistance;
        this.velY = (-1) * newDistanceY * this.maxVelocity / newDiagonalDistance;
    }

    protected final ArmyUnit findNearestEnemyIn(Regiment enemyRegiment){
        ArmyUnit myEnemy = null;
        Optional<ArmyUnit> myEnemyOptional = enemyRegiment.getFirstArmyUnit();

        if(myEnemyOptional.isPresent())
        {
            myEnemy = myEnemyOptional.get();
            double minimalDistance = this.getDistanceTo(myEnemy);
            for(ArmyUnit armyUnit: enemyRegiment.armyUnitList){
                if(this.getDistanceTo(armyUnit) <= minimalDistance) {
                    minimalDistance = this.getDistanceTo(armyUnit);
                    myEnemy = armyUnit;
                }
            }
        }
        return myEnemy;
    }


    protected final ArmyUnit getEnemyInSafeArea()
    {
        double minDistance = safeArea + 10; // 10 has no meaning here- it hast to be 'big' number
        Point topLeft = new Point( (int)(this.x-safeArea), (int)(this.y-safeArea) );
        Point bottomRight = new Point( (int)(this.x+safeArea), (int)(this.y+safeArea) );
        ArmyUnit enemy = null;

        for(SimulationObject simulationObject: myRegiment.handler.simulationObjectList)
        {
            Regiment regiment = (Regiment) simulationObject;
            if(!regiment.alliance.equals(this.alliance) && regiment!=this.myRegiment.enemyRegiment)
            {
                for(ArmyUnit armyUnit: regiment.armyUnitList)
                {
                    if(armyUnit.inMySafeArea(topLeft,bottomRight) && armyUnit.getDistanceTo(this) <= minDistance)
                    {
                        minDistance = armyUnit.getDistanceTo(this);
                        enemy = armyUnit;
                    }
                }
            }
        }
        return enemy;
    }

    protected final boolean inMySafeArea(Point topLeft, Point bottomRight)
    {
        return this.x > topLeft.x && this.x < bottomRight.x && this.y > topLeft.y && this.y < bottomRight.y;
    }
}
