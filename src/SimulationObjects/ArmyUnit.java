package SimulationObjects;

import Enums.UnitAction;

import java.util.Optional;

public abstract class ArmyUnit extends SimulationObject
{
    protected float hp;
    protected float minDMG;
    protected float maxDMG;
    protected float attackRange;
    protected Regiment myRegiment;
    protected ArmyUnit myEnemy;
    protected UnitAction unitAction;

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
}
