import java.awt.*;

public abstract class SimulationObject {

    // Schemat klas, tak jak w 3ciej prezentacji (metody i pola się moą nie zgadzać, ale struktura powinna być identico)
    protected float x; // pozycja
    protected float y;
    protected float velX; // prędkość
    protected float velY;
    protected float maxVelocity; // Żeby nie wszedł w 3 nadświetlną xD
    protected Alliance alliance; // Do którego teamu należę

    public SimulationObject(float x, float y) {
        this.x = x;
        this.y = y;
    }
    public SimulationObject(float x, float y, Alliance alliance) {
        this.x = x;
        this.y = y;
        this.alliance = alliance;
    }

    public abstract void tick(); // co się dzieje w takcie symulacji
    public abstract void render(Graphics g); // renderowanie grafik

    protected Point getPosition()
    {
        return new Point((int)x,(int)y);
    }

    // odległość między dwoma SimulationObjects
    protected double getDistanceTo(SimulationObject simulationObject)
    {
        return this.getPosition().distance(simulationObject.getPosition());
    }
}
