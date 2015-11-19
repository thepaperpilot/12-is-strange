package thepaperpilot.strange.Levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Json;
import thepaperpilot.strange.Main;

import java.util.*;

public class Level {
    private final static Json json = new Json();
    public Map<Item[], Item> combinations = new HashMap<Item[], Item>();
    public List<Item> inventory = new ArrayList<Item>();
    public List<Item> selected = new ArrayList<Item>();
    public Scene firstScene;
    Map<String, Scene> scenes = new HashMap<String, Scene>();
    Map<String, Item> items = new HashMap<String, Item>();

    public Level(LevelPrototype levelPrototype) {
        // load all the different areas
        for (Scene.ScenePrototype scene : levelPrototype.scenes) {
            scenes.put(scene.name, new Scene(scene, this));
        }

        // load all the different items
        for (Item.ItemPrototype item : levelPrototype.items) {
            items.put(item.name, new Item(item, this));
        }

        // load all the different recipes
        for (Item.CombinationPrototype prototype : levelPrototype.combinations) {
            Item[] key = new Item[prototype.ingredients.length];
            for (int i = 0; i < prototype.ingredients.length; i++) {
                key[i] = this.items.get(prototype.ingredients[i]);
            }
            combinations.put(key, this.items.get(prototype.result));
        }

        // go to the first area
        firstScene = scenes.get(levelPrototype.first);
    }

    public static Level readLevel(String fileName) {
        // read the level from a JSON file
        return new Level(json.fromJson(LevelPrototype.class, Gdx.files.internal(fileName)));
    }

    public void combine() {
        // iterate through all the recipes in this level
        outer:
        for (int i = 0; i < combinations.keySet().size(); i++) {
            Item[] combo = (Item[]) combinations.keySet().toArray()[i];
            // ensure we have the same amount of items selected as the recipe needs
            if (selected.size() != combo.length) continue;
            // check if we have the same items selected as the recipe needs
            for (Item item : selected) {
                boolean found = false;
                for (Item comboItem : combo) {
                    if (item == comboItem)
                        found = true;
                }
                if (!found) continue outer;
            }

            // if the selected items match the recipe, complete the recipe
            Main.manager.get("audio/pickup.wav", Sound.class).play();
            ArrayList<Item> tmp = new ArrayList<Item>();
            Collections.addAll(tmp, combo);
            inventory.removeAll(tmp);
            inventory.add(combinations.get(combo));
            selected.clear();
            break;
        }
    }

    public void updateInventory() {
        for (Scene scene : scenes.values())
            scene.updateInventory();
    }

    public static class LevelPrototype {
        Scene.ScenePrototype[] scenes;
        Item.ItemPrototype[] items;
        Item.CombinationPrototype[] combinations;
        String first;

        public LevelPrototype(){

        }

        public LevelPrototype(String first) {
            this.first = first;
        }
    }
}
