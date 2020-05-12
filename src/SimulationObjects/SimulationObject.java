package SimulationObjects;

import Enums.Alliance;
import Simulation.Simulation;

import java.awt.*;

public abstract class SimulationObject {

    public float x; //cooridinates of the center of the Object
    public float y;
    public float velX;
    public float velY;
    public float maxVelocity;
    public Alliance alliance;

    public SimulationObject(){}
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

    public Point getPosition()
    {
        return new Point((int)x,(int)y);
    }

    public double getDistanceTo(SimulationObject simulationObject)
    {
        return this.getPosition().distance(simulationObject.getPosition());
    }

    public void setDirectionTo(SimulationObject simulationObject)
    {
        float diagonalDistance = (float) this.getDistanceTo(simulationObject);
        float distanceX = this.x - simulationObject.x;
        float distanceY = this.y - simulationObject.y;

        this.velX = (-1) * distanceX * this.maxVelocity / diagonalDistance;
        this.velY = (-1) * distanceY * this.maxVelocity / diagonalDistance;
    }

    public boolean notInTheBattlefield()
    {
        if(0 <= this.x && this.x <= Simulation.SCREEN_WIDTH &&
            0 <= this.y && this.y <= Simulation.SCREEN_HEIGHT-30)
            return false;
        return true;
    }

    public void setDirectionToNearestEdge()
    {
        float directionX;
        float directionY;
        if(Math.abs(0-this.x)<Math.abs(Simulation.SCREEN_WIDTH-this.x))
            directionX = -1;
        else
            directionX = 1;

        if(Math.abs(0-this.y)<Math.abs(Simulation.SCREEN_HEIGHT-this.y))
            directionY = -1;
        else
            directionY = 1;

        if(Math.abs(directionX)<Math.abs(directionY))
            {this.velY = 0; this.velX = directionX*maxVelocity;}
        else
            {this.velX = 0; this.velY = directionY*maxVelocity;}
    }
}
