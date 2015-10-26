package thepaperpilot.strange;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import thepaperpilot.strange.Entities.Entity;
import thepaperpilot.strange.Screens.GameScreen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum Item {
    ALCOHOL("alcohol", "alcohol", Scene.SEVENTH, 15, 32),
    BOTTLES("bottles", "bottles", Scene.EIGTH, 225, 13),
    CAMERA("camera", "camera", Scene.OFFICE, 110, 33),
    CAT_FOOD("cat food", "catFood", Scene.FOURTH, 170, 13),
    DOG_BONE("dog bone", "dogBone", Scene.SECOND, 200, 14),
    DUCT_TAPE("duct tape", "ductTape"),
    FILES("files", "files", Scene.OFFICE, 135, 30),
    FIRE_EXTINGUISHER("fire extinguisher", "fireExtinguish", Scene.EIGTH, 10, 16),
    FORTUNE_COOKIE_CODE("fortune cookie code", "fortuneCookieCode"),
    FORTUNE_COOKIE("fortune cookie", "fortuneCookie"),
    GUN("gun", "gun"),
    HAMMER("hammer", "hammer", Scene.SEVENTH, 170, 16),
    KEYS("keys", "keys"),
    MAKE_SHIFT_BOMB("make-shift bomb", "makeShiftBomb"),
    NOTEBOOK("notebook", "notebook"),
    PHONE("phone", "phone", Scene.TENTH, 120, 13),
    PHOTO("photo", "photo"),
    SOAP("soap", "soap", Scene.FIRST, 230, 16),
    SODA("soda", "soda", Scene.THIRD, 80, 16),
    SUGAR("sugar", "sugar"),
    USB("usb", "usb"),
    WEED_KILLER("weed killer", "weedKiller", Scene.FOURTH, 21, 7);

    public static final Map<Item[], Item> combinations = new HashMap<Item[], Item>();
    static {
        combinations.put(new Item[]{DUCT_TAPE, SODA, SUGAR, WEED_KILLER}, MAKE_SHIFT_BOMB);
        combinations.put(new Item[]{FORTUNE_COOKIE}, FORTUNE_COOKIE_CODE);
    }

    public final String name;
    public final Image image;

    Item(String name, String image, Scene scene, int x, int y) {
        this.name = name;
        this.image = new Image(Main.manager.get(image + "Inv.png", Texture.class));
        new ItemImage(new Image(Main.manager.get(image + "World.png", Texture.class)).getDrawable(), scene.screen, x, y, ordinal());
    }

    Item(String name, String image) {
        this.name = name;
        this.image = new Image(Main.manager.get(image + "Inv.png", Texture.class));
    }

    public static void combine() {
        outer: for (int i = 0; i < combinations.keySet().size(); i++) {
            Item[] combo = (Item[]) combinations.keySet().toArray()[i];
            if(Main.selected.size() != combo.length) continue;
            for (Item item : Main.selected) {
                boolean found = false;
                for (Item comboItem : combo) {
                    if (item == comboItem)
                        found = true;
                }
                if(!found) continue outer;
            }
            Main.manager.get("pickup.wav", Sound.class).play();
            ArrayList<Item> tmp = new ArrayList<Item>();
            Collections.addAll(tmp, combo);
            Main.inventory.removeAll(tmp);
            Main.inventory.add(combinations.get(combo));
            Main.selected.clear();
        }
    }

    public class ItemImage extends Entity {
        public int parent;

        public ItemImage(Drawable drawable, final GameScreen game, int x, int y, final int parent) {
            super(drawable, game, x, y);
            this.parent = parent;
        }

        public void onTouch() {
            Main.manager.get("pickup.wav", Sound.class).play();
            Main.inventory.add(Item.values()[parent]);
            Scene.updateInventory();
            remove();
        }
    }
}
