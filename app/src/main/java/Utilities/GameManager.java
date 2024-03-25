package Utilities;
import java.awt.Graphics2D;
import java.util.ArrayList;

import GameObjects.GameObject;

public class GameManager {
    public ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();
    public void tick(){
        gameObjects.forEach(gameObject -> gameObject.tick());
    }
    public void render(Graphics2D g){
        gameObjects.forEach(gameObject -> gameObject.render(g));
    }
    public void addGameObject(GameObject go){
        gameObjects.add(go);
    }
    public void removeGameObject(GameObject go){
        gameObjects.remove(go);
    }
    public GameObject[] getGameObjects() {
        return gameObjects.toArray(new GameObject[gameObjects.size()]);
    }
}
