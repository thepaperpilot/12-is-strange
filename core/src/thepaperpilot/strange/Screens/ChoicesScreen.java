package thepaperpilot.strange.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import thepaperpilot.strange.Entities.RightClickIndicator;
import thepaperpilot.strange.Main;

import java.util.Arrays;

public class ChoicesScreen implements Screen {
    public static ParticleEffect choicesParticle;
    public SpriteBatch batch;
    int decision;
    Screen[] nextScreen;
    private Stage stage;
    private Table decisionTable;

    public ChoicesScreen(int decision, String question, String[] choices, Screen nextScreen, Screen previousScreen) {
        Screen[] screens = new Screen[choices.length];
        Arrays.fill(screens, nextScreen);
        init(decision, question, choices, screens, previousScreen);
    }

    public ChoicesScreen(int decision, String question, String[] choices, Screen[] nextScreens, Screen previousScreen) {
        init(decision, question, choices, nextScreens, previousScreen);
    }

    private void init(int decision, String question, String[] choices, Screen[] nextScreens, final Screen previousScreen) {
        this.decision = decision;
        this.nextScreen = nextScreens;
        stage = new Stage(new StretchViewport(640, 360));
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.input.setInputProcessor(stage);

        decisionTable = new Table(Main.skin);
        decisionTable.setFillParent(true);

        Table table = new Table(Main.skin);
        table.setFillParent(true);
        table.top().left().add(new RightClickIndicator());
        table.add(new Label(" rewind time by right clicking", Main.skin));
        table.setColor(1, 1, 1, .5f);
        table.addListener(new ClickListener(Input.Buttons.RIGHT) {
            public void clicked(InputEvent event, float x, float y) {
                Main.manager.get("rewind.wav", Sound.class).play();
                Main.changeScreen(previousScreen);
            }
        });

        stage.addActor(table);

        setChoice(question, choices);

        stage.addListener(new ClickListener(Input.Buttons.RIGHT) {
            public void clicked(InputEvent event, float x, float y) {
                Main.manager.get("rewind.wav", Sound.class).play();
                Main.changeScreen(previousScreen);
            }
        });

        batch = new SpriteBatch();
        choicesParticle = new ParticleEffect();
        choicesParticle.load(Gdx.files.internal("swirls.p"), Gdx.files.internal(""));
        choicesParticle.setPosition(stage.getWidth() / 2, stage.getHeight() / 2);
    }

    @Override
    public void show() {
    }

    private void setChoice(String question, final String[] choices) {
        decisionTable.clearChildren();
        Label choiceLabel = new Label(question, Main.skin, "large");
        choiceLabel.setWrap(true);
        choiceLabel.setAlignment(Align.center);
        TextButton[] optionButtons = new TextButton[choices.length];
        double choiceWidth = Math.min(stage.getWidth() - 10, new GlyphLayout(Main.skin.getFont("large"), question).width);
        double optionWidth = new GlyphLayout(Main.skin.getFont("font"), choices[0]).width;
        for (int i = 0; i < optionButtons.length; i++) {
            optionButtons[i] = new TextButton(choices[i], Main.skin);
            optionButtons[i].getLabel().setWrap(true);
            optionWidth = Math.max(optionWidth, new GlyphLayout(Main.skin.getFont("font"), choices[i]).width);
        }
        optionWidth = Math.min(choiceWidth, stage.getWidth() / (double) (choices.length + 1)) + 2;
        decisionTable.add(choiceLabel).width((int) choiceWidth).colspan(choices.length).padBottom(2).row();
        for (int i = 0; i < optionButtons.length; i++) {
            decisionTable.add(optionButtons[i]).width((int) optionWidth).pad(5);
            final int decision = i;
            optionButtons[i].addListener(new ClickListener(Input.Buttons.LEFT) {
                public void clicked(InputEvent event, float x, float y) {
                    Main.manager.get("select.wav", Sound.class).play();
                    if (ChoicesScreen.this.decision != -1)
                        Main.decisions[ChoicesScreen.this.decision - 1] = decision;
                    Main.changeScreen(nextScreen[decision]);
                }
            });
        }
        stage.addActor(decisionTable);
    }

    @Override
    public void render(float delta) {
        final Matrix4 trans = new Matrix4();
        trans.scale(Gdx.graphics.getWidth() / stage.getWidth(), Gdx.graphics.getHeight() / stage.getHeight(), 1);
        batch.setTransformMatrix(trans);
        batch.begin();
        choicesParticle.draw(batch, delta / 10f);
        batch.end();
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
        batch.dispose();
    }
}
