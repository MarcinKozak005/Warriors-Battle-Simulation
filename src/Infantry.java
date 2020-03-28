import java.awt.*;

// $ Piechota
public class Infantry extends ArmyUnit {


    public Infantry(float x, float y, Handler handler) {
        super(x, y, handler);
        this.maxVelocity = 1;
        this.attackRange = 10;
    }

    private void attackAction() {
        // atakowanie enemy
        System.out.println("I'm attacking him!");
    }

    private void moveAction()
    {
        float diagonalDistance = (float) this.getDistanceTo(myEnemy);
        float distanceX = this.x - myEnemy.x;
        float distanceY = this.y - myEnemy.y;

        this.velX = (-1)*distanceX*this.maxVelocity/diagonalDistance;
        this.velY = (-1)*distanceY*this.maxVelocity/diagonalDistance;

        x+=velX;
        y+=velY;
    }

    private void regroupAction(){}

    @Override
    void attackOrder(Regiment regiment) {
        ArmyUnit enemy = this.findNearestEnemy(regiment);
        if(this.getDistanceTo(enemy) < this.attackRange)
        {
            this.myEnemy = enemy;
            this.unitAction = UnitAction.ATTACK;
        }
        else{
            this.myEnemy = enemy;
            this.unitAction = UnitAction.MOVE;
        }
    }

    @Override
    void moveOrder(Point point) {}

    @Override
    void regroupOrder(Point point) {}

    @Override
    public void tick()
    {
        if (this.unitAction == UnitAction.ATTACK ) this.attackAction();
        else if (this.unitAction == UnitAction.MOVE) this.moveAction();
        // Not overlaping
//        float distanceToEnemy = Math.abs(this.x- myEnemy.x);
//        if(distanceToEnemy<10)
//        {
//            if (this.x<myEnemy.x) {
//                this.x -= (10-distanceToEnemy) / 2;
//                myEnemy.x += (10-distanceToEnemy) / 2;
//            }
//            else{
//                this.x += (10-distanceToEnemy) / 2;
//                myEnemy.x -= (10-distanceToEnemy) / 2;
//            }
//        }
        // Not overlaping
    }

    @Override
    public void render(Graphics g) {
        if(this.alliance.equals(Alliance.Blue)) g.setColor(Color.BLUE);
        else g.setColor(Color.RED);
        // TODO Block Size
        g.fillRect((int)x,(int)y,10,10);
    }
}
