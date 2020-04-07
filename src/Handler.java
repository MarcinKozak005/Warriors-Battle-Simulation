import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class Handler {

    List<SimulationObject> simulationObjectList = new LinkedList<>();
    List<Regiment> toRemove = new LinkedList<>();


    public void tick(){
        for(SimulationObject simulationObject: simulationObjectList)
        {
            simulationObject.tick();
        }
        simulationObjectList.removeAll(toRemove);
    }

    public void render(Graphics g){
        for(SimulationObject simulationObject: simulationObjectList){
            simulationObject.render(g);
        }
    }

    public void addSimulationObject(SimulationObject object){
        this.simulationObjectList.add(object);
    }

    public Regiment getNearestEnemyFor(Regiment regiment) throws CantFindEnemyRegiment {
        double actualMinimum = Double.MAX_VALUE;
        Regiment enemyRegiment = null;

        for(SimulationObject object: simulationObjectList)
        {
            if(object.alliance != regiment.alliance && regiment.getDistanceTo(object)<actualMinimum)
                enemyRegiment = (Regiment) object;
        }

        if( enemyRegiment == null) throw new CantFindEnemyRegiment();
        return enemyRegiment;
    }

    public void safeToRemove(Regiment regiment){
        toRemove.add(regiment);
    }
}
