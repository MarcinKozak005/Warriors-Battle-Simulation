import java.awt.*;
import java.util.LinkedList;
import java.util.List;

//update and render all objects
public class Handler {

    //może druga lista na wszystkie jednostki -> od shuffle ...?
    List<SimulationObject> simulationObjectList = new LinkedList<>();

    // rusz każdym obiektem/ wykonaj akcję na każdym obiekcie
    public void tick(){
        for(SimulationObject simulationObject: simulationObjectList)
        {
            simulationObject.tick();
        }
    }

    // pokaż każdy obiekt
    public void render(Graphics g){
        for(SimulationObject simulationObject: simulationObjectList){
            simulationObject.render(g);
        }
    }

    public void addSimulationObject(SimulationObject object){
        this.simulationObjectList.add(object);
    }

    public void removeSimulationObject(SimulationObject object){
        this.simulationObjectList.remove(object);
    }

    // Zwraca Pierwszy lepszy wrogi pulk
    public Regiment getEnemyRegimentFor(Alliance alliance) throws CantFindEnemyRegiment {
        for(SimulationObject simulationObject: simulationObjectList)
        {
            if(simulationObject.alliance != alliance) return (Regiment) simulationObject;
        }
        throw new CantFindEnemyRegiment();
    }
}

class CantFindEnemyRegiment extends Exception
{}
