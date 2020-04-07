import java.awt.*;

public class Infantry extends ArmyUnit {

    public static final float infantryBlockSize = 10;

    public Infantry(float x, float y) {
        super(x, y);
        this.hp = 100;
        this.minDMG = 2;
        this.maxDMG = 10;
        // Taki zasięg rąk- mam nadzije że dzięki temu nie będą się tak blokowały.
        this.attackRange = infantryBlockSize+infantryBlockSize/2;
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
            //System.out.println(this + " dealt: "+ DMGDealt+" to: "+ myEnemy);
        }
    }

    private void moveAction()
    {
        setDirectionTo(myEnemy);

        float newX = x + velX;
        float newY = y + velY;

        long matches = myRegiment.armyUnitList.stream().filter(n -> (Math.sqrt(Math.pow(newX - n.x, 2) + Math.pow(newY - n.y,2)) < Infantry.infantryBlockSize)).count();

        if (matches <= 1) {
            // not quite sure why are we comparing matches to 1, if we compare it to 0 as one could expect units won't move, and the more overlaps we allow the messier it gets
            x = newX;
            y = newY;
        }
        /* else {
            // tried to change unit we're focused on, sometimes it works, sometimes units will get stuck anyway
            this.attackOrder(myEnemy.myRegiment);
        } */

    }

    private void regroupAction()
    {
        // Praktycznie identyczne jak Infantry.moveAction()
        setDirectionTo(myRegiment);

        float newX = x + velX;
        float newY = y + velY;

        long matches = myRegiment.armyUnitList.stream().filter(n -> (Math.sqrt(Math.pow(newX - n.x, 2) + Math.pow(newY - n.y,2)) < Infantry.infantryBlockSize)).count();

        if (matches <= 1) {
            x = newX;
            y = newY;
        }
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
    void moveToAttackOrder(Regiment regimentToAttack)
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
        g.fillRect((int)(x - infantryBlockSize/2),(int) (y - infantryBlockSize/2),(int)infantryBlockSize,(int)infantryBlockSize);
    }
}
