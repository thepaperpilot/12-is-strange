package thepaperpilot.strange.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import thepaperpilot.strange.Entities.Clock;
import thepaperpilot.strange.Entities.Max;
import thepaperpilot.strange.Entities.RightClickIndicator;
import thepaperpilot.strange.Main;

public class EndingScreen implements Screen {
    private final int ending;
    private Stage stage;
    private Table inventoryTable;
    private Max max;

    public EndingScreen(int ending) {
        this.ending = ending;
    }

    @Override
    public void show() {
        stage = new Stage(new StretchViewport(256, 144));
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.input.setInputProcessor(stage);

        Table table = new Table(Main.skin);
        table.setFillParent(true);
        table.top().left().add(new RightClickIndicator());
        table.setColor(1, 1, 1, .5f);
        stage.addActor(table);

        inventoryTable = new Table(Main.skin);
        inventoryTable.setSize(stage.getWidth() - 30, 40);
        inventoryTable.setPosition(30, stage.getHeight() - 40);
        inventoryTable.setBackground(Main.skin.getDrawable("default-round"));
        inventoryTable.pad(2);

        updateInventory();

        stage.addActor(inventoryTable);

        stage.addListener(new ClickListener(Input.Buttons.LEFT) {
            public void clicked(InputEvent event, float x, float y) {
                max.target = (int) (x - max.getWidth() / 2);
            }
        });

        stage.addListener(new ClickListener(Input.Buttons.RIGHT) {
            public void clicked(InputEvent event, float x, float y) {
                Main.changeScreen(new ChoicesScreen());
            }
        });

        max = new Max((int) stage.getWidth() / 4, 10);
        stage.addActor(max);
        stage.addActor(new Clock(ending, (int) stage.getWidth() / 2, (int) stage.getHeight() / 2));
    }

    private void updateInventory() {
        inventoryTable.clearChildren();
        for (int i = 0; i < Main.inventory.size(); i++) {
            Table item = new Table(Main.skin);
            item.add(new ImageButton(Main.inventory.get(i).image.getDrawable())).padBottom(1).row();
            Label invLabel = new Label(Main.inventory.get(i).name, Main.skin);
            invLabel.setWrap(true);
            invLabel.setAlignment(Align.center);
            item.add(invLabel).width(25);
            inventoryTable.left().add(item).padRight(2);
        }
    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
