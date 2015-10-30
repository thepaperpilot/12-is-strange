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
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import thepaperpilot.strange.Entities.RightClickIndicator;
import thepaperpilot.strange.Item;
import thepaperpilot.strange.Main;
import thepaperpilot.strange.Screens.ChoicesScreen;

import java.util.HashMap;
import java.util.Map;

public class Scene implements Screen{
    Table inventoryTable;
    String name;
    Image background;
    Map<String, Entity> entities = new HashMap<String, Entity>();
    Entity targetEntity;
    int target;
    Rectangle[] obstacles;
    Stage stage;
    Stage ui;
    String previous;
    boolean transition;
    Level level;

    public Scene(ScenePrototype prototype, final Level level) {
        name = prototype.name;
        this.level = level;
        background = new Image(Main.backgrounds.findRegion(prototype.background));

        stage = new Stage(new StretchViewport(256, 144));
        ui = new Stage(new StretchViewport(640, 360));

        stage.addActor(background);

        for (Entity.EntityPrototype entityPrototype : prototype.entities) {
            final Entity entity = new Entity(entityPrototype, this);

            entity.addListener(new ClickListener(Input.Buttons.LEFT) {
                public void clicked(InputEvent event, float x, float y) {
                    targetEntity = entity;
                    x = (int) entity.getX();
                    for (Rectangle obstacle : obstacles) {
                        if (x >= obstacle.x - entities.get("max").getWidth() / 2f && entities.get("max").getX() <= obstacle.x) {
                            x = obstacle.x - entities.get("max").getWidth() / 2f - 2;
                            if (entity.getX() < x)
                                targetEntity = null;
                        }
                        if (x <= obstacle.x + obstacle.width + entities.get("max").getWidth() / 2f && entities.get("max").getX() >= obstacle.x) {
                            x = obstacle.x + obstacle.width + entities.get("max").getWidth() / 2f + 2;
                            if (entity.getX() > x)
                                targetEntity = null;
                        }
                    }
                    target = (int) x;
                    event.reset();
                }
            });

            entities.put(entityPrototype.name, entity);
        }

        stage.addListener(new ClickListener(Input.Buttons.LEFT) {
            public void clicked(InputEvent event, float x, float y) {
                for (Rectangle obstacle : obstacles) {
                    if (x >= obstacle.x && entities.get("max").getX() <= obstacle.x)
                        x = obstacle.x - 2;
                    if (x <= obstacle.x + obstacle.width && entities.get("max").getX() >= obstacle.x)
                        x = obstacle.x + obstacle.width + 2;
                }
                target = (int) (x - entities.get("max").getWidth() / 2);
                targetEntity = null;
            }
        });

        stage.addListener(new ClickListener(Input.Buttons.RIGHT) {
            public void clicked(InputEvent event, float x, float y) {
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
        });

        if(prototype.previous != null) {
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
        for (int i = 0; i < Main.inventory.size(); i++) {
            Button item = new Button(Main.skin, "toggle");
            item.add(new ImageButton(Main.inventory.get(i).image.getDrawable())).padBottom(1).row();
            Label invLabel = new Label(Main.inventory.get(i).name, Main.skin);
            invLabel.setAlignment(Align.center);
            item.add(invLabel);
            inventoryTable.left().add(item).pad(2).height(32);
            if (Main.selected.contains(Main.inventory.get(i)))
                item.toggle();
            final int index = i;
            item.addListener(new ClickListener(Input.Buttons.LEFT) {
                public void clicked(InputEvent event, float x, float y) {
                    if (Main.selected.contains(Main.inventory.get(index))) {
                        Main.selected.remove(Main.inventory.get(index));
                    } else Main.selected.add(Main.inventory.get(index));
                    Item.combine();
                    thepaperpilot.strange.Scene.updateInventory();
                }
            });
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputMultiplexer(ui, stage));
        target = (int) entities.get("max").getX();
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
            ChoicesScreen.renderParticles(delta);

        if (targetEntity != null && entities.get("max").getX() == target) {
            targetEntity.onTouch();
            targetEntity = null;
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
        label.setPosition((int) ((entities.get("max").getX() + entities.get("max").getWidth() / 2f) * ui.getWidth() / stage.getWidth() - label.getWidth() / 2f), 5);
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
    }
}
