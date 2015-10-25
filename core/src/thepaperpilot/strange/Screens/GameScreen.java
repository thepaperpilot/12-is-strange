package thepaperpilot.strange.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
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
import thepaperpilot.strange.Scene;

public class GameScreen implements Screen {
    private Stage stage;
    private Stage ui;
    private Table inventoryTable;
    private Max max;
    public Clock clock;

    public GameScreen(final Scene scene) {
        stage = new Stage(new StretchViewport(256, 144));
        ui = new Stage(new StretchViewport(640, 360));

        if(scene.ordinal() != 0) {
            Table table = new Table(Main.skin);
            table.setFillParent(true);
            table.top().left().add(new RightClickIndicator());
            table.setColor(1, 1, 1, .5f);
            ui.addActor(table);
        }

        inventoryTable = new Table(Main.skin);
        inventoryTable.setSize(ui.getWidth() - 30, 34);
        inventoryTable.setPosition(30, ui.getHeight() - 34);
        inventoryTable.setBackground(Main.skin.getDrawable("default-round"));
        inventoryTable.pad(2);

        updateInventory();

        ui.addActor(inventoryTable);

        stage.addListener(new ClickListener(Input.Buttons.LEFT) {
            public void clicked(InputEvent event, float x, float y) {
                max.target = (int) (x - max.getWidth() / 2);
            }
        });

        stage.addListener(new ClickListener(Input.Buttons.RIGHT) {
            public void clicked(InputEvent event, float x, float y) {
                scene.previous();
            }
        });

        max = new Max((int) stage.getWidth() / 4, 10);
        clock = new Clock(scene.ordinal(), (int) stage.getWidth() / 2, (int) stage.getHeight() / 2);
        stage.addActor(max);
        stage.addActor(clock);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputMultiplexer(ui, stage));
        max.target = (int) max.x;
        // TODO add a particle effect to show next screen
    }

    private void updateInventory() {
        inventoryTable.clearChildren();
        for (int i = 0; i < Main.inventory.size(); i++) {
            Table item = new Table(Main.skin);
            item.add(new ImageButton(Main.inventory.get(i).image.getDrawable())).padBottom(1).row();
            Label invLabel = new Label(Main.inventory.get(i).name, Main.skin);
            invLabel.setAlignment(Align.center);
            item.add(invLabel);
            inventoryTable.left().add(item).pad(2);
        }
    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
        ui.act(delta);
        ui.draw();
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
        ui.dispose();
    }
}
