package SimulationObjects;

import Enums.Alliance;

import java.awt.*;

public class Cavalry extends ArmyUnit {

    public static final double cavalryBlockSize = 5;

    public Cavalry(double x, double y) {
        super(x, y);
        this.hp = 150;
        this.minDMG = 0;
        this.maxDMG = 15;
        this.meanDMG = 7;
        this.stdDMG = 3;
        this.attackRange = cavalryBlockSize+cavalryBlockSize/2;
        this.maxVelocity = 1.5;
        this.safeArea = this.attackRange;
    }

    @Override
    public void render(Graphics g) {
        if(this.alliance.equals(Alliance.Blue)) g.setColor(Color.BLUE);
        else g.setColor(Color.RED);
        g.fillRect((int)(x - cavalryBlockSize/2),
                (int) (y - cavalryBlockSize/2),
                (int)cavalryBlockSize,
                (int)cavalryBlockSize
        );
    }
}
