package SimulationObjects;

import Enums.UnitAction;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public abstract class ArmyUnit extends SimulationObject
{
    public double hp;
    protected double minDMG;
    protected double maxDMG;
    protected double meanDMG;
    protected double stdDMG;
    protected double attackRange;
    protected Regiment myRegiment;
    protected ArmyUnit myEnemy;
    protected UnitAction unitAction;
    protected double safeArea; // attack enemy in this area regardless of the enemy's regiment

    public ArmyUnit(double x, double y) {
        super(x, y);
    }

    protected abstract void attackAction();
    protected abstract void moveAction();
    protected abstract void regroupAction();
    protected abstract void retreatAction();

    protected final void dealDMGToEnemy() {
        double DMGDealt = Math.min(Math.max(new Random().nextGaussian() * stdDMG + meanDMG, minDMG), maxDMG);
        if (myEnemy != null) myEnemy.takeDamage(DMGDealt);
    }

    protected final void takeDamage(double dmgDealt)
    {this.hp = (this.hp-dmgDealt<0)?0.0:this.hp-dmgDealt;}

    public void attackOrder(Regiment regimentToAttack) {
        ArmyUnit enemyInSafeArea = getEnemyInSafeArea();

        if(enemyInSafeArea!=null)
        {
            this.myEnemy = enemyInSafeArea;
            this.unitAction = UnitAction.ATTACK;
            return;
        }

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

    public void moveToAttackOrder(Regiment regimentToAttack) {
        ArmyUnit enemyInSafeArea = getEnemyInSafeArea();

        if(enemyInSafeArea!=null)
        {
            this.myEnemy = enemyInSafeArea;
            this.unitAction = UnitAction.ATTACK;
            return;
        }

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

    public void regroupOrder(){
        ArmyUnit enemyInSafeArea = getEnemyInSafeArea();

        if(enemyInSafeArea!=null)
        {
            this.myEnemy = enemyInSafeArea;
            this.unitAction = UnitAction.ATTACK;
            return;
        }

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

    public void retreatOrder(Regiment enemyRegiment) {
        this.setVelocityModifier(0.7);
        ArmyUnit enemyInSafeArea = getEnemyInSafeArea();

        if(enemyInSafeArea!=null)
        {
            this.myEnemy = enemyInSafeArea;
            this.unitAction = UnitAction.ATTACK;
            return;
        }

        ArmyUnit enemy = this.findNearestEnemyIn(enemyRegiment);
        if (enemy == null) this.unitAction = null;
        else if(this.getDistanceTo(enemy) < this.attackRange)
        {
            this.myEnemy = enemy;
            this.unitAction = UnitAction.ATTACK;
        }
        else {
            this.myEnemy = enemy;
            this.unitAction = UnitAction.RETREAT;
        }
    }

    public void chaseOrder(Regiment enemyRegiment) {
        ArmyUnit enemyInSafeArea = getEnemyInSafeArea();

        if(enemyInSafeArea!=null)
        {
            this.myEnemy = enemyInSafeArea;
            this.unitAction = UnitAction.ATTACK;
            return;
        }

        Regiment tmpRegiment = new Regiment();
        tmpRegiment.armyUnitList = new LinkedList<>(enemyRegiment.armyUnitList);
        ArmyUnit chasedEnemy;

        this.myEnemy = null;
        while (!tmpRegiment.armyUnitList.isEmpty()){
            chasedEnemy = this.findNearestEnemyIn(tmpRegiment);
            this.checkAndSetChased(chasedEnemy);

            if(this.myEnemy == null)
                tmpRegiment.armyUnitList.remove(chasedEnemy);
            else break;
        }

        if (myEnemy == null) this.unitAction = UnitAction.REGROUP;
        else if(this.getDistanceTo(myEnemy) < this.attackRange)
        {
            this.unitAction = UnitAction.ATTACK;
        }
        else{
            this.unitAction = UnitAction.MOVE_TO_ENEMY;
        }
    }

    protected final boolean willOverlapWithAnother(double newX, double newY, double blockSize) {
        long matches = 0;
        List<SimulationObject> list = myRegiment.handler.simulationObjectList.stream()
                .filter(obj -> obj.alliance == this.alliance)
                .collect(Collectors.toList());

        for(SimulationObject simulationObject: list) {
            matches += ((Regiment) simulationObject).armyUnitList.stream()
                    .filter(n -> this.getDistanceTo(n)<2*blockSize)
                    .filter(n -> (Math.sqrt(Math.pow(newX - n.x, 2) + Math.pow(newY - n.y,2)) < blockSize))
                    .count();
        }
        return (matches > 1);
    }

    protected final void moveWithoutCollisions(double newX, double newY, SimulationObject simulationObject, boolean retreat)
    {
        if (!willOverlapWithAnother(newX, newY, Infantry.infantryBlockSize)) {
            x = newX;
            y = newY;
        }
        else {
            setAlternativeDirectionTo(simulationObject);
            newX = x + velX*(retreat?-1:1);
            newY = y + velY*(retreat?-1:1);
            if (!willOverlapWithAnother(newX, newY, Infantry.infantryBlockSize)) {
                x = newX;
                y = newY;
            }
        }
    }

    protected final void setAlternativeDirectionTo(SimulationObject simulationObject) {
        double diagonalDistance = this.getDistanceTo(simulationObject);
        double distanceX = this.x - simulationObject.x;
        double distanceY = this.y - simulationObject.y;

        double a, b, newDistanceX, newDistanceY;

        if (new Random().nextBoolean()) {
            b = (Math.pow(distanceY, 2) - distanceX*distanceY - Math.pow(diagonalDistance, 2)*Math.sin(Math.PI/2))/distanceX * (-1);
            newDistanceX = distanceY;
            newDistanceY = distanceY - (Double.isNaN(b)?0:b);
        } else {
            a = (Math.pow(distanceX, 2) - distanceX*distanceY - Math.pow(diagonalDistance, 2)*Math.sin(Math.PI/2))/distanceY * (-1);
            newDistanceX = distanceX - (Double.isNaN(a)?0:a);
            newDistanceY = distanceX;
        }

        double newDiagonalDistance = Math.sqrt(Math.pow(newDistanceX,2)+Math.pow(newDistanceY,2));
        this.velX = (-1) * newDistanceX * (this.getVelocity()) / newDiagonalDistance;
        this.velY = (-1) * newDistanceY * (this.getVelocity()) / newDiagonalDistance;
    }

    protected final ArmyUnit findNearestEnemyIn(Regiment enemyRegiment){
        ArmyUnit myEnemy = null;
        Optional<ArmyUnit> myEnemyOptional = enemyRegiment.getFirstArmyUnit();

        if(myEnemyOptional.isPresent())
        {
            myEnemy = myEnemyOptional.get();
            double minimalDistance = this.getDistanceTo(myEnemy);
            for(ArmyUnit armyUnit: enemyRegiment.armyUnitList){
                if(this.getDistanceTo(armyUnit) <= minimalDistance) {
                    minimalDistance = this.getDistanceTo(armyUnit);
                    myEnemy = armyUnit;
                }
            }
        }
        return myEnemy;
    }


    protected final ArmyUnit getEnemyInSafeArea()
    {
        double minDistance = safeArea + 10; // 10 has no meaning here- it hast to be 'big' number
        Point topLeft = new Point( (int)(this.x-safeArea), (int)(this.y-safeArea) );
        Point bottomRight = new Point( (int)(this.x+safeArea), (int)(this.y+safeArea) );
        ArmyUnit enemy = null;

        for(SimulationObject simulationObject: myRegiment.handler.simulationObjectList)
        {
            Regiment regiment = (Regiment) simulationObject;
            if(!regiment.alliance.equals(this.alliance) && regiment!=this.myRegiment.enemyRegiment)
            {
                for(ArmyUnit armyUnit: regiment.armyUnitList)
                {
                    if(armyUnit.inMySafeArea(topLeft,bottomRight) && armyUnit.getDistanceTo(this) <= minDistance)
                    {
                        minDistance = armyUnit.getDistanceTo(this);
                        enemy = armyUnit;
                    }
                }
            }
        }
        return enemy;
    }

    protected final boolean inMySafeArea(Point topLeft, Point bottomRight)
    {
        return this.x > topLeft.x && this.x < bottomRight.x && this.y > topLeft.y && this.y < bottomRight.y;
    }

    protected final void checkAndSetChased(ArmyUnit enemy) {
        ArmyUnit furthestAttacking = null;
        double furthestAttackingDistance = 0;
        int counter = 0;

        for(ArmyUnit armyUnit: myRegiment.armyUnitList)
        {
            if(armyUnit.myEnemy == enemy){
                counter++;
                if(armyUnit.getDistanceTo(enemy)>=furthestAttackingDistance){
                    furthestAttacking = armyUnit;
                    furthestAttackingDistance = armyUnit.getDistanceTo(enemy);
                }
            }
        }
        if(counter<5){
            this.myEnemy = enemy;
        }
        else if(this.getDistanceTo(enemy) < furthestAttackingDistance && furthestAttacking != null){
            furthestAttacking.myEnemy = null;
            furthestAttacking.unitAction = null;
            this.myEnemy = enemy;
        }else {
            this.myEnemy = null;
        }
    }
}
