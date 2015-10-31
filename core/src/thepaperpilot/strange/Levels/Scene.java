package thepaperpilot.strange.Levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import thepaperpilot.strange.Entities.Max;
import thepaperpilot.strange.Entities.RightClickIndicator;
import thepaperpilot.strange.Main;

import java.util.HashMap;
import java.util.Map;

public class Scene implements Screen {
    Table inventoryTable;
    String name;
    Image background;
    Map<String, Entity> entities = new HashMap<String, Entity>();
    Entity target;
    Map<String, Rectangle> obstacles = new HashMap<String, Rectangle>();
    Stage stage;
    Stage ui;
    String previous;
    boolean transition;
    Level level;
    Max max;

    public Scene(final ScenePrototype prototype, final Level level) {
        name = prototype.name;
        this.level = level;
        obstacles = prototype.obstacles;
        background = new Image(Main.backgrounds.findRegion(prototype.background));

        stage = new Stage(new StretchViewport(background.getWidth(), background.getHeight()));
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        ui = new Stage(new StretchViewport(640, 360));

        stage.addActor(background);

        for (Entity.EntityPrototype entityPrototype : prototype.entities) {
            try {
                final Entity entity = new Entity(entityPrototype, this);

                entity.addListener(new ClickListener(Input.Buttons.LEFT) {
                    public void clicked(InputEvent event, float x, float y) {
                        target = entity;
                        x = entity.getX();
                        for (Rectangle obstacle : obstacles.values()) {
                            if (entity.getX() >= obstacle.x - max.getWidth() / 2f && max.getX() <= obstacle.x) {
                                x = obstacle.x - max.getWidth() / 2f - 2;
                                if (obstacle.x < entity.getX())
                                    target = null;
                            }
                            else if (entity.getX() <= obstacle.x + obstacle.width + max.getWidth() / 2f && max.getX() >= obstacle.x) {
                                x = obstacle.x + obstacle.width + max.getWidth() / 2f + 2;
                                if (obstacle.x + obstacle.width > entity.getX() + entity.getWidth())
                                    target = null;
                            }
                        }
                        max.target = (int) x;
                        event.reset();
                    }
                });

                entities.put(entityPrototype.name, entity);
            } catch (NullPointerException e) {
                Gdx.app.log("Couldn't create entity", "entity \"" + entityPrototype.name + "\" could not be created in scene \"" + name + "\"", e);
            }
        }

        max = new Max(64, 10);
        stage.addActor(max);

        stage.addListener(new ClickListener(Input.Buttons.LEFT) {
            public void clicked(InputEvent event, float x, float y) {
                for (Rectangle obstacle : obstacles.values()) {
                    if (x >= obstacle.x && max.getX() <= obstacle.x)
                        x = obstacle.x - 2;
                    if (x <= obstacle.x + obstacle.width && max.getX() >= obstacle.x)
                        x = obstacle.x + obstacle.width + 2;
                }
                max.target = (int) (x - max.getWidth() / 2);
                target = null;
            }
        });

        stage.addListener(new ClickListener(Input.Buttons.RIGHT) {
            public void clicked(InputEvent event, float x, float y) {
                if (prototype.previous != null) {
                    transition = true;
                    Main.manager.get("audio/rewind.wav", Sound.class).play(.4f); // 40% volume, because its really loud
                    stage.addAction(Actions.sequence(Actions.fadeOut(.5f), Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            Main.reverse = true;
                            transition = false;
                            Main.changeScreen(level.scenes.get(previous));
                        }
                    }), Actions.fadeIn(0)));
                }
            }
        });

        if (prototype.previous != null) {
            previous = prototype.previous;
            Table table = new Table(Main.skin);
            table.setFillParent(true);
            table.top().left().add(new RightClickIndicator());
            table.setColor(1, 1, 1, .5f);
            table.addListener(new ClickListener(Input.Buttons.LEFT) {
                public void clicked(InputEvent event, float x, float y) {
                    Main.reverse = true;
                    transition = false;
                    Main.changeScreen(level.scenes.get(previous));
                }
            });
            ui.addActor(table);
        }

        inventoryTable = new Table(Main.skin);
        inventoryTable.setSize(ui.getWidth() - 30, 38);
        inventoryTable.setPosition(30, ui.getHeight() - 38);
        inventoryTable.setBackground(Main.skin.getDrawable("default-round"));
        inventoryTable.pad(2);

        updateInventory();

        ui.addActor(inventoryTable);
    }

    public void updateInventory() {
        inventoryTable.clearChildren();
        for (int i = 0; i < level.inventory.size(); i++) {
            inventoryTable.left().add(new Item(level.inventory.get(i))).pad(2).height(32);
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputMultiplexer(ui, stage));
        max.target = (int) max.getX();
        if (Main.reverse) {
            Main.reverse = false;
            transition = true;
            stage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(.5f), Actions.run(new Runnable() {
                @Override
                public void run() {
                    transition = false;
                }
            })));
        }
    }

    @Override
    public void render(float delta) {
        if (transition)
            Main.renderParticles(delta);

        if (target != null && max.getX() == max.target) {
            target.onTouch();
            target = null;
        }

        stage.act(delta);
        stage.draw();
        ui.act(delta);
        ui.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
        ui.getViewport().update(width, height);
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

    public void say(String message) {
        Main.manager.get("audio/error.wav", Sound.class).play();
        final Label label = new Label(message, Main.skin);
        label.setPosition((int) ((max.getX() + max.getWidth() / 2f) * ui.getWidth() / stage.getWidth() - label.getWidth() / 2f), 5);
        label.addAction(Actions.sequence(Actions.moveBy(0, 0, 1f), Actions.parallel(Actions.moveBy(0, 50, 1), Actions.fadeOut(1)), Actions.run(new Runnable() {
            @Override
            public void run() {
                label.remove();
            }
        })));
        ui.addActor(label);
    }

    public static class ScenePrototype {
        String name;
        String background;
        String previous;
        Entity.EntityPrototype[] entities;
        Map<String, Rectangle> obstacles = new HashMap<String, Rectangle>();

        public ScenePrototype() {

        }

        public ScenePrototype(String name, String background, String previous) {
            this.name = name;
            this.background = background;
            this.previous = previous;
        }
    }
}
