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


    /*
    Action to są już konkretne akcje jednostki
    Order to polecenia od Regimentu
     */
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

    // Na razie w sumie prawie takie samo jak regroupAction, ale to się może zmienić potem (chyba) xD
    private void moveAction()
    {
        float diagonalDistance = (float) this.getDistanceTo(myEnemy);
        float distanceX = this.x - myEnemy.x;
        float distanceY = this.y - myEnemy.y;

        // Generalnie to chodzi o to żeby w dobrym kierunku nie szybciej niż maxVelocity
        // To -1 jest od tego że pkt (0,0) to jest lewy góry róg. I jakby trzeba to uwzględnić. Pitagoras, skalowanie i trygonometria xD
        this.velX = (-1)*distanceX*this.maxVelocity/diagonalDistance;
        this.velY = (-1)*distanceY*this.maxVelocity/diagonalDistance;

        x+=velX;
        y+=velY;
    }

    // Na razie w sumie prawie takie samo jak moveAction, ale to się może zmienić potem (chyba) xD
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
    // Zachowanie jak otrzyma rozkaz ataku
    void attackOrder(Regiment regimentToAttack) {
        ArmyUnit enemy = this.findNearestEnemyIn(regimentToAttack);

        if (enemy == null) this.unitAction = null; // Brak wrogów ...? -> postoi w miejscu
        else if(this.getDistanceTo(enemy) < this.attackRange) // Jak wróg blisko to atakuj
        {
            this.myEnemy = enemy;
            this.unitAction = UnitAction.ATTACK;
        }
        else{ // jak nie, to idz w stronę najbiższego wroga -> Jak bedzie blisko to wejdzie powyższy if
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

        if (enemy == null) this.unitAction = null; // brak wrogow -> postoi w miejscu
        else if(this.getDistanceTo(enemy) < this.attackRange) // jak blisko to atak
        {
            this.myEnemy = enemy;
            this.unitAction = UnitAction.ATTACK;
        }
        else if(this.getDistanceTo(this.myRegiment) >= Regiment.regimentCenterRadius ) // poza centrum -> idz w stronę centrum
        {
            this.unitAction = UnitAction.REGROUP;
        }
        else { // jak nie ma wroga, a jesteś w centrum, to idz do wroga
            this.myEnemy = enemy;
            this.unitAction = UnitAction.MOVE_TO_ENEMY;
        }
    }

    @Override
    void regroupOrder(){
        ArmyUnit enemy = this.findNearestEnemyIn(this.myRegiment.enemyRegiment);

        if (enemy == null) this.unitAction = null; // brak wrogów -> stoi w miejscu
        else if(this.getDistanceTo(enemy) < this.attackRange) // jest obok wróg -> walcz
        {
            this.myEnemy = enemy;
            this.unitAction = UnitAction.ATTACK;
        }
        else if(this.getDistanceTo(this.myRegiment) <= Regiment.regimentRegroupRadius ) // Jesteś w strefie przegrupowania -> szukaj/idz do wroga
        {
            this.myEnemy = enemy;
            this.unitAction = UnitAction.MOVE_TO_ENEMY;
        }
        else { // jesteś poza strefą -> przegrupuj się
            this.unitAction = UnitAction.REGROUP;
        }
    }

    @Override
    public void tick()
    {
        if (this.hp <= 0) // śmierć
            myRegiment.removeArmyUnit(this);
        else { // wykonaj akcję.
            if (this.unitAction == UnitAction.ATTACK) this.attackAction();
            else if (this.unitAction == UnitAction.MOVE_TO_ENEMY) this.moveAction();
            else if (this.unitAction == UnitAction.REGROUP) this.regroupAction();
        }
    }

    @Override
    public void render(Graphics g) {
        if(this.alliance.equals(Alliance.Blue)) g.setColor(Color.BLUE);
        else g.setColor(Color.RED);
        g.fillRect((int)x,(int)y,(int)infantryBlockSize,(int)infantryBlockSize);
    }
}
