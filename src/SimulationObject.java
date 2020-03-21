import java.awt.*;

public abstract class SimulationObject {

    protected float x;
    protected float y;
    protected float velX;
    protected float velY;
    protected Alliance alliance;

    public SimulationObject(float x, float y, Alliance alliance) {
        this.x = x;
        this.y = y;
        this.alliance = alliance;
    }

    public abstract void tick();
    public abstract void render(Graphics g);

    // Getters and Setters below
    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Alliance getAlliance() {
        return alliance;
    }

    public void setAlliance(Alliance alliance) {
        this.alliance = alliance;
    }

    public float getVelX() {
        return velX;
    }

    public void setVelX(float velX) {
        this.velX = velX;
    }

    public float getVelY() {
        return velY;
    }

    public void setVelY(float velY) {
        this.velY = velY;
    }
}
