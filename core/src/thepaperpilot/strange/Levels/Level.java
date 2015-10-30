package thepaperpilot.strange.Levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Json;
import thepaperpilot.strange.Main;

import java.util.*;

public class Level {
    private final static Json json = new Json();
    Map<String, Scene> scenes = new HashMap<String, Scene>();
    Map<String, Item> items = new HashMap<String, Item>();
    public Map<Item[], Item> combinations = new HashMap<Item[], Item>();
    public List<Item> inventory = new ArrayList<Item>();
    public List<Item> selected = new ArrayList<Item>();
    public Scene firstScene;

    public static Level readLevel(String fileName) {
        return new Level(json.fromJson(LevelPrototype.class, Gdx.files.internal(fileName)));
    }

    public void combine() {
        outer:
        for (int i = 0; i < combinations.keySet().size(); i++) {
            Item[] combo = (Item[]) combinations.keySet().toArray()[i];
            if (Main.selected.size() != combo.length) continue;
            for (Item item : selected) {
                boolean found = false;
                for (Item comboItem : combo) {
                    if (item == comboItem)
                        found = true;
                }
                if (!found) continue outer;
            }
            Main.manager.get("audio/pickup.wav", Sound.class).play();
            ArrayList<Item> tmp = new ArrayList<Item>();
            Collections.addAll(tmp, combo);
            inventory.removeAll(tmp);
            inventory.add(combinations.get(combo));
            selected.clear();
            break;
        }
    }

    public Level(LevelPrototype levelPrototype) {
        for (Scene.ScenePrototype scene : levelPrototype.scenes) {
            scenes.put(scene.name, new Scene(scene, this));
        }

        for (Item.ItemPrototype item : levelPrototype.items) {
            items.put(item.name, new Item(item, this));
        }

        for(String[] items : levelPrototype.combinations.keySet()) {
            Item[] key = new Item[items.length];
            for (int i = 0; i < items.length; i++) {
                key[i] = this.items.get(items[i]);
            }
            combinations.put(key, this.items.get(levelPrototype.combinations.get(items)));
        }

        firstScene = scenes.get(levelPrototype.first);
    }

    public void updateInventory() {
        for(Scene scene : scenes.values())
            scene.updateInventory();
    }

    public static class LevelPrototype {
        Scene.ScenePrototype[] scenes;
        Item.ItemPrototype[] items;
        Map<String[], String> combinations;
        String first;
    }
}
