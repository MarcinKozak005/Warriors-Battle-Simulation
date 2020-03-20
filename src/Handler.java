import java.awt.*;
import java.util.LinkedList;
import java.util.List;

//update and render all objects
public class Handler {

    List<GameObject> gameObjectList = new LinkedList<>();

    public void tick(){
        for(GameObject gameObject: gameObjectList){
            gameObject.tick();
        }
    }
    public void render(Graphics g){
        for(GameObject gameObject: gameObjectList){
            gameObject.render(g);
        }
    }

    public void addGameObject(GameObject object){
        this.gameObjectList.add(object);
    }

    public void removeGameObject(GameObject object){
        this.gameObjectList.remove(object);
    }

}
