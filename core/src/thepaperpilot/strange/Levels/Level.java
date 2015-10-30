package thepaperpilot.strange.Levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Level {
    private final static Json json = new Json();
    Map<String, Scene> scenes = new HashMap<String, Scene>();
    Map<String, Item> items = new HashMap<String, Item>();
    public List<Item> inventory = new ArrayList<Item>();
    public List<Item> selected = new ArrayList<Item>();
    public Scene firstScene;


    public static Level readLevel(String fileName) {
        return new Level(json.fromJson(LevelPrototype.class, Gdx.files.internal(fileName)));
    }

    public Level(LevelPrototype levelPrototype) {
        for (Scene.ScenePrototype scene : levelPrototype.scenes) {
            scenes.put(scene.name, new Scene(scene, this));
        }

        for (Item.ItemPrototype item : levelPrototype.items) {
            items.put(item.name, new Item(item, this));
        }

        firstScene = scenes.get(levelPrototype.first);
    }

    public static class LevelPrototype {
        Scene.ScenePrototype[] scenes;
        Item.ItemPrototype[] items;
        String first;
    }
}
