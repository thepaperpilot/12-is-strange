package thepaperpilot.strange.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import thepaperpilot.strange.Entities.Entity;
import thepaperpilot.strange.Entities.Max;
import thepaperpilot.strange.Item;
import thepaperpilot.strange.Main;
import thepaperpilot.strange.Scene;

public class JunkyardCutscene implements Screen {
    private static final float CAR_SPEED = 4f;
    public Stage stage;
    public Stage ui;
    boolean bottlesPlaced;
    boolean carMoving;
    Table inventoryTable;
    Max max;
    Entity bottles;
    Entity car;
    Actor[] entities;
    boolean shooting;
    float time = 0;

    public JunkyardCutscene(Actor[] actors, Entity car, Max max) {
        this.max = max;
        this.car = car;
        this.entities = actors;
        stage = new Stage(new StretchViewport(256, 144));
        ui = new Stage(new StretchViewport(640, 360));

        stage.addActor(new Image(Main.manager.get("junkyardBackground.png", Texture.class)));
        for (Actor actor : actors) {
            stage.addActor(actor);
        }
        stage.addActor(car);
        stage.addActor(max);

        bottles = new Entity(new Image(Main.manager.get("bottlesWorld.png", Texture.class)).getDrawable(), Scene.FIFTH.screen, 140, 16);
        bottles.remove();
        bottles.clearListeners();

        // TODO clicking on duct tape sort of works

        inventoryTable = new Table(Main.skin);
        inventoryTable.setSize(ui.getWidth() - 30, 38);
        inventoryTable.setPosition(30, ui.getHeight() - 38);
        inventoryTable.setBackground(Main.skin.getDrawable("default-round"));
        inventoryTable.pad(2);

        updateInventory();

        ui.addActor(inventoryTable);

        max.target = 140;
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
                    Scene.updateInventory();
                }
            });
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        if (shooting) {
            time += delta;
            if (time > Max.ANIM_SPEED * 7) {
                Main.manager.get("explosion.wav", Sound.class).play();
                Main.inventory.remove(Item.GUN);
                Main.inventory.remove(Item.GUN);
                Scene.updateInventory();
                updateInventory();
                carMoving = true;
                shooting = false;
            }
        } else if (max.x == max.target && !carMoving) {
            if (bottlesPlaced) {
                max.shooting = true;
                shooting = true;
                max.time = 0;

            } else {
                stage.addActor(bottles);
                bottles.setZIndex(1);
                Main.inventory.remove(Item.BOTTLES);
                Main.selected.remove(Item.BOTTLES);
                Scene.updateInventory();
                updateInventory();
                max.target = 50;
                bottlesPlaced = true;
            }
        }
        if (carMoving) {
            car.setPosition(car.getX() + CAR_SPEED * delta, car.getY());
            if (car.getX() > 90) {
                car.clearListeners();
                // TODO glass breaking sound?
                Stage toStage = Scene.FIFTH.screen.stage;
                toStage.addActor(new Image(Main.manager.get("junkyardBackground.png", Texture.class)));
                for (Actor actor : entities) {
                    toStage.addActor(actor);
                }
                toStage.addActor(car);
                toStage.addActor(max);
                Main.changeScreen(Scene.FIFTH.screen);
            }
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
    }
}
