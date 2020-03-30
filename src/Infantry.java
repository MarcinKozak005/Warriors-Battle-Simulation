import java.awt.*;

public class Infantry extends ArmyUnit {

    public static final float infantryBlockSize = 10;

    public Infantry(float x, float y) {
        super(x, y);
        this.hp = 100;
        this.minDMG = 2;
        this.maxDMG = 10;
        this.attackRange = infantryBlockSize;
        this.maxVelocity = 1;
    }

    private void attackAction() {
        this.dealDMGToEnemy();
    }

    private void dealDMGToEnemy() {
        // new Random jest chyba lepszy niż Math.random, ale na razie jest tak xD
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

        float newX = x + velX;
        float newY = y + velY;

        // check if the new position will overlap another unit's position
        long matches = myRegiment.armyUnitList.stream().filter(n -> (Math.sqrt(Math.pow(newX - n.x, 2) + Math.pow(newY - n.y,2)) < 10)).count();

        // check if the new position will overlap any of the enemy unit's position
        // not working efficiently because army units sometimes eventually get stuck not being able to reach their desired enemies
        boolean enemyMatches = myEnemy.myRegiment.armyUnitList.stream().anyMatch(n -> (Math.sqrt(Math.pow(newX - n.x, 2) + Math.pow(newY - n.y,2)) < 5));

        if (matches <= 1) { //(matches <= 1 && !enemyMatches)
            // if the new position won't overlap another unit's position it's safe to assume it
            // not quite sure why are we comparing matches to 1, if we compare it to 0 as one could expect units won't move, and the more overlaps we allow the messier it gets
            x = newX;
            y = newY;
        }
        /* else {
            // tried to change unit we're focused on, sometimes it works, sometimes units will get stuck anyway
            this.attackOrder(myEnemy.myRegiment);
        } */
        // TODO for now the unit won't move as not to walk into any other unit
        // possible solutions:
        // * change the unit's myEnemy
        // * get the unit to move along a curve to the targeted enemy instead of the straight line
        // * get the unit to get as close as possible instead of not moving at all
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
        else if(this.getDistanceTo(this.myRegiment) >= Regiment.regimentCenterRadius )
        {
            this.unitAction = UnitAction.REGROUP;
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
        else if(this.getDistanceTo(this.myRegiment) <= Regiment.regimentRegroupRadius )
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
            myRegiment.safeToRemove(this);
        else {
            if (this.unitAction == UnitAction.ATTACK) this.attackAction();
            else if (this.unitAction == UnitAction.MOVE_TO_ENEMY) this.moveAction();
            else if (this.unitAction == UnitAction.REGROUP) this.regroupAction();
        }
    }

    @Override
    public void render(Graphics g) {
        if(this.alliance.equals(Alliance.Blue)) g.setColor(Color.BLUE);
        else g.setColor(Color.RED);
        //g.fillRect((int)((x - infantryBlockSize)/2),(int) ((y - infantryBlockSize)/2),(int)infantryBlockSize,(int)infantryBlockSize);
        g.fillRect((int)(x - infantryBlockSize/2),(int) (y - infantryBlockSize/2),(int)infantryBlockSize,(int)infantryBlockSize);
    }
}
