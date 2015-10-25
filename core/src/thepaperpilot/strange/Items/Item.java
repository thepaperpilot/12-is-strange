package thepaperpilot.strange.Items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import thepaperpilot.strange.Main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum Item {
    ALCOHOL("alcohol"),
    BOTTLES("bottles"),
    CAMERA("camera"),
    CAT_FOOD("cat food", "catFood"),
    DOG_BONE("dog bone", "dogBone"),
    DUCT_TAPE("duct tape", "ductTape"),
    FILES("files"),
    FIRE_EXTINGUISHER("fire extinguisher", "fireExtinguish"),
    FORTUNE_COOKIE_CODE("fortune cookie code", "fortuneCookieCode"),
    FORTUNE_COOKIE("fortune cookie", "fortuneCookie"),
    GUN("gun"),
    HAMMER("hammer"),
    KEYS("keys"),
    MAKE_SHIFT_BOMB("make-shift bomb", "makeShiftBomb"),
    NOTEBOOK("notebook"),
    PHONE("phone"),
    PHOTO("photo"),
    SOAP("soap"),
    SODA("soda"),
    SUGAR("sugar"),
    USB("usb"),
    WEED_KILLER("weed killer", "weedKiller");

    public static final Map<Item[], Item> combinations = new HashMap<Item[], Item>();
    static {
        combinations.put(new Item[]{DUCT_TAPE, SODA, SUGAR, WEED_KILLER}, MAKE_SHIFT_BOMB);
    }

    public final String name;
    public final Image image;

    Item(String name, String image) {
        this.name = name;
        this.image = new Image(Main.manager.get("assets/" + image + "Inv.png", Texture.class));
    }

    Item(String name) {
        this(name, name);
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
}
