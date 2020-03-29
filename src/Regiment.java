import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class Regiment extends SimulationObject
{
    List<ArmyUnit> armyUnitList = new LinkedList<>();
    Regiment enemyRegiment;
    Handler handler;
    static final float regimentCenterRadius = 100;
    static final float regimentRegroupRadius = 200;
    static final float regimentBorderRadius = 300;


    public Regiment(float x, float y, Alliance alliance, Handler handler) {
        super(x, y, alliance);
        this.handler = handler;
    }

    public void addArmyUnit(ArmyUnit armyUnit)
    {
        armyUnit.myRegiment = this;
        armyUnit.alliance = this.alliance;
        armyUnitList.add(armyUnit);
    }

    public void removeArmyUnit(ArmyUnit armyUnit){
        armyUnitList.remove(armyUnit);
    }

    public Optional<ArmyUnit> getFirstArmyUnit()
    {
        try{
            return Optional.of(armyUnitList.get(0));
        }catch (IndexOutOfBoundsException e){
            return Optional.empty();
        }
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
        for (ArmyUnit armyUnit: armyUnitList) armyUnit.moveToAttackOrder(enemyRegiment);
        // Wykonaj akcję -> shuffle po drodze
        for (ArmyUnit armyUnit: armyUnitList) armyUnit.tick();
    }

    public void drawCircle(Graphics g, Color c, float radius)
    {
        g.setColor(c);
        g.drawOval((int)(x-radius),(int)(y-radius),(int)radius*2,(int)radius*2);
    }

    @Override
    public void render(Graphics g){
        g.setColor(Color.YELLOW);
        g.fillRect((int)x,(int)y,7,7);

        drawCircle(g,Color.GREEN,regimentCenterRadius);
        drawCircle(g,Color.YELLOW,regimentRegroupRadius);
        drawCircle(g,Color.RED,regimentBorderRadius);

        for (ArmyUnit armyUnit: armyUnitList) armyUnit.render(g);
    }
}
