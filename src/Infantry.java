import java.awt.*;

// $ Piechota
public class Infantry extends SimulationObject {

    Infantry myEnemy;
    Handler handler;

    public Infantry(float x, float y, Alliance alliance,Handler handler) {
        super(x, y, alliance);
        this.handler = handler;
    }

    @Override
    public void tick() {
        if(myEnemy==null)
        {
            for(SimulationObject simulationObject: handler.simulationObjectList)
            {
                if(simulationObject.alliance!=this.alliance) myEnemy = (Infantry) simulationObject;
            }
        }

        float distanceX = this.x - myEnemy.x;
        float distanceY = this.y - myEnemy.y;

        this.velX = (-1)*distanceX/50;
        this.velY = (-1)*distanceY/50;

        x+=velX;
        y+=velY;

    }

    @Override
    public void render(Graphics g) {
        if(this.alliance.equals(Alliance.Blue)) g.setColor(Color.BLUE);
        else g.setColor(Color.RED);
        g.fillRect((int)x,(int)y,10,10);
    }
}
