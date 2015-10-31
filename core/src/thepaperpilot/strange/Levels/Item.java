package thepaperpilot.strange.Levels;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import thepaperpilot.strange.Main;

public class Item extends Button {
    String name;

    public Item(ItemPrototype prototype, final Level level) {
        super(Main.skin, "toggle");

        this.name = prototype.name;

        add(new ImageButton(new TextureRegionDrawable(Main.entities.findRegion(prototype.texture + "Inv")))).padBottom(1).row();
        Label invLabel = new Label(name, Main.skin);
        invLabel.setAlignment(Align.center);
        add(invLabel);

        if (level.selected.contains(this))
            toggle();

        addListener(new ClickListener(Input.Buttons.LEFT) {
            public void clicked(InputEvent event, float x, float y) {
                if (level.selected.contains(Item.this)) {
                    level.selected.remove(Item.this);
                } else level.selected.add(Item.this);

                level.combine();
                level.updateInventory();
            }
        });
    }

    public static class ItemPrototype {
        String name;
        String texture;
    }

    public static class CombinationPrototype {
        public String[] ingredients;
        public String result;
    }
}
