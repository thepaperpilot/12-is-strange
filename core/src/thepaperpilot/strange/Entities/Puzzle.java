package thepaperpilot.strange.Entities;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import thepaperpilot.strange.Item;
import thepaperpilot.strange.Main;
import thepaperpilot.strange.Scene;
import thepaperpilot.strange.Screens.GameScreen;

public class Puzzle extends Entity {
    final Item[] keys;
    public boolean locked = true;

    public Puzzle(Drawable drawable, GameScreen game, int x, int y, Item[] keys) {
        super(drawable, game, x, y);
        this.keys = keys;
    }

    public Puzzle(Drawable drawable, GameScreen game, int x, int y, Item key) {
        this(drawable, game, x, y, new Item[]{key});
    }

    public Puzzle(GameScreen game, Item[] keys) {
        super(game);
        this.keys = keys;
    }

    public void onTouch() {
        if (locked) {
            for (Item key : keys) {
                if (!Main.selected.contains(key)) {
                    onFail();
                    return;
                }
            }
            locked = false;
            Main.manager.get("pickup.wav", Sound.class).play();
            for (Item key : keys) {
                Main.selected.remove(key);
                Main.inventory.remove(key);
            }
            onSuccess();
            Scene.updateInventory();
        } else open();
    }

    public void onFail() {

    }

    public void onSuccess() {
        open();
    }

    public void open() {

    }
}
