package thepaperpilot.strange.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import thepaperpilot.strange.Main;

public class ChoicesScreen implements Screen {
    private Stage stage;
    private Table decisionTable;

    @Override
    public void show() {
        stage = new Stage(new StretchViewport(256, 144));
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.input.setInputProcessor(stage);

        decisionTable = new Table(Main.skin);
        decisionTable.setFillParent(true);
        //decisionTable.debugAll();

        // Look at if decisions have already been made
        // And change the decisions based on the map
        Main.decision3 = -1;
        String choice = "are you a terrible person?";
        String[] options = new String[]{"sacrifice arcadia bay",
                "sacrifice arcadia bae"};
        setChoice(choice, options);

        stage.addListener(new ClickListener(Input.Buttons.RIGHT) {
            public void clicked(InputEvent event, float x, float y) {
                if (Main.decision2 != -1) {
                    Main.decision2 = -1;
                } else {
                    Main.decision1 = -1;
                }
                setChoice("look at you reversing time", new String[]{"I was taught by the best!"});
            }
        });
    }

    private void setChoice(String choice, String[] options) {
        decisionTable.clearChildren();
        Label choiceLabel = new Label(choice, Main.skin, "large");
        choiceLabel.setWrap(true);
        choiceLabel.setAlignment(Align.center);
        TextButton[] optionButtons = new TextButton[options.length];
        double choiceWidth = Math.min(stage.getWidth() - 10, new GlyphLayout(Main.skin.getFont("large"), choice).width);
        double optionWidth = new GlyphLayout(Main.skin.getFont("font"), options[0]).width;
        for (int i = 0; i < optionButtons.length; i++) {
            optionButtons[i] = new TextButton(options[i], Main.skin, "large");
            optionButtons[i].getLabel().setWrap(true);
            optionWidth = Math.max(choiceWidth, new GlyphLayout(Main.skin.getFont("large"), options[i]).width);
        }
        optionWidth = Math.min(optionWidth, stage.getWidth() / (double) (options.length + 1)) + 2;
        decisionTable.add(choiceLabel).width((int) choiceWidth).colspan(options.length).padBottom(2).row();
        for (int i = 0; i < optionButtons.length; i++) {
            decisionTable.add(optionButtons[i]).width((int) optionWidth).pad(5);
            final int decision = i;
            optionButtons[i].addListener(new ClickListener(Input.Buttons.LEFT) {
                public void clicked(InputEvent event, float x, float y) {
                    // TODO add a graph of the different choices. Go to the next.
                    if (Main.decision1 == -1) {
                        Main.decision1 = decision;
                    } else if (Main.decision2 == -1) {
                        Main.decision2 = decision;
                    } else {
                        Main.decision3 = decision;
                        Main.findEnding();
                    }
                    setChoice("you made a decision. how does that make you feel?", new String[]{"Fucking terrible", "alright I guess", "could be better"});
                }
            });
        }

        stage.addActor(decisionTable);
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
