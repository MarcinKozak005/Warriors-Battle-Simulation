import java.util.Optional;

public abstract class ArmyUnit extends SimulationObject
{
    protected float hp;
    protected float minDMG; //comment
    protected float maxDMG;
    protected float attackRange; // zasięg "rąk"
    protected Regiment myRegiment;
    protected ArmyUnit myEnemy; // przechowywanie wybranego wroga do pozniejszego ataku
    protected UnitAction unitAction; // przechowywanie akcji ktora FAKTYCZNIE podejmnie jednostka

    public ArmyUnit(float x, float y) {
        super(x, y);
    }

    abstract void attackOrder(Regiment regimentToAttack);
    abstract void moveToAttackOrder(Regiment regimentToAttack);
    abstract void regroupOrder();

    protected ArmyUnit findNearestEnemyIn(Regiment enemyRegiment){
        ArmyUnit myEnemy = null;
        Optional<ArmyUnit> myEnemyOptional = enemyRegiment.getFirstArmyUnit();

        if(myEnemyOptional.isPresent())
        {
            myEnemy = myEnemyOptional.get();
            double minimalDistance = this.getDistanceTo(myEnemy);
            for(ArmyUnit armyUnit: enemyRegiment.armyUnitList){
                if(this.getDistanceTo(armyUnit) <= minimalDistance)
                    myEnemy = armyUnit;
            }
        }
        return myEnemy;
    }
}
