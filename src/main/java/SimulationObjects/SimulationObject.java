package SimulationObjects;

import Enums.Alliance;
import Simulation.Simulation;

import java.awt.*;

public abstract class SimulationObject {

    public double x; //cooridinates of the center of the Object
    public double y;
    public double velX=0;
    public double velY=0;
    public double maxVelocity;
    private double velocityModifier = 1;
    public Alliance alliance;

    public SimulationObject(){}
    public SimulationObject(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public SimulationObject(double x, double y, Alliance alliance) {
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
        double diagonalDistance = this.getDistanceTo(simulationObject);
        double distanceX = this.x - simulationObject.x;
        double distanceY = this.y - simulationObject.y;

        if(diagonalDistance!=0) {
            this.velX = (-1) * distanceX * this.getVelocity() / diagonalDistance;
            this.velY = (-1) * distanceY * this.getVelocity() / diagonalDistance;
        }
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
        double directionX;
        double directionY;

        if(Math.abs(0-this.x)<Math.abs(Simulation.SCREEN_WIDTH-this.x))
            directionX = -1;
        else
            directionX = 1;

        if(Math.abs(0-this.y)<Math.abs(Simulation.SCREEN_HEIGHT-this.y))
            directionY = -1;
        else
            directionY = 1;

        if(Math.abs(directionX)<Math.abs(directionY))
            {this.velY = 0; this.velX = directionX*getVelocity();}
        else
            {this.velX = 0; this.velY = directionY*getVelocity();}

    }

    public void setVelocityModifier(double modifier)
    {
        this.velocityModifier = (modifier*maxVelocity<=0) ? 0.0 : modifier*maxVelocity;
    }

    public double getVelocityModifier() {
        return velocityModifier;
    }

    public double getVelocity(){return maxVelocity*velocityModifier;}
}
