package thepaperpilot.strange.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import thepaperpilot.strange.Main;

public class FinalScreen implements Screen {
    private Stage stage;
    private Stage ui;
    private float time = 0;
    private Label dialog;
    private String[] dialogs;

    public FinalScreen(Image background, String[] dialogs) {
        // TODO flesh this out more
        stage = new Stage(new StretchViewport(256, 144));
        Gdx.input.setInputProcessor(stage);
        ui = new Stage(new StretchViewport(640, 360));
        this.dialogs = dialogs;

        stage.addActor(background);

        Table dialogTable = new Table(Main.skin);
        dialogTable.setFillParent(true);
        dialog = new Label(dialogs[0], Main.skin);
        dialog.setWrap(true);
        dialogTable.top().add(dialog);

        ui.addActor(dialogTable);
    }

    @Override
    public void show() {
        stage.act();
        stage.draw();
        ui.act();
        ui.draw();
    }

    @Override
    public void render(float delta) {
        time += delta;

        dialog.setText(dialogs[Math.min(dialogs.length, (int) (time / 10))]);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        ui.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
