import java.awt.*;
import java.util.AbstractMap;
import java.util.Map;

public abstract class SimulationObject {

    protected float x; //cooridinates of the center of the Object
    protected float y;
    protected float velX;
    protected float velY;
    protected float maxVelocity;
    protected Alliance alliance;

    public SimulationObject(float x, float y) {
        this.x = x;
        this.y = y;
    }
    public SimulationObject(float x, float y, Alliance alliance) {
        this.x = x;
        this.y = y;
        this.alliance = alliance;
    }

    public abstract void tick();
    public abstract void render(Graphics g);

    protected Point getPosition()
    {
        return new Point((int)x,(int)y);
    }

    protected double getDistanceTo(SimulationObject simulationObject)
    {
        return this.getPosition().distance(simulationObject.getPosition());
    }

    protected void setDirectionTo(SimulationObject simulationObject)
    {
        float diagonalDistance = (float) this.getDistanceTo(simulationObject);
        float distanceX = this.x - simulationObject.x;
        float distanceY = this.y - simulationObject.y;

        this.velX = (-1)*distanceX*this.maxVelocity/diagonalDistance;
        this.velY = (-1)*distanceY*this.maxVelocity/diagonalDistance;
    }
}
