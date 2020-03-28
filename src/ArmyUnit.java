import java.awt.*;

public abstract class ArmyUnit extends SimulationObject
{
    protected float hp;
    protected float maxDMG;
    protected float minDMG;
    protected float attackRange; // zasięg "rąk"
    protected Regiment myRegiment;
    protected ArmyUnit myEnemy; // przechowywanie wybranego wroga do pozniejszego ataku
    protected UnitAction unitAction; // przechowywanie akcji ktora FAKTYCZNIE podejmnie jednostka
    protected Handler handler;

    public ArmyUnit(float x, float y, Handler handler) {
        super(x, y);
        this.handler = handler;
    }

    abstract void attackOrder(Regiment regiment);
    abstract void moveOrder(Point point);
    abstract void regroupOrder(Point point);

    protected ArmyUnit findNearestEnemy(Regiment enemyRegiment)
    {
        ArmyUnit myEnemy = null;
        // TODO Co gdy Regiment jest pusty ...? Czy może sie tak zdarzyć ?
        double minimalDistance = this.getDistanceTo(enemyRegiment.unitList.get(0));
        for(ArmyUnit armyUnit: enemyRegiment.unitList){
            if(this.getDistanceTo(armyUnit) <= minimalDistance)
            {
                myEnemy = armyUnit;
            }
        }
        return myEnemy;
    }

}
