import java.awt.*;

// $ Piechota
public class Infantry extends ArmyUnit {

    public static final float infantryBlockSize = 10;

    public Infantry(float x, float y) {
        super(x, y);
        this.hp = 100;
        this.minDMG = 2;
        this.maxDMG = 10;
        this.attackRange = 10;
        this.maxVelocity = 1;
    }

    private void attackAction() {
        this.dealDMGToEnemy();
    }

    private void dealDMGToEnemy() {
        float DMGDealt = (float) (minDMG + Math.random() * (maxDMG - minDMG));
        if (myEnemy != null) {
            myEnemy.hp -= DMGDealt;
            System.out.println(this + " dealt: "+ DMGDealt+" to: "+ myEnemy);
        }
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

    private void regroupAction()
    {
        float diagonalDistance = (float) this.getDistanceTo(myRegiment);
        float distanceX = this.x - myRegiment.x;
        float distanceY = this.y - myRegiment.y;

        this.velX = (-1)*distanceX*this.maxVelocity/diagonalDistance;
        this.velY = (-1)*distanceY*this.maxVelocity/diagonalDistance;

        x+=velX;
        y+=velY;
    }

    @Override
    void attackOrder(Regiment regimentToAttack) {
        ArmyUnit enemy = this.findNearestEnemyIn(regimentToAttack);

        if (enemy == null) this.unitAction = null;
        else if(this.getDistanceTo(enemy) < this.attackRange)
        {
            this.myEnemy = enemy;
            this.unitAction = UnitAction.ATTACK;
        }
        else{
            this.myEnemy = enemy;
            this.unitAction = UnitAction.MOVE_TO_ENEMY;
        }
    }

    @Override
    void moveToAttackOrder(Regiment regimentToAttack) // idz zaatakować [kogo?]
    {
        /* Na razie szukamy tylko w WrogimRegimencieNo1 (to nie jest żadna nazwa zmiennej),
        potem można zrobić też żeby atakował wroga koło siebie, nawet jeśli ten wróg nie jest z WrogiegoRegimentuNo1
        (tylko np No2) */

        ArmyUnit enemy = this.findNearestEnemyIn(regimentToAttack);

        if (enemy == null) this.unitAction = null;
        else if(this.getDistanceTo(enemy) < this.attackRange)
        {
            this.myEnemy = enemy;
            this.unitAction = UnitAction.ATTACK;
        }
        else if(this.getDistanceTo(this.myRegiment) >= Regiment.regimentCenterRadius ) // poza centrum
        {
            this.unitAction = UnitAction.REGROUP; // na razie regroup działa do tego samego miejsca
        }
        else {
            this.myEnemy = enemy;
            this.unitAction = UnitAction.MOVE_TO_ENEMY;
        }
    }

    @Override
    void regroupOrder(){
        ArmyUnit enemy = this.findNearestEnemyIn(this.myRegiment.enemyRegiment);

        if (enemy == null) this.unitAction = null;
        else if(this.getDistanceTo(enemy) < this.attackRange)
        {
            this.myEnemy = enemy;
            this.unitAction = UnitAction.ATTACK;
        }
        else if(this.getDistanceTo(this.myRegiment) <= Regiment.regimentRegroupRadius ) // w centrum
        {
            this.myEnemy = enemy;
            this.unitAction = UnitAction.MOVE_TO_ENEMY;
        }
        else {
            this.unitAction = UnitAction.REGROUP;
        }
    }

    @Override
    public void tick()
    {
        if (this.hp <= 0)
            myRegiment.removeArmyUnit(this);
        else {
            if (this.unitAction == UnitAction.ATTACK) this.attackAction();
            else if (this.unitAction == UnitAction.MOVE_TO_ENEMY) this.moveAction();
            else if (this.unitAction == UnitAction.REGROUP) this.regroupAction();
        }

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
        g.fillRect((int)x,(int)y,(int)infantryBlockSize,(int)infantryBlockSize);
    }
}
