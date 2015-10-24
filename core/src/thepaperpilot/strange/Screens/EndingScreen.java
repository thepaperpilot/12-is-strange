package thepaperpilot.strange.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import thepaperpilot.strange.Main;

public class EndingScreen implements Screen {
    Stage stage;
    private final int ending;
    Table inventoryTable;

    public EndingScreen(int ending) {
        this.ending = ending;
    }

    @Override
    public void show() {
        stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        inventoryTable = new Table(Main.skin);
        inventoryTable.debugAll();
        inventoryTable.setSize(200, Gdx.graphics.getHeight());
        inventoryTable.setPosition(Gdx.graphics.getWidth() - 200, 0);
        inventoryTable.setBackground(Main.skin.getDrawable("default-round"));
        inventoryTable.pad(20);

        updateInventory();

        stage.addActor(inventoryTable);
    }

    private void updateInventory() {
        inventoryTable.clearChildren();
        for (int i = 0; i < Main.inventory.size(); i++) {
            inventoryTable.add(new ImageButton(Main.inventory.get(i).image.getDrawable())).padBottom(10).row();
            Label invLabel = new Label(Main.inventory.get(i).name, Main.skin);
            invLabel.setWrap(true);
            invLabel.setAlignment(Align.center);
            inventoryTable.add(invLabel).width(190).padBottom(40).row();
        }
    }

    @Override
    public void render(float delta) {
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
