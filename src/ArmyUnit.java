import java.util.Optional;

public abstract class ArmyUnit extends SimulationObject
{
    protected float hp; //  no HP xD
    protected float minDMG;
    protected float maxDMG;
    protected float attackRange; // zasięg ataku
    protected Regiment myRegiment; // do którego Regiment (Pulku) należe
    protected ArmyUnit myEnemy; // przechowywanie wybranego wroga do pozniejszego ataku
    protected UnitAction unitAction; // przechowywanie akcji ktora FAKTYCZNIE podejmnie jednostka

    public ArmyUnit(float x, float y) {
        super(x, y);
    }

    abstract void attackOrder(Regiment regimentToAttack); // Rozkaz Ataku od Regimentu
    abstract void moveToAttackOrder(Regiment regimentToAttack); // Rozkaz RuchuZAtakiem od Regimentu
    abstract void regroupOrder(); // Rozkaz Przegrupowania od Regimentu

    // Znajdz mi najbliższego wroga z enemyRegiment'u
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
