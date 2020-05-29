package SimulationObjects;

import Enums.Alliance;

import java.awt.*;

public class Peasant extends ArmyUnit {

    public static final double infantryBlockSize = 5;

    public Peasant(double x, double y) {
        super(x, y);
        this.hp = 100;
        this.minDMG = 0;
        this.maxDMG = 10;
        this.meanDMG = 5;
        this.stdDMG = 2;
        this.attackRange = infantryBlockSize+infantryBlockSize/2;
        this.maxVelocity = 1;
        this.safeArea = this.attackRange;
    }

    @Override
    public void render(Graphics g) {
        if(this.alliance.equals(Alliance.Blue)) g.setColor(Color.BLUE);
        else g.setColor(Color.RED);
        g.fillOval((int)(x - infantryBlockSize/2),
                (int) (y - infantryBlockSize/2),
                (int)infantryBlockSize,
                (int)infantryBlockSize
        );
    }
}
