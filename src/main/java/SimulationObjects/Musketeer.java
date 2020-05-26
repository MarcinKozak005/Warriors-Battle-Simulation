package SimulationObjects;

import Enums.Alliance;

import java.awt.*;
import java.util.Random;

public class Musketeer extends ArmyUnit {

    public static final double musketeerBlockSize = 5;

    public Musketeer(double x, double y) {
        super(x, y);
        this.hp = 100;

        this.minDMG = 0;
        this.maxDMG = 10;
        this.meanDMG = 5;
        this.stdDMG = 2;
        this.attackRange = musketeerBlockSize*20;
        this.maxVelocity = 1;
        this.safeArea = this.attackRange;
    }

    @Override
    public void render(Graphics g) {
        if(this.alliance.equals(Alliance.Blue)) g.setColor(Color.BLUE);
        else g.setColor(Color.RED);
        g.fillArc((int)(x - musketeerBlockSize /2),
                (int) (y - musketeerBlockSize /2),
                (int) musketeerBlockSize,
                (int) musketeerBlockSize,
                45,135
        );
    }
}
