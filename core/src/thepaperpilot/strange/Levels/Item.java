package thepaperpilot.strange.Levels;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import thepaperpilot.strange.Main;

public class Item extends Button {
    String name;
    String texture;
    Level level;

    public Item(ItemPrototype prototype, final Level level) {
        super(Main.skin, "toggle");

        texture = prototype.texture;
        name = prototype.name;
        this.level = level;
    }

    public Item(final Item item) {
        super(Main.skin, "toggle");

        add(new ImageButton(new TextureRegionDrawable(Main.entities.findRegion(item.texture + "Inv")))).padBottom(1).row();
        Label invLabel = new Label(item.name, Main.skin);
        add(invLabel);

        if (item.level.selected.contains(item.level.items.get(item.name)))
            toggle();

        addListener(new ClickListener(Input.Buttons.LEFT) {
            public void clicked(InputEvent event, float x, float y) {
                Item thisItem = item.level.items.get(item.name);
                if (item.level.selected.contains(thisItem)) {
                    item.level.selected.remove(thisItem);
                } else item.level.selected.add(thisItem);

                item.level.combine();
                item.level.updateInventory();
            }
        });
    }

    public static class ItemPrototype {
        String name;
        String texture;

        public ItemPrototype() {

        }

        public ItemPrototype(String name, String texture) {
            this.name = name;
            this.texture = texture;
        }
    }

    public static class CombinationPrototype {
        public String[] ingredients;
        public String result;

        public CombinationPrototype() {

        }

        public CombinationPrototype(String[] ingredients, String result) {
            this.ingredients = ingredients;
            this.result = result;
        }
    }
}
