package SimulationObjects;

import Enums.Alliance;

import java.awt.*;

public class Musketeer extends ArmyUnit {

    public static final double musketeerBlockSize = 3;

    public Musketeer(double x, double y) {
        super(x, y);
        this.hp = 100;

        this.minDMG = 0;
        this.maxDMG = 5;
        this.meanDMG = 2;
        this.stdDMG = 2;
        this.attackRange = musketeerBlockSize*20;
        this.maxVelocity = 1;
        this.safeArea = this.attackRange;
    }

    @Override
    public void render(Graphics g) {
        if(this.alliance.equals(Alliance.Blue)) g.setColor(Color.BLUE);
        else g.setColor(Color.RED);
        g.fillRect((int)(x - musketeerBlockSize /2),
                (int) (y - musketeerBlockSize /2),
                (int) musketeerBlockSize,
                (int) musketeerBlockSize
        );
    }
}
