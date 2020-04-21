package SimulationObjects;

import Enums.Alliance;
import Exceptions.CantFindEnemyRegiment;
import Simulation.Handler;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Regiment extends SimulationObject
{

    public static final float regimentBlockSize = 10;
    // Jak blisko musi być wrogi SimulationObjects.Regiment, by z move() przejśc na atak()
    public static final float regimentInRangeDistance = 100;
    static final float regimentCenterRadius = 150;
    static final float regimentRegroupRadius = 200;
    static final float regimentBorderRadius = 300;


    List<ArmyUnit> armyUnitList = new LinkedList<>();
    List<ArmyUnit> toRemove = new LinkedList<>();
    Regiment enemyRegiment;
    Handler handler;

    public Regiment(float x, float y, Alliance alliance, Handler handler) {
        super(x, y, alliance);
        this.handler = handler;
        this.maxVelocity = Float.MAX_VALUE;
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
        if (this.enemyRegiment == null || !handler.simulationObjectList.contains(this.enemyRegiment))
        {
            try {
                this.enemyRegiment = handler.getNearestEnemyFor(this);
            }
            catch (CantFindEnemyRegiment e)
            {
                System.out.println("-------- " + this.alliance.toString().toUpperCase() + "'S VICTORY --------");
                //e.printStackTrace();
                // Victory - Na pewno nie jest to najbardziej elegancki sposób xD
                throw new RuntimeException();
            }
        }

        // Decyzja Regimentu
        if (meanDistanceToRegiment() >= regimentBorderRadius){
            for (ArmyUnit armyUnit: armyUnitList) armyUnit.regroupOrder();
        }
        else if(this.getDistanceTo(this.enemyRegiment) > regimentInRangeDistance){
            for (ArmyUnit armyUnit: armyUnitList) armyUnit.moveToAttackOrder(enemyRegiment);

            // Z jaka predkoscią bedzie poruszał się SimulationObjects.Regiment
            this.maxVelocity = Float.MAX_VALUE;
            for (ArmyUnit armyUnit: armyUnitList)
                if(armyUnit.maxVelocity<this.maxVelocity) this.maxVelocity = armyUnit.maxVelocity;

            setDirectionTo(enemyRegiment);
            this.x += velX;
            this.y += velY;
        }
        else if (this.getDistanceTo(this.enemyRegiment) <= regimentInRangeDistance){
            for (ArmyUnit armyUnit: armyUnitList) armyUnit.attackOrder(enemyRegiment);
        }

        for (ArmyUnit armyUnit: armyUnitList) armyUnit.tick();

        this.removeDeadUnits();
        if(this.armyUnitList.size() == 0) this.handler.safeToRemove(this);
    }

    public double meanDistanceToRegiment()
    {
        double sum = 0;
        for (ArmyUnit armyUnit: armyUnitList) sum+=armyUnit.getDistanceTo(this);

        return sum/armyUnitList.size();
    }

    public void safeToRemove(ArmyUnit armyUnit) {
        toRemove.add(armyUnit);
    }

    /**
     * Helper function to test functionalities easier. Not working as smoothly as desired.
     * Adds units to the regiment passed as the argument in number specified in number.
     * Possibilities of further modifications include allowing to pass class of units to add as an argument(only adds SimulationObjects.Infantry units for now).
     * @param number Number of units to populate the regiment with
     */
    public void populateRegimentWithUnits(int number) {
        Random random = new Random();
        for (int i = 0; i < number; i++) {
            this.addArmyUnit(new Infantry(this.x + (-25 + random.nextInt(10)*5), this.y + (-10 + random.nextInt(4))*5));
            //System.out.println("SimulationObjects.Regiment " + this + "'s unit position: (" + armyUnitList.getLast().getPosition() + ")");
        }
    }

    /**
     * Adds side*side number of units to the regiment in position that forms a square formation with evenly
     * or densely distributed units depending on the value of evenlyDistributed.
     * @param side Number of units at the side of the square
     * @param evenlyDistributed Flag defining units distribution.
     *                          If true distribution of units in regiment field is even (sparse distribution if side is small).
     *                          If false distance between units is fixed and equal to unit's size.
     */
    public void formationSquare(int side, boolean evenlyDistributed) {
        float baseX = 0;
        float baseY = 0;
        float step = 0;
        if (evenlyDistributed) {
            baseX = this.x - regimentCenterRadius / (float) Math.sqrt(2);
            baseY = this.y - regimentCenterRadius / (float) Math.sqrt(2);
            step = 2 * regimentCenterRadius / (float) (Math.sqrt(2) * (side - 1));
        } else {
            baseX = this.x - side*Infantry.infantryBlockSize;
            baseY = this.y - side*Infantry.infantryBlockSize;
            step = 2*Infantry.infantryBlockSize;
        }
        for (int i = 0; i < side; i++) {
            for (int j = 0; j < side; j++) {
                this.addArmyUnit(new Infantry(baseX + i*step, baseY + j*step));
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
        g.fillRect((int) (x - regimentBlockSize/2),(int) (y -regimentBlockSize/2),(int)regimentBlockSize,(int)regimentBlockSize);

        drawCircle(g,Color.GREEN,regimentCenterRadius);
        drawCircle(g,Color.YELLOW,regimentRegroupRadius);
        drawCircle(g,Color.RED,regimentBorderRadius);

        for (ArmyUnit armyUnit: armyUnitList) armyUnit.render(g);
    }


}
