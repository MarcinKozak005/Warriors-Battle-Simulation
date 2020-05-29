package Simulation;

import Exceptions.CantFindEnemyRegiment;
import Exceptions.CantFindFriendlyRegiment;
import Exceptions.VictoryException;
import SimulationObjects.Regiment;
import SimulationObjects.SimulationObject;
import Statistics.DataCollector;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class Handler {

    public int redDead = 0;
    public int blueDead = 0;
    public int redRefugee = 0;
    public int blueRefugee = 0;
    public List<SimulationObject> simulationObjectList = new LinkedList<>();
    public List<Regiment> toRemove = new LinkedList<>();
    public DataCollector dataCollector = new DataCollector();

    public void tick(){
        try {
            for (SimulationObject simulationObject : simulationObjectList)
            {
                dataCollector.registerState((Regiment) simulationObject);
                simulationObject.tick();
            }
            simulationObjectList.removeAll(toRemove);
        }catch (VictoryException v)
        {
            for(SimulationObject r: this.simulationObjectList) ((Regiment) r).giveOUTData();
            dataCollector.exportDataToCSV();
            printDeadData();
            throw new VictoryException();
        }
    }

    private void printDeadData() {
        System.out.println("Red- dead: "+redDead);
        System.out.println("Red- refugee: "+redRefugee);

        System.out.println("Blue- dead: "+blueDead);
        System.out.println("blue- refugee: "+blueRefugee);

    }

    public void render(Graphics g){
        for(SimulationObject simulationObject: simulationObjectList){
            simulationObject.render(g);
        }
    }

    public void addRegiment(Regiment object){
        this.simulationObjectList.add(object);
        this.dataCollector.add(object);
    }

    public Regiment getNearestEnemyFor(Regiment regiment) throws CantFindEnemyRegiment {
        double actualMinimum = Double.MAX_VALUE;
        Regiment enemyRegiment = null;

        for(SimulationObject object: simulationObjectList)
        {
            if(object.alliance != regiment.alliance && regiment.getDistanceTo(object)<actualMinimum){
                enemyRegiment = (Regiment) object;
                actualMinimum = regiment.getDistanceTo(object);
            }
        }

        if( enemyRegiment == null) throw new CantFindEnemyRegiment();
        return enemyRegiment;
    }

    public Regiment getNearestFriendFor(Regiment regiment) throws CantFindFriendlyRegiment {
        double actualMinimum = Double.MAX_VALUE;
        Regiment friendlyRegiment = null;

        for(SimulationObject object: simulationObjectList)
        {
            if(object.alliance == regiment.alliance && regiment.getDistanceTo(object)<actualMinimum && regiment!=object){
                Regiment tmp = (Regiment) object;
                friendlyRegiment = (Regiment) object;
                actualMinimum = regiment.getDistanceTo(object);
            }
        }

        if( friendlyRegiment == null) throw new CantFindFriendlyRegiment();
        return friendlyRegiment;
    }

    public void safeToRemove(Regiment regiment){
        regiment.giveOUTData();
        toRemove.add(regiment);
    }
}
