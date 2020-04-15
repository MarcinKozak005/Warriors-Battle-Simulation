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
