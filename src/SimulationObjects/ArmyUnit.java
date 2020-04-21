package SimulationObjects;

import Enums.UnitAction;

import java.awt.*;
import java.util.Optional;

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
    protected float safeArea; // jeżeli wróg jest w tym obszarze, to atakuj go niezależnie w jakim Regimencie jest.

    public ArmyUnit(float x, float y) {
        super(x, y);
    }

    abstract void attackOrder(Regiment regimentToAttack);
    abstract void moveToAttackOrder(Regiment regimentToAttack);
    abstract void regroupOrder();

    protected boolean willOverlapWithAnother(float newX, float newY) {
        long matches = myRegiment.armyUnitList.stream().filter(n -> (Math.sqrt(Math.pow(newX - n.x, 2) + Math.pow(newY - n.y,2)) < Infantry.infantryBlockSize)).count();
        return (matches > 1);

    }

    protected boolean setAlternativeDirectionTo(SimulationObject simulationObject) {
        // TODO nie musi nic zwracać
        // wyznacz prostą łączącą dwa obiekty
        float diagonalDistance = (float) this.getDistanceTo(simulationObject);
        float distanceX = this.x - simulationObject.x;
        float distanceY = this.y - simulationObject.y;

        float a, b, newDistanceX, newDistanceY;
        // sprawdź przypadek
        // TODO przypadki graniczne
//        if (Math.abs(this.x - simulationObject.x) < 20 || Math.abs(this.y - simulationObject.y) < 20) {
//            return false;
//        }
        if (Math.abs(this.y - simulationObject.y) < 40) {
            // idź pod kątem 45 stopni
            newDistanceX = distanceX/2;
            newDistanceY = newDistanceX;
            if (willOverlapWithAnother(this.x+newDistanceX, this.y+newDistanceY)){
                newDistanceY = (-1)*newDistanceY;
            }
        }
        if (Math.abs(this.x - simulationObject.x) < 40) {
            // idź pod kątem 45 stopni
            newDistanceY = distanceY/2;
            newDistanceX = newDistanceY;
            if (willOverlapWithAnother(this.x+newDistanceX, this.y+newDistanceY)){
                newDistanceX = (-1)*newDistanceX;
            }
        }
        if (simulationObject.x - this.x > 0) {
            a = distanceY-distanceX;
            b = (float) (Math.pow(distanceY, 2) - distanceX*distanceY - Math.pow(diagonalDistance, 2)*Math.sin(Math.PI/2))/distanceX * (-1);
            newDistanceX = distanceY;
            newDistanceY = distanceY - b;
        } else {
            b = distanceX-distanceY;
            a = (float) (Math.pow(distanceX, 2) - distanceX*distanceY - Math.pow(diagonalDistance, 2)*Math.sin(Math.PI/2))/distanceY * (-1);
            newDistanceX = distanceX - a;
            newDistanceY = distanceX;
        }
        this.velX = (-1)*(newDistanceX)*this.maxVelocity/diagonalDistance;
        this.velY = (-1)*(newDistanceY)*this.maxVelocity/diagonalDistance;

        return true;
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
        double minDistance = safeArea + 10; // równie dobrze może być +20 lub +50. Chodzi żeby było coś dużego.
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
