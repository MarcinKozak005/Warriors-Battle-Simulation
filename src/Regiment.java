import java.awt.*;
import java.util.*;
import java.util.List;

public class Regiment extends SimulationObject
{
    LinkedList<ArmyUnit> armyUnitList = new LinkedList<>();
    List<ArmyUnit> toRemove = new LinkedList<>();
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
        for (ArmyUnit armyUnit: armyUnitList) armyUnit.attackOrder(enemyRegiment);
        // Po drodze List.shuffle żeby w losowej kolejności może ... ?

        /*
        Zmiana, bo wywalało ConcurrentModificationException
        https://javastart.pl/baza-wiedzy/wyjatki/concurrentmodificationexception
        https://www.baeldung.com/java-concurrentmodificationexception
        */
        for (ArmyUnit armyUnit: armyUnitList) armyUnit.tick();
        this.removeDeadUnits();
    }

    public void safeToRemove(ArmyUnit armyUnit) {
        toRemove.add(armyUnit);
    }

    /**
     * Helper function to test functionalities easier. Not working as smoothly as desired.
     * Adds units to the regiment passed as the argument in number specified in number.
     * Possibilities of further modifications include allowing to pass class of units to add as an argument(only adds Infantry units for now).
     * @param number Number of units to populate the regiment with
     */
    public void populateRegimentWithUnits(int number) {
        Random random = new Random();
        for (int i = 0; i < number; i++) {
            this.addArmyUnit(new Infantry(this.x + (-25 + random.nextInt(10)*5), this.y + (-10 + random.nextInt(4))*5));
            System.out.println("Regiment " + this + "'s unit position: (" + armyUnitList.getLast().getPosition() + ")");
        }
    }

    /**
     * Adds side*side number of units to the regiment in position that forms a square formation.
     * @param side Number of units at the side of the square
     */
    public void formationSquare(int side) {
        for (int i = 20 * side; i > 0; i -= 20) {
            for (int j = 20 * side; j > 0; j -= 20) {
                this.addArmyUnit(new Infantry(this.x - 100 + i, this.y - 100 + j));
                System.out.println("Regiment " + this + "'s unit position: (" + armyUnitList.getLast().getPosition() + ")");
            }
        }
    }

    private void removeDeadUnits() {
        armyUnitList.removeAll(toRemove);
        toRemove.clear();
    }

    public void drawCircle(Graphics g, Color c, float radius)
    {
        g.setColor(c);
        g.drawOval((int)(x-radius),(int)(y-radius),(int)radius*2,(int)radius*2);
    }

    @Override
    public void render(Graphics g){
        g.setColor(Color.YELLOW);
        //g.fillRect((int) ((x - 7)/2),(int) ((y -7)/2),7,7);
        g.fillRect((int) (x - 7/2),(int) (y -7/2),7,7);

        drawCircle(g,Color.GREEN,regimentCenterRadius);
        drawCircle(g,Color.YELLOW,regimentRegroupRadius);
        drawCircle(g,Color.RED,regimentBorderRadius);

        for (ArmyUnit armyUnit: armyUnitList) armyUnit.render(g);
    }


}
