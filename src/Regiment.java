import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class Regiment extends SimulationObject
{
    List<ArmyUnit> unitList = new LinkedList<>();
    Regiment enemyRegiment;
    Handler handler;

    public Regiment(float x, float y, Alliance alliance, Handler handler) {
        super(x, y, alliance);
        this.handler = handler;
    }

    @Override
    public void tick() {
        if (this.enemyRegiment == null)
        {
            try {
                this.enemyRegiment = handler.getEnemyRegimentFor(this.alliance);
            }
            catch (VictoryException e)
            {
                e.printStackTrace();
            }
        }

        //Wybierz akcje
        for (ArmyUnit armyUnit: unitList) armyUnit.attackOrder(enemyRegiment);
        // Wykonaj akcję -> shuffle po drodze
        for (ArmyUnit armyUnit: unitList) armyUnit.tick();
    }

    @Override
    public void render(Graphics g){
        // render Regiment indicator

        for (ArmyUnit armyUnit: unitList) armyUnit.render(g);
    }

    public void addArmyUnit(ArmyUnit armyUnit)
    {
        armyUnit.myRegiment = this;
        armyUnit.alliance = this.alliance;
        unitList.add(armyUnit);
    }
}
