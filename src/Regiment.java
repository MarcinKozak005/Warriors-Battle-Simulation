import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class Regiment extends SimulationObject
{
    // Lista jednostek w Regimencie
    List<ArmyUnit> armyUnitList = new LinkedList<>();
    Regiment enemyRegiment; // Wrogi Pułk
    Handler handler; // handler obsluguje wszystkie obiekty (na razie w sumie wszystkie Regimenty)
    static final float regimentCenterRadius = 100; // W tym obszarze od pulku mają być przy ruchu
    static final float regimentRegroupRadius = 200; // Jak mają sie przegrupować, to mają się zbliżyć do Regimentu na taką odległośc
    static final float regimentBorderRadius = 300; // Obiekty odległe o więcej niż to, to obiekty dalekie. Jak jest ich dużo to trzeba się przegrupować (patrz 1 linię wyżej)


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

    // Patrzebne w jednym momencie xD Ctrl+ LPP
    // Optional<ArmyUnit> ma albo w sobie referencję ArmyUnit, albo null (w Haskellu było coś podobnego, ale nie pamiętam jak się nazywało xD)
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
            catch (CantFindEnemyRegiment e)
            {
                e.printStackTrace();
            }
        }

        //Tu będzie podejmowanie decyzji przez Regiment na podstawie "obserwacji"
        // Na razie mamy wykonywanie tego samego dla wszystkich xD Tu możesz zmienić moveToAttackOrder na inne Ordery i powinno w miarę działać xD
        for (ArmyUnit armyUnit: armyUnitList) armyUnit.moveToAttackOrder(enemyRegiment);
        // Wykonaj akcję na każdym Army unit
        // Po drodze List.shuffle żeby w losowej kolejności może ... ?
        for (ArmyUnit armyUnit: armyUnitList) armyUnit.tick();
    }

    // Potrzebne do rysowania tych naszych zakresów (ana górze tej klasy te static'y)
    public void drawCircle(Graphics g, Color c, float radius)
    {
        g.setColor(c);
        g.drawOval((int)(x-radius),(int)(y-radius),(int)radius*2,(int)radius*2);
    }

    @Override
    public void render(Graphics g){
        g.setColor(Color.YELLOW);
        g.fillRect((int)x,(int)y,7,7);

        // Nasze zakresy
        drawCircle(g,Color.GREEN,regimentCenterRadius);
        drawCircle(g,Color.YELLOW,regimentRegroupRadius);
        drawCircle(g,Color.RED,regimentBorderRadius);

        for (ArmyUnit armyUnit: armyUnitList) armyUnit.render(g);
    }
}
