package thepaperpilot.strange.Items;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import thepaperpilot.strange.Main;
import thepaperpilot.strange.Scene;
import thepaperpilot.strange.Screens.GameScreen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum Item {
    ALCOHOL("alcohol", "alcohol", Scene.FIRST, 0, 0),
    BOTTLES("bottles", "bottles", Scene.SECOND, 20, 40),
    CAMERA("camera", "camera", Scene.FIRST, 30, 90),
    CAT_FOOD("cat food", "catFood", Scene.EIGTH, 39, 3),
    DOG_BONE("dog bone", "dogBone", Scene.THIRD, 20, 23),
    DUCT_TAPE("duct tape", "ductTape", Scene.FOURTH, 120, 21),
    FILES("files", "files", Scene.FIFTH, 24, 120),
    FIRE_EXTINGUISHER("fire extinguisher", "fireExtinguish", Scene.SECOND, 233, 2),
    FORTUNE_COOKIE_CODE("fortune cookie code", "fortuneCookieCode"),
    FORTUNE_COOKIE("fortune cookie", "fortuneCookie", Scene.FIFTH, 124, 23),
    GUN("gun", "gun", Scene.EIGTH, 233, 2),
    HAMMER("hammer", "hammer", Scene.EIGTH, 0, 0),
    KEYS("keys", "keys", Scene.NINTH, 12, 23),
    MAKE_SHIFT_BOMB("make-shift bomb", "makeShiftBomb"),
    NOTEBOOK("notebook", "notebook", Scene.ELEVENTH, 112, 122),
    PHONE("phone", "phone", Scene.FIRST, 112, 22),
    PHOTO("photo", "photo"),
    SOAP("soap", "soap", Scene.FIRST, 56, 56),
    SODA("soda", "soda", Scene.SIXTH, 80, 90),
    SUGAR("sugar", "sugar", Scene.SEVENTH, 110, 32),
    USB("usb", "usb", Scene.TWELTH, 23, 53),
    WEED_KILLER("weed killer", "weedKiller", Scene.FIRST, 23, 56);

    public static final Map<Item[], Item> combinations = new HashMap<Item[], Item>();
    static {
        combinations.put(new Item[]{DUCT_TAPE, SODA, SUGAR, WEED_KILLER}, MAKE_SHIFT_BOMB);
    }

    public final String name;
    public final Image image;

    Item(String name, String image, Scene scene, int x, int y) {
        this.name = name;
        this.image = new Image(Main.manager.get("assets/" + image + "Inv.png", Texture.class));
        new ItemImage(new Image(Main.manager.get("assets/" + image + "World.png", Texture.class)).getDrawable(), scene.screen, x, y, ordinal());
    }

    Item(String name, String image) {
        this.name = name;
        this.image = new Image(Main.manager.get("assets/" + image + "Inv.png", Texture.class));
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
            ArrayList<Item> tmp = new ArrayList<Item>();
            Collections.addAll(tmp, combo);
            Main.inventory.removeAll(tmp);
            Main.inventory.add(combinations.get(combo));
            Main.selected.clear();
        }
    }

    public class ItemImage extends Image {
        public int parent;

        public ItemImage(Drawable drawable, final GameScreen game, int x, int y, final int parent) {
            this.parent = parent;
            setDrawable(drawable);
            setPosition(x, y);
            setSize(16, 16);
            game.stage.addActor(this);
            addListener(new ClickListener(Input.Buttons.LEFT) {
                public void clicked(InputEvent event, float x, float y) {
                    game.max.target = (int) ItemImage.this.getX();
                    game.target = ItemImage.this;
                    event.reset();
                }
            });
        }
    }
}
