package SimulationObjects;

import Enums.Alliance;

import java.awt.*;

public class Infantry extends ArmyUnit {

    public static final double infantryBlockSize = 3;

    public Infantry(double x, double y) {
        super(x, y);
        this.hp = 100;

        this.minDMG = 0;
        this.maxDMG = 5;
        this.meanDMG = 2;
        this.stdDMG = 2;
        this.attackRange = infantryBlockSize*20;
        this.maxVelocity = 1;
        this.safeArea = this.attackRange;
    }

    @Override
    public void render(Graphics g) {
        if(this.alliance.equals(Alliance.Blue)) g.setColor(Color.BLUE);
        else g.setColor(Color.RED);
        g.fillRect((int)(x - infantryBlockSize /2),
                (int) (y - infantryBlockSize /2),
                (int) infantryBlockSize,
                (int) infantryBlockSize
        );
    }
}
